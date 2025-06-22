package kektor.auction.bid.service.client;

import kektor.auction.bid.dto.BidRequestDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(url = "/orchestrator"
        , contentType = MediaType.APPLICATION_JSON_VALUE,
        accept = MediaType.APPLICATION_JSON_VALUE)
public interface SagaOrchestratorClient {

    @PostExchange("/place-bid")
    Long placeBid(@RequestBody BidRequestDto bidRequestDto);

}
