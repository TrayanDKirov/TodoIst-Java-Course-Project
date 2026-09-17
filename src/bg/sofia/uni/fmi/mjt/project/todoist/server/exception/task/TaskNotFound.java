package bg.sofia.uni.fmi.mjt.project.todoist.server.exception.task;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.CommandException;

public class TaskNotFound extends CommandException {
    public TaskNotFound(String message) {
        super(message);
    }

    public TaskNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
