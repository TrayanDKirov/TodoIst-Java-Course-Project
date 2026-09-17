package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.colaborationcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;

public class ListCollaborations extends CollaborationCommand {

    public static final String SYNTAX = "\"list-collaborations\"";
    public static final String EXPLANATION = "to list all collaborations you are a part of. ";

    private static final int LEN_OF_LIST_COLLABORATIONS = 1;

    public ListCollaborations(User user, String passwordToUser, TodoIstRepository repository,
                              String[] tokens) throws WrongCommandSyntax {
        super(user, passwordToUser, repository);

        if (tokens.length != LEN_OF_LIST_COLLABORATIONS) {
            throw new WrongCommandSyntax("Command list-collaborations accepts no parameters. ");
        }
    }

    @Override
    public String execute() {
        String[] collaborations = user.getCollaborations().toArray(new String[0]);

        return "The collaborations you are a part of are:" + String.join(", ", collaborations) + ". ";
    }
}
