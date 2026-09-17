package bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.CommandException;

public class AccountException extends CommandException {
    public AccountException(String message) {
        super(message);
    }

    public AccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
