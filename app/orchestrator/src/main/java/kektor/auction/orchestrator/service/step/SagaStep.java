package kektor.auction.orchestrator.service.step;

import kektor.auction.orchestrator.model.Saga;

public interface SagaStep {

    SagaStep setSaga(Saga saga);

    void execute();

    void commit();

    void compensate();

    Void handleExecutionException(Throwable exception);

    Void handleCommitException(Throwable exception);

    Void handleCompensateException(Throwable throwable);
}
