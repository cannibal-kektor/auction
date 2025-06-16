package kektor.auction.bid.controllers;

import jakarta.validation.constraints.Positive;
import kektor.auction.bid.dto.BidDto;
import kektor.auction.bid.dto.NewBidRequestDto;
import kektor.auction.bid.dto.OrchestratedBidDto;
import kektor.auction.bid.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.service.annotation.GetExchange;

import java.util.concurrent.Callable;


@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class BidApi {

    final BidService bidService;

    @GetMapping(value = "/{bidId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Callable<BidDto> getBid(@PathVariable("bidId") @Positive Long bidId) {
        return () -> bidService.getBid(bidId);
    }

    @GetMapping(value = "/saga/{sagaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Callable<BidDto> getBidBySaga(@PathVariable("sagaId") @Positive Long sagaId) {
        return () -> bidService.getBidBySaga(sagaId);
    }


    //    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/placeBid", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResponseEntity<Void>> placeBid(@RequestBody
                                                         @Validated NewBidRequestDto newBid) {
        DeferredResult<ResponseEntity<Void>> deferredResult = new DeferredResult<>();
        bidService.placeBid(newBid, deferredResult);
        return deferredResult;
    }


    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Callable<Long> createBid(@RequestBody
                                    @Validated OrchestratedBidDto orchestratedBidDto) {
        return () -> bidService.create(orchestratedBidDto);
    }

    @PostMapping(value = "/commit")
    public Callable<Void> commitBid(Long sagaId) {
        return () -> {
            bidService.commit(sagaId);
            return null;
        };
    }

    @PostMapping(value = "/reject")
    public Callable<Void> compensateBid(Long sagaId) {
        return () -> {
            bidService.reject(sagaId);
            return null;
        };
    }


}
