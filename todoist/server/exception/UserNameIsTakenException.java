package bg.sofia.uni.fmi.mjt.project.todoist.server.exception;

public class UserNameIsTakenException extends AccoutException {
    public UserNameIsTakenException(String message) {
        super(message);
    }

    public UserNameIsTakenException(String message, Throwable cause) {
        super(message, cause);
    }
}
