package kektor.auction.lot.stream;

import kektor.auction.lot.exception.StreamIOException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Component
public class StreamResponseHelper {

    private static final String PING = "Ping";

    ConcurrentMap<Long, Queue<SseEmitter>> sseEmitters = new ConcurrentHashMap<>();

    public void registerSseEmitter(Long lotId, SseEmitter sseEmitter) {

        sseEmitter.onCompletion(() -> {
            sseEmitters.get(lotId).remove(sseEmitter);
            sseEmitters.computeIfPresent(lotId, (key, queue) -> {
                if (queue.isEmpty()) {
                    return null;
                }
                return queue;
            });
        });
//        sseEmitter.onError(ex -> {
//            System.out.println("ON ERROR " + lotId);
//            sseEmitters.get(lotId).remove(sseEmitter);
//        });
//        sseEmitter.onTimeout(() -> {
//            System.out.println("ON TIMEOUT " + lotId);
//            sseEmitter.completeWithError(new StreamIOException("Async request timeout"));
//            sseEmitters.get(lotId).remove(sseEmitter);
//        });


        sseEmitters.compute(lotId, (key, queue) -> {
            if (queue == null) {
                queue = new ConcurrentLinkedQueue<>();
            }
            queue.add(sseEmitter);
            return queue;
        });

    }

    public void notifySseEmitters(LotChangedEvent event) {
        Optional.ofNullable(sseEmitters.get(event.getLotId()))
                .ifPresent(emitters -> emitters
                        .forEach(sseEmitter -> sendToEmitterSilently(event, sseEmitter)));
    }

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void pingSseEmitters() {
        sseEmitters.forEach(
                (id, sseEmittersQueue) -> sseEmittersQueue.forEach(
                        emitter -> sendToEmitterSilently(PING, emitter)
                )
        );
    }

    public <T> void sendToEmitter(T resource, ResponseBodyEmitter emitter) {
        sendToEmitter(resource, emitter, false);
    }

    public <T> void sendToEmitterSilently(T resource, ResponseBodyEmitter emitter) {
        sendToEmitter(resource, emitter, true);
    }

    private <T> void sendToEmitter(T resource, ResponseBodyEmitter emitter, boolean ignoreException) {
        try {
            emitter.send(resource);
        } catch (Exception e) {
            emitter.completeWithError(e);
            if (!ignoreException) {
                throw new StreamIOException("IO Streaming Error", e);
            }
        }
    }


}

