package bg.sofia.uni.fmi.mjt.project.todoist.server.exception;

public class TaskNotFound extends Exception {
    public TaskNotFound(String message) {
        super(message);
    }

    public TaskNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
