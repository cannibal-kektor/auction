package kektor.auction.sse.service;

import kektor.auction.sse.exception.LotNotFoundException;
import kektor.auction.sse.exception.StaleLotVersionException;
import kektor.auction.sse.service.client.LotServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.springframework.http.HttpStatus.NOT_FOUND;


@Service
@RequiredArgsConstructor
public class SseService {

    final SseManager sseManager;
    final LotServiceClient lotService;

    @Async
    public void subscribeToLotUpdates(Long lotId, Long lotVersion, SseEmitter sseEmitter) {

        long currentLotVersion = 0;
        try {
            currentLotVersion = lotService.fetchVersion(lotId);
        } catch (RestClientResponseException e) {
            if (e.getStatusCode() == NOT_FOUND) {
                throw new LotNotFoundException(lotId);
            }
            throw e;
        }

        if (!lotVersion.equals(currentLotVersion)) {
            sseEmitter.completeWithError(new StaleLotVersionException(lotId, currentLotVersion, lotVersion));
            return;
        }

        sseManager.registerSseEmitter(lotId, sseEmitter);

    }
}
