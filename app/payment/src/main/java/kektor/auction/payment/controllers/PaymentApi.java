package kektor.auction.payment.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import kektor.auction.payment.dto.PaymentAccountDto;
import kektor.auction.payment.dto.ReplenishmentDto;
import kektor.auction.payment.dto.ReservationDto;
import kektor.auction.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.concurrent.Callable;


@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/api"
        , produces = MediaType.APPLICATION_JSON_VALUE
        , consumes = MediaType.APPLICATION_JSON_VALUE)
public class PaymentApi {

    final PaymentService paymentService;

    @GetMapping("/{accountId}")
    public Callable<PaymentAccountDto> getAccount(@PathVariable("accountId") @Positive Long accountId,
                                                  @RequestParam(value = "withOperations", defaultValue = "false") boolean withOperations) {
        return () -> paymentService.getAccount(accountId, withOperations);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public Callable<PaymentAccountDto> createAccount(@RequestBody @Positive Long userId) {
        return () -> paymentService.createAccount(userId);
    }

    @PostMapping("/replenish")
    public Callable<PaymentAccountDto> replenishAccount(@RequestBody @Valid ReplenishmentDto replenishmentDto) {
        return () -> paymentService.replenishAmount(replenishmentDto);
    }

    @PostMapping("/reserve")
    public Callable<PaymentAccountDto> reserveAmount(@RequestBody @Valid ReservationDto reservationDto) {
        return () -> paymentService.reserveAmount(reservationDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = "/commit", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Callable<Void> commitReservation(@RequestParam("sagaId") @Positive Long sagaId,
                                            @RequestParam("bidId") @Positive Long bidId) {
        return () -> {
            paymentService.commitReservation(sagaId, bidId);
            return null;
        };
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/cancel")
    public Callable<Void> cancelReservation(@RequestBody @Positive Long sagaId) {
        return () -> {
            paymentService.cancelReservation(sagaId);
            return null;
        };
    }

    @PostMapping(value = "/check", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Callable<Boolean> checkEnoughFunds(@RequestParam("userId") @Positive Long userId,
                                              @RequestParam("amount") BigDecimal amount) {
        return () -> paymentService.checkEnoughFunds(userId, amount);
    }
}
