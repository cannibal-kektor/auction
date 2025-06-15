package kektor.auction.orchestrator.service.step;


import kektor.auction.orchestrator.log.LogHelper;
import kektor.auction.orchestrator.service.SagaPhase;
import kektor.auction.orchestrator.mapper.SagaMapper;
import kektor.auction.orchestrator.model.Saga;
import kektor.auction.orchestrator.service.client.BidServiceClient;
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
public class BidCreationSagaStep implements SagaStep {

    final BidServiceClient bidService;
    final SagaMapper sagaMapper;
    final LogHelper logHelper;
    private Saga saga;

    @Override
    public SagaStep setSaga(Saga saga) {
        this.saga = saga;
        return this;
    }

    @Override
    public void execute() {
        var newBid = sagaMapper.toBid(saga);
        bidService.createBid(newBid);
    }

    @Override
    public void commit() {
        bidService.commitBid(saga.getSagaId());
    }

    @Override
    public void compensate() {
        bidService.compensateBid(saga.getSagaId());
    }

    @SneakyThrows
    @Override
    public Void handleExecutionException(Throwable ex) {
        logHelper.logBidCreateStepException(SagaPhase.EXECUTE, saga, ex);
        throw ex;
    }

    @SneakyThrows
    @Override
    public Void handleCommitException(Throwable ex) {
        logHelper.logBidCreateStepException(SagaPhase.COMMIT, saga, ex);
        throw ex;
    }

    @SneakyThrows
    @Override
    public Void handleCompensateException(Throwable ex) {
        logHelper.logBidCreateStepException(SagaPhase.COMPENSATE, saga, ex);
        throw ex;
    }
}
