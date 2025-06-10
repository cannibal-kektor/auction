package kektor.auction.lot.controllers;


import jakarta.validation.constraints.Positive;
import kektor.auction.lot.dto.LotCreateDto;
import kektor.auction.lot.dto.LotFetchDto;
import kektor.auction.lot.dto.LotUpdateDto;
import kektor.auction.lot.service.LotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.Callable;


@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/lots", produces = MediaType.APPLICATION_JSON_VALUE)
public class LotApi {

    final LotService lotService;

    //    @JsonView(ItemDto.View.WithCategories.class)
    @GetMapping(value = "/{lotId}")
    public Callable<LotFetchDto> get(@PathVariable("lotId") @Positive Long id) {
        return () -> lotService.get(id);
    }

    //    @JsonView(ItemDto.View.WithCategories.class)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Callable<LotFetchDto> create(@RequestBody
                                        @Validated LotCreateDto dto) {
        return () -> lotService.create(dto);
    }

    //    @JsonView(ItemDto.View.WithCategories.class)
    @PutMapping(path = "/{lotId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Callable<LotFetchDto> update(@RequestBody @Validated LotUpdateDto dto) {
        return () -> lotService.update(dto);
    }


    @GetMapping(path = "/{lotId}/sse-events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribeToItemEvents(@PathVariable("lotId") @Positive Long lotId,
                                                            @RequestParam Long lotVersion) {
        var sseEmitter = new SseEmitter(-1L);
        lotService.subscribeSseEmitter(lotId, lotVersion, sseEmitter);
        return ResponseEntity.ok().
                contentType(MediaType.TEXT_EVENT_STREAM).
                body(sseEmitter);
    }


}
