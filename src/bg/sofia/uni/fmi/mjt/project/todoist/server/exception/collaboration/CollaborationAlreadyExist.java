package bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration;

public class CollaborationAlreadyExist extends CollaborationException {
    public CollaborationAlreadyExist(String message) {
        super(message);
    }

    public CollaborationAlreadyExist(String message, Throwable cause) {
        super(message, cause);
    }
}
