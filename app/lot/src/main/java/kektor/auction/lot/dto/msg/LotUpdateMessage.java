package kektor.auction.lot.dto.msg;

import java.time.Instant;

public interface LotUpdateMessage {

    Long lotId();

    Instant timestamp();
}
