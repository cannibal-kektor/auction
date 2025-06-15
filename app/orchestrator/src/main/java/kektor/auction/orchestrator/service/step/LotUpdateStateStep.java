package kektor.auction.orchestrator.service.step;

import kektor.auction.orchestrator.log.LogHelper;
import kektor.auction.orchestrator.service.SagaPhase;
import kektor.auction.orchestrator.model.Saga;
import kektor.auction.orchestrator.service.client.LotServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class LotUpdateStateStep implements SagaStep {

    final LotServiceClient lotService;
    final LogHelper logHelper;
    private Saga saga;


    @Override
    public SagaStep setSaga(Saga saga) {
        this.saga = saga;
        return this;
    }

    @Override
    public void execute() {
        lotService.updateBidInfo(saga.getLotId(), saga.getItemVersion(),
                saga.getNewBidAmount(), saga.getCompensateBidsCount() + 1);
    }

    @Override
    public void commit() {
    }

    @Override
    public void compensate() {
//        Long upToDateVersion = lotService.fetchVersion(saga.getLotId());
        lotService.updateBidInfo(saga.getLotId(), saga.getItemVersion() + 1,
                saga.getCompensateHighestBid(), saga.getCompensateBidsCount());
    }

    @SneakyThrows
    @Override
    public Void handleExecutionException(Throwable ex) {
        logHelper.logLotUpdateStepException(SagaPhase.EXECUTE, saga, ex);
        throw ex;
    }

    @SneakyThrows
    @Override
    public Void handleCompensateException(Throwable ex) {
        logHelper.logLotUpdateStepException(SagaPhase.COMPENSATE, saga, ex);
        throw ex;
    }

    @SneakyThrows
    @Override
    public Void handleCommitException(Throwable ex) {
        throw ex;
    }
}
