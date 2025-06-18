package kektor.auction.orchestrator.exception;

import lombok.Getter;

@Getter
public final class StaleLotVersionException extends RuntimeException {

    private static final String STALE_VERSION = "Submitted lot (id=%d) version is stale. Current version = %d. Submitted version= %d";
    private static final String STALE_VERSION_WITH_SAGA = "Submitted lot (id=%d) version is stale. Current version = %d. Submitted version= %d. Saga id: %d";

    long lotId;
    long currentVersion;
    long submittedVersion;
    long sagaId;

    public StaleLotVersionException(long lotId, long currentVersion, long submittedVersion) {
        super(String.format(STALE_VERSION, lotId, currentVersion, submittedVersion));
        this.lotId = lotId;
        this.currentVersion = currentVersion;
        this.submittedVersion = submittedVersion;
    }

    public StaleLotVersionException(long lotId, long sagaId, long currentVersion, long submittedVersion) {
        super(String.format(STALE_VERSION_WITH_SAGA, lotId, currentVersion, submittedVersion, sagaId));
        this.lotId = lotId;
        this.sagaId = sagaId;
        this.currentVersion = currentVersion;
        this.submittedVersion = submittedVersion;
    }
}
