package kektor.auction.bid.service;

import kektor.auction.bid.dto.msg.SagaStatusMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class PendingConfirmationService {

    ConcurrentMap<Long, DeferredResult<ResponseEntity<Object>>> pendingConfirmations = new ConcurrentHashMap<>();

    public void addWaitingClient(long sagaId, DeferredResult<ResponseEntity<Object>> deferredResult) {
        pendingConfirmations.put(sagaId, deferredResult);
        deferredResult.onTimeout(() -> {
            pendingConfirmations.remove(sagaId);
            deferredResult.setResult(ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .build());
        });
    }

    public void notifyWaitingClient(SagaStatusMessage msg) {
        var pendingResult = pendingConfirmations.remove(msg.sagaId());
        if (pendingResult != null) {
            ResponseEntity<Object> response = switch (msg.status()) {
                case COMPLETED -> ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                case COMPENSATED, STALLED -> {
                    var problemDetail = msg.problemDetail();
                    if (problemDetail != null) {
                        yield ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
                    }
                    yield ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
                default -> throw new IllegalStateException("Unexpected value: " + msg.status());
            };
            pendingResult.setResult(response);
        }
    }

}
