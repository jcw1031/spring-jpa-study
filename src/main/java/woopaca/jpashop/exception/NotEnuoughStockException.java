package woopaca.jpashop.exception;

public class NotEnuoughStockException extends RuntimeException {

    public NotEnuoughStockException() {
        super();
    }

    public NotEnuoughStockException(String message) {
        super(message);
    }

    public NotEnuoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnuoughStockException(Throwable cause) {
        super(cause);
    }
}
