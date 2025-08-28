package kektor.auction.query.exception;

public class StreamIOException extends RuntimeException {

    public StreamIOException(String message) {
        super(message);
    }

    public StreamIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
