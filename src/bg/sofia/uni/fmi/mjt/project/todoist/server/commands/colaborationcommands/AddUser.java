package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.colaborationcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.collaboration.Collaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.CommandParameter;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.ParameterType;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.UserNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.WrongPassword;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.CollaborationNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.UserIsAlreadyInTheCollaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.UserNotInCollaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;

public class AddUser extends CollaborationCommand {
    private static final String OUTPUT = "User successfully added to collaboration. ";

    public static final String SYNTAX = "\"add-user collaboration=<name> user=<username>\"";
    public static final String EXPLANATION = "to add a user to a collaboration (only the creator can do this). ";

    private static final int LEN_OF_ADD_USER_COMMAND = 3;
    private static final int COLLABORATION_NAME_INDEX = 1;
    private static final int USER_NAME_INDEX = 2;

    private String collaborationName;
    private String username;

    private void parseParameters(String[] tokens) throws WrongCommandSyntax {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in AddUser::parseParameters. ");
        }

        if (tokens.length != LEN_OF_ADD_USER_COMMAND) {
            throw new WrongCommandSyntax("Command add-user accepts two parameters. ");
        }

        CommandParameter currParameter = CommandParameter.parseParameter(tokens[COLLABORATION_NAME_INDEX]);
        if (currParameter.type() != ParameterType.COLLABORATION_NAME) {
            throw new WrongCommandSyntax(
                "Command add-user accepts first parameter - \"collaboration=<name of collaboration>\"");
        }
        this.collaborationName = currParameter.param();

        currParameter = CommandParameter.parseParameter(tokens[USER_NAME_INDEX]);
        if (currParameter.type() != ParameterType.USERNAME) {
            throw new WrongCommandSyntax(
                "Command add-user accepts second parameter - \"user=<name of user to add>\"");
        }
        this.username = currParameter.param();
    }

    public AddUser(User user, String passwordOfUser, TodoIstRepository repository,
                   String[] tokens) throws WrongCommandSyntax {
        super(user, passwordOfUser, repository);

        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in AddUser constructor. ");
        }

        parseParameters(tokens);
    }

    @Override
    public String execute() throws CollaborationNotFound, UserNotInCollaboration,
        UserIsAlreadyInTheCollaboration, UserNotFound, WrongPassword {

        Collaboration collaborationToAdd = repository
            .getCollaboration(user.getUsername(), passwordOfUser, collaborationName);

        collaborationToAdd.addUser(username);
        User userToAdd = repository.getUser(username);
        userToAdd.addCollaboration(collaborationName);

        return OUTPUT;
    }
}
