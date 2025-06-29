package kektor.auction.sink.controller;


import kektor.auction.sink.service.DataSynchronizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class SinkApi {

    final DataSynchronizationService syncService;

    //TODO Debezium snapshot table signal
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/resynchronize")
    public void commitBid() {
        syncService.resynchronize();
    }


}
