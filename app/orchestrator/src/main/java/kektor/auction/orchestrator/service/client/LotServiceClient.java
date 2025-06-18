package kektor.auction.orchestrator.service.client;

import kektor.auction.orchestrator.dto.LotDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.math.BigDecimal;

@HttpExchange(url = "/api")
public interface LotServiceClient {

    @GetExchange(value = "/{lotId}", accept = MediaType.APPLICATION_JSON_VALUE)
    LotDto fetchLot(@PathVariable("lotId") Long lotId);

    @GetExchange(value = "/{lotId}/version")
    Long fetchVersion(@PathVariable("lotId") Long lotId);


    @PostExchange(value = "/{lotId}/updateHighestBid", contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    void updateBidInfo(@PathVariable("lotId") Long id,
                       @RequestParam("version") Long version,
                       @RequestParam("highestBid") BigDecimal highestBid,
                       @RequestParam("winningBidId") Long winningBidId,
                       @RequestParam("isRollback") boolean isRollback);



}
