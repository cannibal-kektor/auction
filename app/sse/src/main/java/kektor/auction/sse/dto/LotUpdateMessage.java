package kektor.auction.sse.dto;

import java.time.Instant;

public interface LotUpdateMessage {

    Long lotId();

    Instant timestamp();

}
