package kektor.auction.sse.service.client;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url = "/api")
public interface LotServiceClient {

    @GetExchange(value = "/{lotId}/version", accept = MediaType.APPLICATION_JSON_VALUE)
    Long fetchVersion(@PathVariable("lotId") Long lotId);

}
