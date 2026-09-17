package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.colaborationcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.collaboration.Collaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.UserNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.WrongPassword;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.CollaborationNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.UserNotInCollaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;

import java.util.Set;

public class ListUsers extends CollaborationNameCommand {
    private static final String OUTPUT = "Participants in this collaboration are: ";

    public static final String SYNTAX = "\"list-users collaboration=<name>\"";
    public static final String EXPLANATION = "to list all participants in a collaboration. ";

    public ListUsers(User user, String passwordOfUser, TodoIstRepository repository,
                     String[] tokens) throws WrongCommandSyntax {
        super(user, passwordOfUser, repository, tokens);
    }

    @Override
    public String execute() throws CollaborationNotFound, UserNotInCollaboration, UserNotFound, WrongPassword {
        Collaboration collaboration = repository
            .getCollaboration(user.getUsername(), passwordOfUser, collaborationName);

        Set<String> participants = collaboration.getParticipants();
        String[] participantsArray = participants.toArray(new String[0]);

        return OUTPUT + String.join(", ", participantsArray) + ". ";
    }
}
