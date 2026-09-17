package bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account;

public class UserNotFound extends AccountException {
    public UserNotFound(String message) {
        super(message);
    }

    public UserNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
