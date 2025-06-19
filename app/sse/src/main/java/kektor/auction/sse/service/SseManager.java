package kektor.auction.sse.service;

import kektor.auction.sse.dto.LotUpdateMessage;
import kektor.auction.sse.exception.StreamIOException;
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
public class SseManager {

    private static final String PING = "Ping";

    ConcurrentMap<Long, Queue<SseEmitter>> sseEmitters = new ConcurrentHashMap<>();

    public void registerSseEmitter(Long itemId, SseEmitter sseEmitter) {

        sseEmitter.onCompletion(() -> {
            sseEmitters.get(itemId).remove(sseEmitter);
            sseEmitters.computeIfPresent(itemId, (_, queue) -> {
                if (queue.isEmpty()) {
                    return null;
                }
                return queue;
            });
        });
//        sseEmitter.onError(ex -> {
//            System.out.println("ON ERROR " + itemId);
//            sseEmitters.get(itemId).remove(sseEmitter);
//        });
//        sseEmitter.onTimeout(() -> {
//            System.out.println("ON TIMEOUT " + itemId);
//            sseEmitter.completeWithError(new StreamIOException("Async request timeout"));
//            sseEmitters.get(itemId).remove(sseEmitter);
//        });

        sseEmitters.compute(itemId, (key, queue) -> {
            if (queue == null) {
                queue = new ConcurrentLinkedQueue<>();
            }
            queue.add(sseEmitter);
            return queue;
        });

    }

    public void notifySseEmitters(LotUpdateMessage event) {
        Optional.ofNullable(sseEmitters.get(event.lotId()))
                .ifPresent(emitters -> emitters
                        .forEach(sseEmitter -> sendToEmitter(event, sseEmitter)));
    }

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void pingSseEmitters() {
        sseEmitters.forEach(
                (_, sseEmittersQueue) -> sseEmittersQueue.forEach(
                        emitter -> sendToEmitter(PING, emitter)
                )
        );
    }


    private <T> void sendToEmitter(T resource, ResponseBodyEmitter emitter) {
        try {
            emitter.send(resource);
        } catch (Exception e) {
            emitter.completeWithError(e);
        }
    }


}

