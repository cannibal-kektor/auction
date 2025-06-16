package kektor.auction.bid.service;

import kektor.auction.bid.dto.BidMessage;
import kektor.auction.bid.dto.OrchestratedBidDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class PendingConfirmationService {

    ConcurrentMap<Long, DeferredResult<ResponseEntity<Void>>> pendingConfirmations = new ConcurrentHashMap<>();

    public void addWaitingClient(long sagaId, DeferredResult<ResponseEntity<Void>> deferredResult) {
        pendingConfirmations.put(sagaId, deferredResult);
    }

    //TODO Удалять зависшие, добавить connection timeout + handler в deferred result
    public void notifyWaitingClient(long sagaId, BidMessage bidMessage) {
        var pendingResult = pendingConfirmations.remove(sagaId);
        if (pendingResult != null) {
            ResponseEntity<Void> response = switch (bidMessage.bidStatus()) {
                case ACCEPTED -> ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                case REJECTED_CONCURRENT -> ResponseEntity.status(HttpStatus.CONFLICT).build();
                case REJECTED, REJECTED_PAYMENT -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            };
            pendingResult.setResult(response);
        }
    }

}
