package bg.sofia.uni.fmi.mjt.project.todoist.server.exception;

public class AccoutException extends Exception {
    public AccoutException(String message) {
        super(message);
    }

    public AccoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
