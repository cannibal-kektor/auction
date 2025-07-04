package kektor.auction.bid.service;

import kektor.auction.bid.dto.BidDto;
import kektor.auction.bid.dto.BidRequestDto;
import kektor.auction.bid.dto.BidCreateDto;
import kektor.auction.bid.exception.BidConcurrencyException;
import kektor.auction.bid.exception.BidNotFoundBySagaException;
import kektor.auction.bid.exception.BidNotFoundException;
import kektor.auction.bid.mapper.BidMapper;
import kektor.auction.bid.model.Bid;
import kektor.auction.bid.model.BidStatus;
import kektor.auction.bid.repository.BidRepository;
import kektor.auction.bid.service.client.SagaOrchestratorClient;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;


@Service
@RequiredArgsConstructor
public class BidService {

    final BidRepository bidRepository;
    final BidMapper mapper;
    final SagaOrchestratorClient orchestratorServiceClient;
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


    public void placeBid(BidRequestDto bidRequestDto, DeferredResult<ResponseEntity<Object>> deferredResult) {
        long sagaId = orchestratorServiceClient.placeBid(bidRequestDto);
        pendingConfirmationService.addWaitingClient(sagaId, deferredResult);
    }


    @Transactional
    public Long create(BidCreateDto bidDto) {
        Bid bid = mapper.toModel(bidDto);
        try {
            bid = bidRepository.saveAndFlush(bid);
        } catch (DataIntegrityViolationException e) {
            throw new BidConcurrencyException(bidDto);
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
