package kektor.auction.orchestrator.exception;

public class BrokerSendingMessageException extends RuntimeException {

    public BrokerSendingMessageException(String message, Throwable cause) {
        super(message, cause);
    }

}
