package kektor.auction.orchestrator.service;

import kektor.auction.orchestrator.dto.LotDto;
import kektor.auction.orchestrator.dto.NewBidRequestDto;
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
    public void placeBid(NewBidRequestDto newBidRequestDto, DeferredResult<Long> deferredResult) {
        Long lotId = newBidRequestDto.lotId();
        LotDto lotDto = lotService.fetchLot(lotId);
        Instant createdOn = Instant.now();
        validateNewBid(lotDto, newBidRequestDto, createdOn);
        Saga saga = sagaManager.prepareSaga(newBidRequestDto, lotDto, createdOn);
        Long recheckedVersion = lotService.fetchVersion(lotId);
        if (!newBidRequestDto.lotVersion().equals(recheckedVersion))
            throw new StaleLotVersionException(lotId, recheckedVersion, newBidRequestDto.lotVersion(), saga.getSagaId());
        deferredResult.setResult(saga.getSagaId());
        sagaManager.executeSaga(saga);
    }


    void validateNewBid(LotDto lot, NewBidRequestDto bid, Instant createdOn) {
        long lotId = lot.id();
        if (!lot.version().equals(bid.lotVersion())) {
            throw new StaleLotVersionException(lotId, lot.version(), bid.lotVersion());
        }

        if (createdOn.isAfter(lot.auctionEnd())) {
            throw new TooLateBidException(lotId, lot.auctionEnd(), createdOn);
        }
        if (createdOn.isBefore(lot.auctionStart())) {
            throw new TooEarlyBidException(lotId, lot.auctionStart(), createdOn);
        }

        BigDecimal newBidAmount = bid.amount();
        if (newBidAmount.compareTo(lot.initialPrice()) <= 0
                || newBidAmount.compareTo(lot.highestBid()) <= 0) {
            throw new TooLowBidAmountException(lot.id(),
                    lot.initialPrice().max(lot.highestBid()), newBidAmount);
        }

    }
}
