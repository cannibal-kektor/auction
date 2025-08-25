package kektor.auction.orchestrator.service.step;


import kektor.auction.orchestrator.dto.BidDto;
import kektor.auction.orchestrator.dto.LotDto;
import kektor.auction.orchestrator.log.LogHelper;
import kektor.auction.orchestrator.model.Saga;
import kektor.auction.orchestrator.service.SagaPhase;
import kektor.auction.orchestrator.service.client.BidServiceClient;
import kektor.auction.orchestrator.service.client.LotServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class LotUpdateSagaStep implements SagaStep {

    final BidServiceClient bidService;
    final LotServiceClient lotService;
    final LogHelper logHelper;

    Saga saga;

    @Override
    public SagaStep setSaga(Saga saga) {
        this.saga = saga;
        return this;
    }

    @Override
    public void execute() {
    }

    @Override
    public void commit() {
        Long sagaId = saga.getSagaId();
        BidDto bid = bidService.fetchBid(sagaId)
                .orElseThrow();
        lotService.updateBidInfo(bid.lotId(), saga.getLotVersion(),
                bid.amount(), bid.id(), bid.bidderId(), false);
    }

    @Override
    public void compensate() {
        Long sagaId = saga.getSagaId();
        Long lotId = saga.getLotId();
        Optional<BidDto> bidOpt = bidService.fetchBid(sagaId);

        if (bidOpt.isPresent()) {
            BidDto bid = bidOpt.get();
            LotDto lot = lotService.fetchLot(lotId);
            if (lot.winningBidId().equals(saga.getCompensateWinningBidId()))
                return;

            if (lot.winningBidId().equals(bid.id()))
                lotService.updateBidInfo(lotId, lot.version(),
                        saga.getCompensateBidAmount(), saga.getCompensateWinningBidId(),
                        saga.getCompensateWinnerId(), true);
        }

    }

    @Override
    public Void handleExecutionException(Throwable ex) {
        return null;
    }

    @SneakyThrows
    @Override
    public Void handleCommitException(Throwable ex) {
        logHelper.logLotUpdateStepException(SagaPhase.COMMIT, saga, ex);
        throw ex;
    }

    @SneakyThrows
    @Override
    public Void handleCompensateException(Throwable ex) {
        logHelper.logLotUpdateStepException(SagaPhase.COMPENSATE, saga, ex);
        throw ex;
    }
}
