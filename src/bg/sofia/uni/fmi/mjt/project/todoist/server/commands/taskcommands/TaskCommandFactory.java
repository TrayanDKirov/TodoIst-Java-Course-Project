package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.CommandFactory;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.DateFormatIncorrect;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

import java.time.LocalDate;

public class TaskCommandFactory {
    private static final int MAX_LEN_OF_LIST_DASH_BOARD_COMMAND = 1;

    private static final String LIST_DASHBOARD_SYNTAX = "\"list-dashboard\"";
    public static final String WRONG_LIST_DASHBOARD_SYNTAX_MESSAGE =
        "Wrong syntax of list-dashboard command. Right syntaxes: " + System.lineSeparator()
            + LIST_DASHBOARD_SYNTAX;

    private TaskCluster taskCluster;

    public TaskCommandFactory(TaskCluster taskCluster) {
        if (taskCluster == null) {
            throw new IllegalArgumentException("Null reference in TaskCommandFactory constructor. ");
        }

        this.taskCluster = taskCluster;
    }

    private ListTasks createListDashboardCommand(String[] tokens)
        throws WrongCommandSyntax, DateFormatIncorrect {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in CommandFactory::createUpdateTaskCommand. ");
        }

        if (tokens.length > MAX_LEN_OF_LIST_DASH_BOARD_COMMAND) {
            throw new WrongCommandSyntax(WRONG_LIST_DASHBOARD_SYNTAX_MESSAGE);
        }

        return new ListTasks(taskCluster, LocalDate.now());
    }

    public TaskCommand of(String input) throws WrongCommandSyntax, DateFormatIncorrect {
        if (input == null) {
            throw new IllegalArgumentException("Null reference in TaskCommandFactory::of. ");
        }

        String[] tokens = input.split(CommandFactory.MULTIPLE_SPACES_REGEX);
        return switch (tokens[CommandFactory.INDEX_OF_COMMAND_NAME]) {
            case "add-task" -> new AddTask(taskCluster, tokens);
            case "delete-task" -> new DeleteTask(taskCluster, tokens);
            case "get-task" -> new GetTask(taskCluster, tokens);
            case "list-dashboard" -> createListDashboardCommand(tokens);
            case "list-tasks" -> new ListTasks(taskCluster, tokens);
            case "update-task" -> new UpdateTask(taskCluster, tokens);
            case "finish-task" -> new FinishTask(taskCluster, tokens);
            default -> throw new IllegalArgumentException(
                "Only valid command names should be send to TaskCommandFactory. ");
        };
    }
}
