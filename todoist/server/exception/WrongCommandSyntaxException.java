package bg.sofia.uni.fmi.mjt.project.todoist.server.exception;

public class WrongCommandSyntaxException extends AccoutException {
    public WrongCommandSyntaxException(String message) {
        super(message);
    }

    public WrongCommandSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }
}
