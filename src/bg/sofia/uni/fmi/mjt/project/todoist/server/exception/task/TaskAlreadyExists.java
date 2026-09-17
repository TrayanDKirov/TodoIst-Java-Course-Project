package bg.sofia.uni.fmi.mjt.project.todoist.server.exception.task;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.CommandException;

public class TaskAlreadyExists extends CommandException {
    public TaskAlreadyExists(String message) {
        super(message);
    }

    public TaskAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }
}
