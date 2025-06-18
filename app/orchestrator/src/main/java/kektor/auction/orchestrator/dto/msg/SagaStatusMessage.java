package kektor.auction.orchestrator.dto.msg;

import kektor.auction.orchestrator.model.SagaStatus;

public record SagaStatusMessage(Long sagaId, SagaStatus sagaStatus) {
}
