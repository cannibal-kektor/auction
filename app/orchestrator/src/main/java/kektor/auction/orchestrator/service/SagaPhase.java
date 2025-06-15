package kektor.auction.orchestrator.service;

public enum SagaPhase {
    EXECUTE,
    COMMIT,
    COMPENSATE
}
