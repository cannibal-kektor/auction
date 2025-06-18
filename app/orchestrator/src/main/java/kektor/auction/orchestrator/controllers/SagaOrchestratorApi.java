package kektor.auction.orchestrator.controllers;


import kektor.auction.orchestrator.dto.NewBidRequestDto;
import kektor.auction.orchestrator.service.SagaOrchestratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;


@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/orchestrator")
public class SagaOrchestratorApi {

    final SagaOrchestratorService sagaService;

    @PostMapping(path = "/place-bid", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<Long> placeBid(@RequestBody @Validated NewBidRequestDto newBidRequestDto) {
        var deferredResult = new DeferredResult<Long>();
        sagaService.placeBid(newBidRequestDto, deferredResult);
        return deferredResult;
    }


}
