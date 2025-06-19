package kektor.auction.sse.controllers;

import kektor.auction.sse.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class SseApi {

    final SseService sseService;

    @GetMapping(path = "/{lotId}/sse-events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribeToItemEvents(@PathVariable("lotId") Long lotId,
                                                            @RequestParam Long lotVersion) {
        var sseEmitter = new SseEmitter(-1L);
        sseService.subscribeToLotUpdates(lotId, lotVersion, sseEmitter);
        return ResponseEntity.ok().
                contentType(MediaType.TEXT_EVENT_STREAM).
                body(sseEmitter);
    }


}
