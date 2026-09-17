package bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account;

public class UsernameIsTaken extends AccountException {
    public UsernameIsTaken(String message) {
        super(message);
    }

    public UsernameIsTaken(String message, Throwable cause) {
        super(message, cause);
    }
}
