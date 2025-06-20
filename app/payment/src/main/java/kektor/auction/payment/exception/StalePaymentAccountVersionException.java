package kektor.auction.payment.exception;

import lombok.Getter;

@Getter
public final class StalePaymentAccountVersionException extends RuntimeException {

    private static final String ACCOUNT_STALE_VERSION = "Submitted payment account (id=%d) version is stale. Current version = %d. Submitted version= %d.";
    private static final String ACCOUNT_STALE_VERSION_WITH_SAGA = "Submitted payment account (id=%d) version is stale. Current version = %d. Submitted version= %d. Saga id: %d";

    long paymentAccountId;
    long currentVersion;
    long submittedVersion;
    long sagaId;

    public StalePaymentAccountVersionException(long paymentAccountId, long currentVersion, long submittedVersion) {
        super(String.format(ACCOUNT_STALE_VERSION, paymentAccountId, currentVersion, submittedVersion));
        this.paymentAccountId = paymentAccountId;
        this.currentVersion = currentVersion;
        this.submittedVersion = submittedVersion;
    }

    public StalePaymentAccountVersionException(long sagaId, long paymentAccountId, long currentVersion, long submittedVersion) {
        super(String.format(ACCOUNT_STALE_VERSION_WITH_SAGA, paymentAccountId, currentVersion, submittedVersion, sagaId));
        this.paymentAccountId = paymentAccountId;
        this.currentVersion = currentVersion;
        this.submittedVersion = submittedVersion;
        this.sagaId = sagaId;
    }
}
