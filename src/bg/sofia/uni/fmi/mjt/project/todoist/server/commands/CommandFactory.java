package bg.sofia.uni.fmi.mjt.project.todoist.server.commands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.collaboration.Collaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.accountcommand.WrongCommand;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.colaborationcommands.CollaborationCommandFactory;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.CommandParameter;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.ParameterType;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands.TaskCommand;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands.TaskCommandFactory;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.AccountException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.DateFormatIncorrect;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.CollaborationException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;

public class CommandFactory {
    public static final int INDEX_OF_COMMAND_NAME = 0;
    public static final int INDEX_OF_FIRST_PARAMETER = 1;

    public static final String MULTIPLE_SPACES_REGEX = "\\s+";

    private User user;
    private String passwordOfUser;
    private TodoIstRepository repository;

    public CommandFactory(User user, String passwordOfUser, TodoIstRepository repository) {
        if (user == null || passwordOfUser == null || repository == null) {
            throw new IllegalArgumentException("Null reference in CommandFactory constructor. ");
        }

        this.user = user;
        this.passwordOfUser = passwordOfUser;
        this.repository = repository;
    }

    private static String getInputWithoutFirstParam(String[] tokens) {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in CommandFactory::getInputWithoutFirstParam. ");
        }

        String[] tokensWithoutFirstParam = new String[tokens.length - 1];

        for (int i = 0, j = 0; i < tokens.length; i++) {
            if (i != INDEX_OF_FIRST_PARAMETER) {
                tokensWithoutFirstParam[j++] = tokens[i];
            }
        }

        return String.join(" ", tokensWithoutFirstParam);
    }

    private TaskCommand createTaskCommand(String[] tokens)
        throws WrongCommandSyntax, DateFormatIncorrect, CollaborationException, AccountException {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in CommandFactory::createTaskCommand. ");
        }
        String input;

        TaskCommandFactory factory;
        if (tokens.length <= 1) {
            factory = new TaskCommandFactory(user.getTasks(passwordOfUser));

            input = String.join(" ", tokens);
            return factory.of(input);
        }

        CommandParameter firstParameter = CommandParameter.parseParameter(tokens[INDEX_OF_FIRST_PARAMETER]);
        if (firstParameter.type() == ParameterType.COLLABORATION_NAME) {

            Collaboration collaboration = repository.getCollaboration(
                user.getUsername(), passwordOfUser, firstParameter.param());

            factory = new TaskCommandFactory(collaboration.getTasks());

            input = getInputWithoutFirstParam(tokens);
        } else {
            factory = new TaskCommandFactory(user.getTasks(passwordOfUser));

            input = String.join(" ", tokens);
        }

        return factory.of(input);
    }

    private Command createCollaboartionCommand(String input) throws AccountException, DateFormatIncorrect {
        if (input == null) {
            throw new IllegalArgumentException("Null reference in CommandFactory::createCollaboartionCommand. ");
        }

        CollaborationCommandFactory factory =
            new CollaborationCommandFactory(user, passwordOfUser, repository);

        return factory.of(input);
    }

    public Command of(String input) throws AccountException, CollaborationException, DateFormatIncorrect {
        if (input == null) {
            throw new IllegalArgumentException("Null reference in CommandFactory::of. ");
        }

        String[] tokens = input.split(MULTIPLE_SPACES_REGEX);
        return switch (tokens[INDEX_OF_COMMAND_NAME]) {
            case "get-task", "delete-task", "update-task", "finish-task",
                 "list-dashboard", "list-tasks", "add-task" ->
                createTaskCommand(tokens);

            case "add-collaboration", "delete-collaboration", "list-collaborations",
                 "add-user", "list-users", "assign-task" ->
                createCollaboartionCommand(input);

            case "help" -> new HelpCommand(tokens);

            default -> new WrongCommand(tokens[INDEX_OF_COMMAND_NAME]);
        };
    }
}
