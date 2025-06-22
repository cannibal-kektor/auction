package kektor.auction.bid.dto.msg;


import org.springframework.http.ProblemDetail;

public record SagaStatusMessage(Long sagaId, SagaStatus status, ProblemDetail problemDetail) {
}
