package kektor.auction.orchestrator.service.client;

import kektor.auction.orchestrator.dto.BidCreateDto;
import kektor.auction.orchestrator.dto.BidDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Optional;

@HttpExchange(url = "/api")
public interface BidServiceClient {

    @PostExchange(value = "/create", contentType = MediaType.APPLICATION_JSON_VALUE)
    void createBid(@RequestBody BidCreateDto newBidCreateDto);

    @PostExchange("/commit")
    void commitBid(Long sagaId);

    @PostExchange("/reject")
    void rejectBid(Long sagaId);

    @GetExchange(value = "/saga/{sagaId}", accept = MediaType.APPLICATION_JSON_VALUE)
    Optional<BidDto> fetchBid(@PathVariable("sagaId") Long sagaId);

}
