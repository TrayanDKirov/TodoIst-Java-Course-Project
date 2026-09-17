package bg.sofia.uni.fmi.mjt.project.todoist.server.exception;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.AccountException;

public class WrongCommandSyntax extends AccountException {
    public WrongCommandSyntax(String message) {
        super(message);
    }

    public WrongCommandSyntax(String message, Throwable cause) {
        super(message, cause);
    }
}
