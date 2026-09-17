package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.colaborationcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.CommandFactory;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.DateFormatIncorrect;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;

public class CollaborationCommandFactory {
    private static final int LEN_OF_ADD_COLLABORATION = 2;
    private static final int LEN_OF_DELETE_COLLABORATION = 2;
    private static final int LEN_OF_ASSIGN_TASK = 4;
    private static final int LEN_OF_ADD_USER = 3;
    private static final int LEN_OF_LIST_USERS = 2;

    private User user;
    private String passwordOfUser;
    private TodoIstRepository repository;

    public CollaborationCommandFactory(User user, String passwordOfUser, TodoIstRepository repository) {
        if (user == null || passwordOfUser == null || repository == null) {
            throw new IllegalArgumentException("Null reference in CollaborationCommandFactory constructor. ");
        }

        this.user = user;
        this.passwordOfUser = passwordOfUser;
        this.repository = repository;
    }

    public CollaborationCommand of(String input) throws WrongCommandSyntax, DateFormatIncorrect {
        if (input == null) {
            throw new IllegalArgumentException("Null reference in CollaborationCommandFactory::of. ");
        }

        String[] tokens = input.split(CommandFactory.MULTIPLE_SPACES_REGEX);
        return switch (tokens[CommandFactory.INDEX_OF_COMMAND_NAME]) {
            case "add-collaboration" -> new AddCollaboration(user, passwordOfUser, repository, tokens);
            case "add-user" -> new AddUser(user, passwordOfUser, repository,  tokens);
            case "assign-task" -> new AssignTask(user, passwordOfUser, repository, tokens);
            case "delete-collaboration" -> new DeleteCollaboration(user, passwordOfUser, repository, tokens);
            case "list-collaborations" -> new ListCollaborations(user, passwordOfUser, repository, tokens);
            case "list-users" -> new ListUsers(user, passwordOfUser, repository, tokens);
            default -> throw new IllegalArgumentException(
                "Only valid command names should be send to CollaborationCommandFactory. ");
        };
    }
}
