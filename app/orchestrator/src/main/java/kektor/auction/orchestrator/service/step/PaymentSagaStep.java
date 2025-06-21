package kektor.auction.orchestrator.service.step;

import kektor.auction.orchestrator.dto.BidDto;
import kektor.auction.orchestrator.mapper.SagaMapper;
import kektor.auction.orchestrator.model.Saga;
import kektor.auction.orchestrator.service.client.BidServiceClient;
import kektor.auction.orchestrator.service.client.PaymentServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;


@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class PaymentSagaStep implements SagaStep {

    final PaymentServiceClient paymentService;
    final BidServiceClient bidService;
    final SagaMapper sagaMapper;

    Saga saga;

    @Override
    public SagaStep setSaga(Saga saga) {
        this.saga = saga;
        return this;
    }

    @Override
    public void execute() {
        var reservationDto = sagaMapper.toReservation(saga);
        paymentService.reserveAmount(reservationDto);
    }

    @Override
    public void commit() {
        Long sagaId = saga.getSagaId();
        BidDto bid = bidService.fetchBid(sagaId)
                .orElseThrow();
        paymentService.commitReservation(sagaId, bid.id());
    }

    @Override
    public void compensate() {
        try {
            paymentService.cancelReservation(saga.getSagaId());
        } catch (RestClientResponseException e) {
            if (e.getStatusCode() != HttpStatus.NOT_FOUND)
                throw e;
        }
    }

    @Override
    public Void handleExecutionException(Throwable exception) {
        return null;
    }

    @Override
    public Void handleCommitException(Throwable exception) {
        return null;
    }

    @Override
    public Void handleCompensateException(Throwable throwable) {
        return null;
    }
}
