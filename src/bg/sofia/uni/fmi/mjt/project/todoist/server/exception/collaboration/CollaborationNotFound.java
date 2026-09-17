package bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration;

public class CollaborationNotFound extends CollaborationException {
    public CollaborationNotFound(String message) {
        super(message);
    }

    public CollaborationNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
