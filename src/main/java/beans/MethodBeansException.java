package beans;

public class MethodBeansException extends RuntimeException {

    public MethodBeansException(String message) {
        super(message);
    }

    public MethodBeansException(String message, Throwable cause) {
        super(message, cause);
    }
}
