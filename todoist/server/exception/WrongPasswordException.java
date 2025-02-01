package bg.sofia.uni.fmi.mjt.project.todoist.server.exception;

public class WrongPasswordException extends AccoutException {
    public WrongPasswordException(String message) {
        super(message);
    }

    public WrongPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
