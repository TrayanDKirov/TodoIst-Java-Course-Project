package bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account;

public class WrongPassword extends AccountException {
    public WrongPassword(String message) {
        super(message);
    }

    public WrongPassword(String message, Throwable cause) {
        super(message, cause);
    }
}
