package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.colaborationcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.UserNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.WrongPassword;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.CollaborationAlreadyExist;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;

public class AddCollaboration extends CollaborationNameCommand {
    private static final String OUTPUT = "Collaboration added successfully. ";

    public static final String SYNTAX = "\"add-collaboration collaboration=<name>\"";
    public static final String EXPLANATION = "to create a new collaboration. ";

    public AddCollaboration(User user, String passwordOfUser, TodoIstRepository repository, String[] tokens)
        throws WrongCommandSyntax {
        super(user, passwordOfUser, repository, tokens);
    }

    @Override
    public String execute() throws CollaborationAlreadyExist, UserNotFound, WrongPassword {
        repository.addCollaboration(user.getUsername(), passwordOfUser, collaborationName);

        return OUTPUT;
    }
}
