package kektor.auction.bid.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import kektor.auction.bid.dto.BidDto;
import kektor.auction.bid.dto.NewBidRequestDto;
import kektor.auction.bid.dto.SagaBidDto;
import kektor.auction.bid.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;


@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/api"
        , produces = MediaType.APPLICATION_JSON_VALUE
        , consumes = MediaType.APPLICATION_JSON_VALUE)
public class BidApi {

    final BidService bidService;

    @GetMapping("/{bidId}")
    public Callable<BidDto> getBid(@PathVariable("bidId") @Positive Long bidId) {
        return () -> bidService.getBid(bidId);
    }

    @GetMapping("/saga/{sagaId}")
    public Callable<BidDto> getBidBySaga(@PathVariable("sagaId") @Positive Long sagaId) {
        return () -> bidService.getBidBySaga(sagaId);
    }

    @PostMapping("/placeBid")
    public DeferredResult<ResponseEntity<Void>> placeBid(@RequestBody
                                                         @Valid NewBidRequestDto newBid) {
        var deferredResult = new DeferredResult<ResponseEntity<Void>>(30000L);
        bidService.placeBid(newBid, deferredResult);
        return deferredResult;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public Callable<Long> createBid(@RequestBody
                                    @Valid SagaBidDto sagaBidDto) {
        return () -> bidService.create(sagaBidDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/commit")
    public Callable<Void> commitBid(@RequestBody @Positive Long sagaId) {
        return () -> {
            bidService.commit(sagaId);
            return null;
        };
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/reject")
    public Callable<Void> compensateBid(@RequestBody @Positive Long sagaId) {
        return () -> {
            bidService.reject(sagaId);
            return null;
        };
    }


}
