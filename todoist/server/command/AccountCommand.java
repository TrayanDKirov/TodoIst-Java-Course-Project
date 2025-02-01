package bg.sofia.uni.fmi.mjt.project.todoist.server.command;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.AccoutException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;

public interface AccountCommand {
    AccountCommandResult execute(TodoIstRepository repository) throws AccoutException;

    static AccountCommand of(String input) {
        String[] tokens = input.split(" ");

        return switch (tokens[0]) {
            case "login" -> new LoginCommand(tokens[1], tokens[2]);
            case "register" -> new RegisterCommand(tokens[1], tokens[2]);
            default -> null;
        };
    }
}
