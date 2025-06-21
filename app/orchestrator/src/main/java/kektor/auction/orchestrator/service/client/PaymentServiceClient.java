package kektor.auction.orchestrator.service.client;

import kektor.auction.orchestrator.dto.ReservationDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.math.BigDecimal;

@HttpExchange(url = "/api"
        , contentType = MediaType.APPLICATION_JSON_VALUE
        , accept = MediaType.APPLICATION_JSON_VALUE)
public interface PaymentServiceClient {

    @PostExchange("/reserve")
    void reserveAmount(@RequestBody ReservationDto reservationDto);

    @PostExchange(value = "/commit", contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    void commitReservation(@RequestParam("sagaId") Long sagaId,
                           @RequestParam("bidId") Long bidId);

    @PostExchange("/cancel")
    void cancelReservation(@RequestBody Long sagaId);

    @PostExchange(value = "/check", contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Boolean checkEnoughFunds(@RequestParam("userId") Long userId,
                             @RequestParam("amount") BigDecimal amount);

}
