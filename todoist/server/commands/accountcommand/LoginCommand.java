package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.accountcommand;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.UserNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongPassword;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;

public class LoginCommand implements AccountCommand {
    private static final String OUTPUT_MESSAGE = "Successfully logged in, ";
    private static final String EXCEPTION_MESSAGE = "Could not log in. Wrong password given for ";

    private String username;
    private String password;

    public LoginCommand(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Null reference in RegisterCommand constructor. ");
        }

        this.username = username;
        this.password = password;
    }

    private String getOutputMessage() {
        return OUTPUT_MESSAGE + username + ". ";
    }

    private String getExceptionMessage() {
        return EXCEPTION_MESSAGE + username + ". ";
    }

    @Override
    public AccountCommandResult execute(TodoIstRepository repository)
        throws UserNotFound, WrongPassword {
        if (repository == null) {
            throw new IllegalArgumentException("Null reference in LoginCommand::execute");
        }

        User userToReturn = repository.getUser(username);
        if (!userToReturn.isPasswordCorrect(password)) {
            throw new WrongPassword(getExceptionMessage());
        }

        return new AccountCommandResult.Builder(getOutputMessage())
            .setUserAndPassword(userToReturn, password)
            .build();
    }
}
