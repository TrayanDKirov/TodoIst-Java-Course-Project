package bg.sofia.uni.fmi.mjt.project.todoist.server.exception;

public class AccountException extends CommandException {
    public AccountException(String message) {
        super(message);
    }

    public AccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
