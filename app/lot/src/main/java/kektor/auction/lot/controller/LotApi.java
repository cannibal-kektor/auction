package kektor.auction.lot.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import kektor.auction.lot.dto.LotCreateDto;
import kektor.auction.lot.dto.LotDto;
import kektor.auction.lot.dto.LotUpdateDto;
import kektor.auction.lot.service.LotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.concurrent.Callable;


@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api"
        , consumes = MediaType.APPLICATION_JSON_VALUE
        , produces = MediaType.APPLICATION_JSON_VALUE)
public class LotApi {

    final LotService lotService;

    @GetMapping("/{lotId}")
    public Callable<LotDto> get(@PathVariable("lotId") @Positive Long id) {
        return () -> lotService.get(id);
    }

    @GetMapping("/{lotId}/version")
    public Callable<Long> fetchCurrentVersion(@PathVariable("lotId") @Positive Long id) {
        return () -> lotService.getCurrentVersion(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Callable<LotDto> create(@RequestBody @Valid LotCreateDto dto) {
        return () -> lotService.create(dto);
    }

    @PutMapping("/update")
    public Callable<LotDto> update(@RequestBody @Valid LotUpdateDto dto) {
        return () -> lotService.update(dto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(path = "/{lotId}/updateHighestBid", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Callable<Void> updateHighestBid(@PathVariable("lotId") @Positive Long id,
                                           @RequestParam("version") @Positive Long version,
                                           @RequestParam("highestBid") @Positive BigDecimal highestBid,
                                           @RequestParam("winningBidId") Long winningBidId,
                                           @RequestParam("isRollback") boolean isRollback) {
        return () -> {
            lotService.updateHighestBid(id, version, highestBid, winningBidId, isRollback);
            return null;
        };
    }


}
