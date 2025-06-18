package kektor.auction.bid.service;

import kektor.auction.bid.dto.BidDto;
import kektor.auction.bid.dto.NewBidRequestDto;
import kektor.auction.bid.dto.SagaBidDto;
import kektor.auction.bid.exception.BidNotFoundBySagaException;
import kektor.auction.bid.exception.BidNotFoundException;
import kektor.auction.bid.exception.StaleLotVersionException;
import kektor.auction.bid.mapper.BidMapper;
import kektor.auction.bid.model.Bid;
import kektor.auction.bid.model.BidStatus;
import kektor.auction.bid.repository.BidRepository;
import kektor.auction.bid.service.client.LotServiceClient;
import kektor.auction.bid.service.client.SagaOrchestratorClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.async.DeferredResult;


@Service
@RequiredArgsConstructor
public class BidService {

    final BidRepository bidRepository;
    final BidMapper mapper;
    final SagaOrchestratorClient orchestratorServiceClient;
    final LotServiceClient lotServiceClient;
    final PendingConfirmationService pendingConfirmationService;

    //TODO Cacheable
    @Transactional(readOnly = true)
    public BidDto getBid(Long bidId) {
        return bidRepository.findById(bidId)
                .map(mapper::toDto)
                .orElseThrow(() -> new BidNotFoundException(bidId));
    }

    //TODO Cacheable
    @Transactional(readOnly = true)
    public BidDto getBidBySaga(Long sagaId) {
        return bidRepository.findBySagaId(sagaId)
                .map(mapper::toDto)
                .orElseThrow(() -> new BidNotFoundBySagaException(sagaId));
    }


    public void placeBid(NewBidRequestDto newBidRequestDto, DeferredResult<ResponseEntity<Void>> deferredResult) {
        long sagaId = orchestratorServiceClient.placeBid(newBidRequestDto);
        pendingConfirmationService.addWaitingClient(sagaId, deferredResult);
    }


    public Long create(SagaBidDto bidDto) {
        Bid bid = mapper.toModel(bidDto);
        bid = bidRepository.save(bid);
        try {
            lotServiceClient.updateBidInfo(bidDto.lotId(), bidDto.lotVersion(),
                    bidDto.amount(), bid.getId(), false);
        } catch (RestClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
//                throw new BidConcurrencyException(bid.getLotId(), bidDto.lotVersion(), bid.getSagaId());
                ProblemDetail detail = e.getResponseBodyAs(ProblemDetail.class);
                long currentV = (long) detail.getProperties().get("currentLotVersion");
                throw new StaleLotVersionException(bid.getLotId(), bidDto.sagaId(),
                        currentV, bidDto.lotVersion());
            }
        }
        return bid.getId();
    }

    @Transactional
    public void commit(Long sagaId) {
        bidRepository.findBySagaId(sagaId)
                .map(bid -> bid.setStatus(BidStatus.ACCEPTED))
                .orElseThrow(() -> new BidNotFoundBySagaException(sagaId));
    }

    @Transactional
    public void reject(Long sagaId) {
        bidRepository.findBySagaId(sagaId)
                .map(bid -> bid.setStatus(BidStatus.REJECTED))
                .orElseThrow(() -> new BidNotFoundBySagaException(sagaId));
    }

}
