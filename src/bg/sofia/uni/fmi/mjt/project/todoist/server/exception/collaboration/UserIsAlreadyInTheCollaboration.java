package bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration;

public class UserIsAlreadyInTheCollaboration extends CollaborationException {
    public UserIsAlreadyInTheCollaboration(String message) {
        super(message);
    }

    public UserIsAlreadyInTheCollaboration(String message, Throwable cause) {
        super(message, cause);
    }
}
