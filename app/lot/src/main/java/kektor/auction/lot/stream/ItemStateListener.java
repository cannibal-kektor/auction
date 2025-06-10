package kektor.auction.lot.stream;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ItemStateListener {

    final StreamResponseHelper streamResponseHelper;

    @Async
    @TransactionalEventListener(classes = LotChangedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT)
    public void lotChanged(LotChangedEvent event) {
        streamResponseHelper.notifySseEmitters(event);
    }

}
