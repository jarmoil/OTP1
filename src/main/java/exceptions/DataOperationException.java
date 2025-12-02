package exceptions;

public class DataOperationException extends RuntimeException {
    public DataOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
