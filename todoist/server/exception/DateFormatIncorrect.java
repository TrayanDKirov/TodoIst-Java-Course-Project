package bg.sofia.uni.fmi.mjt.project.todoist.server.exception;

public class DateFormatIncorrect extends CommandException {
    public DateFormatIncorrect(String message) {
        super(message);
    }

    public DateFormatIncorrect(String message, Throwable cause) {
        super(message, cause);
    }
}
