package kektor.auction.bid.service;

import kektor.auction.bid.dto.msg.SagaStatusMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class BrokerEventService {

    final PendingConfirmationService pendingConfirmationService;

    @KafkaListener(topics = "${app.kafka.saga-status-topic}", groupId = "${HOSTNAME}",
            clientIdPrefix = "${HOSTNAME}", concurrency = "1")
    public void listenCategoryEvents(SagaStatusMessage msg) {
        pendingConfirmationService.notifyWaitingClient(msg);
    }


}
