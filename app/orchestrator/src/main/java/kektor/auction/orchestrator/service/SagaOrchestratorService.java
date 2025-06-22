package kektor.auction.orchestrator.service;

import kektor.auction.orchestrator.dto.LotDto;
import kektor.auction.orchestrator.dto.BidRequestDto;
import kektor.auction.orchestrator.exception.*;
import kektor.auction.orchestrator.model.Saga;
import kektor.auction.orchestrator.service.client.LotServiceClient;
import kektor.auction.orchestrator.service.client.PaymentServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SagaOrchestratorService {

    static final BigDecimal  MIN_STEP = new BigDecimal("10.0");

    final LotServiceClient lotService;
    final PaymentServiceClient paymentService;
    final SagaManager sagaManager;

    @Async
    public void placeBid(BidRequestDto bidRequestDto, DeferredResult<Long> deferredResult) {
        Long lotId = bidRequestDto.lotId();
        LotDto lotDto = lotService.fetchLot(lotId);
        Instant creationTime = Instant.now();
        validateNewBid(lotDto, bidRequestDto, creationTime);
        validateEnoughFunds(bidRequestDto);
        Saga saga = sagaManager.prepareSaga(bidRequestDto, lotDto, creationTime);
//        Long recheckedVersion = lotService.fetchVersion(lotId);
//        if (!newBidRequestDto.lotVersion().equals(recheckedVersion))
//            throw new StaleLotVersionException(lotId, recheckedVersion, newBidRequestDto.lotVersion(), saga.getSagaId());
        deferredResult.setResult(saga.getSagaId());
        sagaManager.executeSaga(saga);
    }


    void validateNewBid(LotDto lot, BidRequestDto bid, Instant creationTime) {
        long lotId = lot.id();
        if (!lot.version().equals(bid.lotVersion())) {
            throw new StaleLotVersionException(lotId, lot.version(), bid.lotVersion());
        }
        if (creationTime.isAfter(lot.auctionEnd())) {
            throw new TooLateBidException(lotId, lot.auctionEnd(), creationTime);
        }
        if (creationTime.isBefore(lot.auctionStart())) {
            throw new TooEarlyBidException(lotId, lot.auctionStart(), creationTime);
        }

        var newBidAmount = bid.amount();
        var initialPrice = lot.initialPrice();
        var highestBid = lot.highestBid();
        if (newBidAmount.subtract(initialPrice).compareTo(MIN_STEP) < 0
                || newBidAmount.subtract(highestBid).compareTo(MIN_STEP) < 0) {
            throw new TooLowBidAmountException(lot.id(),
                    initialPrice.max(highestBid).add(MIN_STEP), newBidAmount);
        }

    }

    void validateEnoughFunds(BidRequestDto bidRequestDto) {
        Long userId = bidRequestDto.bidderId();
        BigDecimal amount = bidRequestDto.amount();
        if (!paymentService.checkEnoughFunds(userId, amount)) {
            throw new NotEnoughAccountFundException(userId, amount);
        }
    }
}
