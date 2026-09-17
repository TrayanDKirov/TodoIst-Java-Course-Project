package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.colaborationcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.collaboration.Collaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.CommandParameter;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.ParameterType;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.UserNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.WrongPassword;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.CollaborationNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.UserNotInCollaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.task.TaskNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.Task;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;

public class AssignTask extends CollaborationCommand {
    private static final String OUTPUT = "Successfully assigned task. ";

    public static final String SYNTAX = "\"assign-task collaboration=<name> user=<username> task=<name>\"";
    public static final String EXPLANATION = "to assign a task in a collaboration to a specific user. ";

    private static final int LEN_OF_ASSIGN_TASK = 4;
    private static final int COLLABORATION_NAME_INDEX = 1;
    private static final int USER_NAME_INDEX = 2;
    private static final int TASK_NAME_INDEX = 3;

    private String collaborationName;
    private String assigneeUsername;
    private String taskName;

    private void parseParameters(String[] tokens) throws WrongCommandSyntax {
        if (tokens.length != LEN_OF_ASSIGN_TASK) {
            throw new WrongCommandSyntax("Command assign-task accepts exactly three parameters: " +
                "collaboration=<name> user=<username> task=<name>. ");
        }

        CommandParameter currParam = CommandParameter.parseParameter(tokens[COLLABORATION_NAME_INDEX]);
        if (currParam.type() != ParameterType.COLLABORATION_NAME) {
            throw new WrongCommandSyntax("First parameter must be \"collaboration=<name>\". ");
        }
        this.collaborationName = currParam.param();

        currParam = CommandParameter.parseParameter(tokens[USER_NAME_INDEX]);
        if (currParam.type() != ParameterType.USERNAME) {
            throw new WrongCommandSyntax("Second parameter must be \"user=<username>\". ");
        }
        this.assigneeUsername = currParam.param();

        currParam = CommandParameter.parseParameter(tokens[TASK_NAME_INDEX]);
        if (currParam.type() != ParameterType.TASK_NAME) {
            throw new WrongCommandSyntax("Third parameter must be \"task=<name>\". ");
        }
        this.taskName = currParam.param();
    }

    public AssignTask(User user, String passwordOfUser, TodoIstRepository repository,
                      String[] tokens) throws WrongCommandSyntax {
        super(user, passwordOfUser, repository);

        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in AssignTask constructor. ");
        }

        parseParameters(tokens);
    }

    @Override
    public String execute() throws CollaborationNotFound, UserNotInCollaboration, UserNotFound, WrongPassword,
        TaskNotFound {
        Collaboration collaboration = repository.getCollaboration(
            user.getUsername(), passwordOfUser, collaborationName);

        if (!collaboration.isUserParticipant(assigneeUsername)) {
            throw new UserNotInCollaboration(
                "User " + assigneeUsername + " is not a participant in this collaboration. ");
        }

        Task task = collaboration.getTasks().getTask(taskName);
        task.setAssignee(assigneeUsername);

        return OUTPUT;
    }
}