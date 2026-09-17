package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.colaborationcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.UserNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.WrongPassword;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.CollaborationCreatorException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.CollaborationNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;

public class DeleteCollaboration extends CollaborationNameCommand {
    private static final String OUTPUT = "Collaboration successfully deleted. ";

    public static final String SYNTAX = "\"delete-collaboration collaboration=<name>\"";
    public static final String EXPLANATION = "to delete a collaboration (only the creator can do this). ";

    public DeleteCollaboration(User user, String passwordOfUser, TodoIstRepository repository,
                               String[] tokens) throws WrongCommandSyntax {
        super(user, passwordOfUser, repository, tokens);
    }

    @Override
    public String execute() throws CollaborationNotFound,
        CollaborationCreatorException, UserNotFound, WrongPassword {
        repository.deleteCollaboration(user.getUsername(), passwordOfUser, collaborationName);

        return OUTPUT;
    }
}
