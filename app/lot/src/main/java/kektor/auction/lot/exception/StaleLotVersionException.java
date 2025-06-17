package kektor.auction.lot.exception;

import lombok.Getter;

@Getter
public final class StaleLotVersionException extends LotException {

    private static final String STALE_VERSION = "Submitted lot (id=%d) version is stale. Current version = %d. submitted version= %d";

    long currentVersion;

    long submittedVersion;

    public StaleLotVersionException(long lotId, long currentVersion, long submittedVersion) {
        super(String.format(STALE_VERSION, lotId, currentVersion, submittedVersion), lotId);
        this.currentVersion = currentVersion;
        this.submittedVersion = submittedVersion;
    }
}
