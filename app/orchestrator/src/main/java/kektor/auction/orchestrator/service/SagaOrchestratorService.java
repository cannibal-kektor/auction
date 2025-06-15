package kektor.auction.orchestrator.service;

import kektor.auction.orchestrator.dto.LotDto;
import kektor.auction.orchestrator.dto.BidRequestDto;
import kektor.auction.orchestrator.exception.*;
import kektor.auction.orchestrator.model.Saga;
import kektor.auction.orchestrator.service.client.LotServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SagaOrchestratorService {

    final LotServiceClient lotService;
    final SagaManager sagaManager;

    @Async
    public void placeBid(BidRequestDto bidRequestDto, DeferredResult<Long> deferredResult) {
        Long lotId = bidRequestDto.lotId();
        LotDto lotDto = lotService.fetchLot(lotId);
        validateNewBid(lotDto, bidRequestDto);
        Saga saga = sagaManager.prepareSaga(bidRequestDto, lotDto);
        deferredResult.setResult(saga.getSagaId());
        Long recheckedVersion = lotService.fetchVersion(lotId);
        if (!bidRequestDto.itemVersion().equals(recheckedVersion))
            throw new ConcurrentSagaException();
        sagaManager.executeSaga(saga);
    }


    void validateNewBid(LotDto lot, BidRequestDto bid) {
        long lotId = lot.id();
        if (!lot.version().equals(bid.itemVersion())) {
            throw new StaleItemVersionException(lotId, lot.version(), bid.itemVersion());
        }
        Instant now = Instant.now();
        if (now.isAfter(lot.auctionEnd())) {
            throw new TooLateBidException(lotId, lot.auctionEnd(), now);
        }
        if (now.isBefore(lot.auctionStart())) {
            throw new TooEarlyBidException(lotId, lot.auctionStart(), now);
        }

        BigDecimal newBidAmount = bid.amount();
        if (newBidAmount.compareTo(lot.initialPrice()) <= 0
                || newBidAmount.compareTo(lot.highestBid()) <= 0) {
            throw new TooLowBidAmountException(lot.id(),
                    lot.initialPrice().max(lot.highestBid()), newBidAmount);
        }

    }
}
