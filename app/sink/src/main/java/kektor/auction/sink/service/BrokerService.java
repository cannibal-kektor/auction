package kektor.auction.sink.service;


import kektor.auction.sink.dto.msg.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(topicPattern = "${app.kafka.cdc.topic-pattern}",
        groupId = "${spring.application.name}",
        clientIdPrefix = "${HOSTNAME}-${spring.application.name}-consumer",
        concurrency = "4")
public class BrokerService {

    final DataSynchronizationService dataSynchronizationService;

    @KafkaHandler
    public void categoryEvent(@Payload(required = false) CategoryCDC msg,
                              @Header("op") String opType,
                              @Header(KafkaHeaders.RECEIVED_KEY) long categoryId) {
        dataSynchronizationService.syncData(opType, categoryId, msg);
    }

    @KafkaHandler
    public void lotEvent(@Payload(required = false) LotCDC msg,
                         @Header("op") String opType,
                         @Header(KafkaHeaders.RECEIVED_KEY) long lotId) {
        dataSynchronizationService.syncData(opType, lotId, msg);
    }

    @KafkaHandler
    public void lotCategoriesEvent(@Payload(required = false) LotCategoriesCDC msg,
                                   @Header("op") String opType,
                                   @Header(KafkaHeaders.RECEIVED_KEY) long lotId) {
        dataSynchronizationService.syncData(opType, lotId, msg);
    }

    @KafkaHandler
    public void bidEvent(@Payload(required = false) BidCDC msg,
                         @Header("op") String opType,
                         @Header(KafkaHeaders.RECEIVED_KEY) long bidId) {
        dataSynchronizationService.syncData(opType, bidId, msg);
    }

    @KafkaHandler
    public void paymentAccountEvent(@Payload(required = false) PaymentAccountCDC msg,
                                    @Header("op") String opType,
                                    @Header(KafkaHeaders.RECEIVED_KEY) long paymentAccountId) {
        dataSynchronizationService.syncData(opType, paymentAccountId, msg);
    }

    @KafkaHandler
    public void balanceOperationEvent(@Payload(required = false) BalanceOperationCDC msg,
                                      @Header("op") String opType,
                                      @Header(KafkaHeaders.RECEIVED_KEY) long balanceOperationDtoId) {
        dataSynchronizationService.syncData(opType, balanceOperationDtoId, msg);
    }

    @KafkaHandler
    public void sagaEvent(@Payload(required = false) SagaCDC msg,
                          @Header("op") String opType,
                          @Header(KafkaHeaders.RECEIVED_KEY) long sagaId) {
        dataSynchronizationService.syncData(opType, sagaId, msg);
    }


    @KafkaListener(topics = "${app.kafka.cdc.dlt-topic}", groupId = "${spring.application.name}-dlt")
    public void handleDlt(@Payload(required = false) Object message,
                          @Header(KafkaHeaders.DLT_ORIGINAL_TOPIC) String originalTopic,
                          @Header(KafkaHeaders.DLT_EXCEPTION_FQCN) String exceptionFqcn,
                          @Header(KafkaHeaders.DLT_EXCEPTION_MESSAGE) String exceptionMessage,
                          @Header(KafkaHeaders.DLT_EXCEPTION_STACKTRACE) String stacktrace) {
        log.error("Received message in DLT: originalTopic={}, exception={}, exception message={}, dlt message={}",
                originalTopic, exceptionFqcn, exceptionMessage, message);
    }

}
