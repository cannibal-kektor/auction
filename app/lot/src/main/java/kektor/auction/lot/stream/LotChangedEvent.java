package kektor.auction.lot.stream;

import kektor.auction.lot.dto.LotFetchDto;
import org.springframework.context.ApplicationEvent;


public abstract sealed class LotChangedEvent extends ApplicationEvent permits LotChangedEvent.LotUpdated {

    public LotChangedEvent(Object source) {
        super(source);
    }

    abstract Long getLotId();


    public final static class LotUpdated extends LotChangedEvent {

        public LotUpdated(Object source) {
            super(source);
        }

        @Override
        Long getLotId() {
            return ((LotFetchDto) getSource()).id();
        }
    }
}
