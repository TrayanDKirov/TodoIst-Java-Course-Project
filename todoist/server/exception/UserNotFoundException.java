package bg.sofia.uni.fmi.mjt.project.todoist.server.exception;

public class UserNotFoundException extends AccoutException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
