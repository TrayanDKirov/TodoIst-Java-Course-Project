package bg.sofia.uni.fmi.mjt.project.todoist.server.exception;

public class CommandParameterNotSet extends RuntimeException {
    public CommandParameterNotSet(String message) {
        super(message);
    }

    public CommandParameterNotSet(String message, Throwable cause) {
        super(message, cause);
    }
}
