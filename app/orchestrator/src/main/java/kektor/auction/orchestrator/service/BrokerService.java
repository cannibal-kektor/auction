package kektor.auction.orchestrator.service;

import kektor.auction.orchestrator.dto.msg.SagaStatusMessage;
import kektor.auction.orchestrator.exception.BrokerSendingMessageException;
import kektor.auction.orchestrator.log.LogHelper;
import kektor.auction.orchestrator.mapper.SagaMapper;
import kektor.auction.orchestrator.model.Saga;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrokerService {

    final KafkaTemplate<?, Object> kafkaTemplate;
    final SagaMapper sagaMapper;
    final LogHelper logHelper;

    @Value("${app.kafka.stalled-saga-topic}")
    String stalledSagaTopic;

    @Value("${app.kafka.saga-status-topic}")
    String sagaStatusTopic;

    public void rearrangeStalledCompensation(Saga saga) {
        kafkaTemplate.send(stalledSagaTopic, saga)
                .exceptionally(ex -> {
                    logHelper.logErrorWhileRearrangingStalledCompensation(saga, ex);
                    throw new BrokerSendingMessageException(ex.getMessage(), ex);
                });
    }

    public Void notifySagaStatusUpdate(Saga saga) {
        SagaStatusMessage msg = sagaMapper.toStatusMessage(saga);
        kafkaTemplate.send(sagaStatusTopic, msg)
                .exceptionally(ex -> {
                    logHelper.logErrorWhileSendingSagaStatusUpdate(msg, ex);
                    return null;
                });
        return null;
    }

}
