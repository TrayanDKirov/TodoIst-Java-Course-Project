package bg.sofia.uni.fmi.mjt.project.todoist.server.command.accountcommand;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.UserNameIsTakenException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.UserImpl;

public class RegisterCommand implements AccountCommand {
    private static final String OUTPUT_MESSAGE = "Account successfully created, ";

    private String username;
    private String password;

    public RegisterCommand(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Null reference in RegisterCommand constructor. ");
        }

        this.username = username;
        this.password = password;
    }

    private String getOutputMessage() {
        return OUTPUT_MESSAGE + username + ". ";
    }

    @Override
    public AccountCommandResult execute(TodoIstRepository repository) throws UserNameIsTakenException {
        if (repository == null) {
            throw new IllegalArgumentException("Null reference in RegisterCommand::execute");
        }

        User newUser = new UserImpl(username, password);

        repository.addUser(newUser);

        return new AccountCommandResult.Builder(getOutputMessage())
            .setUserAndPassword(newUser, password)
            .build();
    }
}
