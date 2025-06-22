package kektor.auction.orchestrator.controller;


import jakarta.validation.Valid;
import kektor.auction.orchestrator.dto.BidRequestDto;
import kektor.auction.orchestrator.service.SagaOrchestratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;


@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/orchestrator"
        , consumes = MediaType.APPLICATION_JSON_VALUE
        , produces = MediaType.APPLICATION_JSON_VALUE)
public class SagaOrchestratorApi {

    final SagaOrchestratorService sagaService;

    @PostMapping("/place-bid")
    public DeferredResult<Long> placeBid(@RequestBody @Valid BidRequestDto bidRequestDto) {
        var deferredResult = new DeferredResult<Long>();
        sagaService.placeBid(bidRequestDto, deferredResult);
        return deferredResult;
    }


}
