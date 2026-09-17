package bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration;

public class UserNotInCollaboration extends CollaborationException {
    public UserNotInCollaboration(String message) {
        super(message);
    }

    public UserNotInCollaboration(String message, Throwable cause) {
        super(message, cause);
    }
}
