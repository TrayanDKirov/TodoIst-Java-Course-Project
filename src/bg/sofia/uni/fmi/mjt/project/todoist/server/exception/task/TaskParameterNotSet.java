package bg.sofia.uni.fmi.mjt.project.todoist.server.exception.task;

public class TaskParameterNotSet extends RuntimeException {
    public TaskParameterNotSet(String message) {
        super(message);
    }

    public TaskParameterNotSet(String message, Throwable cause) {
        super(message, cause);
    }
}
