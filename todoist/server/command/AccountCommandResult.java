package bg.sofia.uni.fmi.mjt.project.todoist.server.command;

import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;

public record AccountCommandResult(User user, String passwordOfUser, String output) {
    public AccountCommandResult {
        if (user == null || passwordOfUser == null || output == null) {
            throw new IllegalArgumentException("Null reference in result constructor. ");
        }
    }
}
