package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.colaborationcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.CommandParameter;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.ParameterType;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;

public abstract class CollaborationNameCommand extends CollaborationCommand {
    private static final int LEN_OF_NAME_COLLABORATION = 2;

    private static final int COLLABORATION_NAME_INDEX = 1;

    protected String collaborationName;

    private void parseParameters(String[] tokens) throws WrongCommandSyntax {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in CollaborationNameCommand::parseParameters. ");
        }

        if (tokens.length != LEN_OF_NAME_COLLABORATION) {
            throw new WrongCommandSyntax("Command accepts 1 parameter. ");
        }

        CommandParameter parameter = CommandParameter.parseParameter(tokens[COLLABORATION_NAME_INDEX]);
        if (parameter.type() != ParameterType.COLLABORATION_NAME) {
            throw new WrongCommandSyntax("Command accepts only 1 parameter - \"collaboration=<name of collaboration>\"");
        }
        this.collaborationName = parameter.param();
    }

    public CollaborationNameCommand(User user, String passwordOfUser, TodoIstRepository repository,
                                    String[] tokens) throws WrongCommandSyntax {
        super(user, passwordOfUser, repository);

        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in CollaborationNameCommand constructor. ");
        }

        parseParameters(tokens);
    }
}
