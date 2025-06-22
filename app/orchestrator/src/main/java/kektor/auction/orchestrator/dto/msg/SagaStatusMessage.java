package kektor.auction.orchestrator.dto.msg;

import kektor.auction.orchestrator.model.SagaStatus;
import org.springframework.http.ProblemDetail;

public record SagaStatusMessage(Long sagaId, SagaStatus status, ProblemDetail problemDetail) {
}
