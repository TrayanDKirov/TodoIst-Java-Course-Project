package bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.CommandException;

public class CollaborationException extends CommandException {
    public CollaborationException(String message) {
        super(message);
    }

    public CollaborationException(String message, Throwable cause) {
        super(message, cause);
    }
}
