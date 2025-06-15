package kektor.auction.orchestrator.service.client;

import kektor.auction.orchestrator.dto.BidDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(url = "/api")
public interface BidServiceClient {

    @PostExchange(value = "/create", contentType = MediaType.APPLICATION_JSON_VALUE)
    void createBid(@RequestBody BidDto newBidDto);

    @PostExchange("/commit")
    void commitBid(Long sagaId);

    @PostExchange("/compensate")
    void compensateBid(Long sagaId);

}
