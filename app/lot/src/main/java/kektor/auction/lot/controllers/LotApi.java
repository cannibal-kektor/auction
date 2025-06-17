package kektor.auction.lot.controllers;


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
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class LotApi {

    final LotService lotService;

    //    @JsonView(ItemDto.View.WithCategories.class)
    @GetMapping(value = "/{lotId}")
    public Callable<LotDto> get(@PathVariable("lotId") @Positive Long id) {
        return () -> lotService.get(id);
    }

    @GetMapping(value = "/{lotId}/version")
    public Callable<Long> fetchCurrentVersion(@PathVariable("lotId") @Positive Long id) {
        return () -> lotService.getCurrentVersion(id);
    }

    //    @JsonView(ItemDto.View.WithCategories.class)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Callable<LotDto> create(@RequestBody
                                        @Validated LotCreateDto dto) {
        return () -> lotService.create(dto);
    }

    //    @JsonView(ItemDto.View.WithCategories.class)
    @PutMapping(path = "/{lotId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Callable<LotDto> update(@RequestBody @Validated LotUpdateDto dto) {
        return () -> lotService.update(dto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(path = "/{lotId}/updateHighestBid", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Callable<Void> updateHighestBid(@PathVariable("lotId") @Positive Long id,
                                           @RequestParam("version") @Positive Long version,
                                           @RequestParam("highestBid") @Positive BigDecimal highestBid,
                                           @RequestParam("winningBidId") Long winningBidId,
                                           @RequestParam("isRollback") boolean isRollback ) {
        return () -> {
            lotService.updateHighestBid(id, version, highestBid, winningBidId, isRollback);
            return null;
        };
    }


}
