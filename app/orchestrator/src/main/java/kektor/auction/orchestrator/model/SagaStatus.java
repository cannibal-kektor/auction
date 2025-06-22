package kektor.auction.orchestrator.model;

public enum SagaStatus {
    ACTIVE,
    STALLED,
    COMPLETED,
    COMPENSATED
}
