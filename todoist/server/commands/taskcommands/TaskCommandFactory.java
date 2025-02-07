package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.CommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.accountcommand.WrongCommand;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.CommandParameter;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.CommandParameters;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.DateFormatIncorrect;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskUtils;

import java.time.LocalDate;

public class TaskCommandFactory {
    private static final int INDEX_OF_COMMAND_NAME = 0;
    private static final int INDEX_OF_TASK_NAME = 1;
    private static final int INDEX_OF_DATE_NAME = 2;

    private static final int MAX_LEN_OF_DELETE_COMMAND = 3;
    private static final int MAX_LEN_OF_GET_TASK_COMMAND = 3;
    private static final int MAX_LEN_OF_FINISH_TASK_COMMAND = 3;
    private static final int MAX_LEN_OF_UPDATE_TASK_COMMAND = 6;
    private static final int MAX_LEN_OF_LIST_TASKS_COMMAND = 2;
    private static final int MAX_LEN_OF_LIST_DASH_BOARD_COMMAND = 1;
    private static final int MAX_LEN_OF_ADD_TASK_COMMAND = 5;

    private static  CommandParameter[] parseCommandParameters(String[] tokens, int maxParams)
        throws WrongCommandSyntax {
        if (tokens == null) {
            throw new IllegalArgumentException(
                "Null reference in CommandFactory::parseCommandParameters. ");
        }

        if (tokens.length > maxParams || tokens.length == 0) {
            throw new WrongCommandSyntax("Too many parameters for command. ");
        }

        CommandParameter[] parameters = new CommandParameter[tokens.length - 1];

        for (int i = INDEX_OF_COMMAND_NAME + 1, curr = 0; i < tokens.length; i++, curr++) {
            parameters[curr] = CommandParameter.parseParameter(tokens[i]);
        }

        return parameters;
    }

    private static  CommandParameters getCommandParameters(String[] tokens, int maxParams)
        throws WrongCommandSyntax, DateFormatIncorrect {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in CommandFactory::getCommandParameters. ");
        }

        CommandParameter[] parameters = parseCommandParameters(tokens, maxParams);

        CommandParameters.Builder resultBuilder = new CommandParameters.Builder();
        for (CommandParameter currParameter : parameters) {
            switch (currParameter.type()) {
                case NAME_OF_TASK -> resultBuilder.setName(currParameter.parameter());
                case DATE_OF_TASK -> resultBuilder.setDateOfTask(TaskUtils.fromStringToDate(currParameter.parameter()));
                case NEW_DATE -> resultBuilder.setNewDate(TaskUtils.fromStringToDate(currParameter.parameter()));
                case NEW_DUE_DATE -> resultBuilder.setNewDueDate(TaskUtils.fromStringToDate(currParameter.parameter()));
                case NEW_DESCRIPTION -> resultBuilder.setNewDescription(currParameter.parameter());
            }
        }

        return resultBuilder.build();
    }

    private static GetTaskCommand createGetTaskCommand(String[] tokens) throws WrongCommandSyntax, DateFormatIncorrect {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in CommandFactory::createGetTaskCommand. ");
        }

        try {
            CommandParameters parameters = getCommandParameters(tokens, MAX_LEN_OF_DELETE_COMMAND);
            if (!GetTaskCommand.areValidParameters(parameters)) {
                throw new WrongCommandSyntax("Parameters are not valid. ");
            }

            return new GetTaskCommand(parameters);
        } catch (WrongCommandSyntax ex) {
            throw new WrongCommandSyntax(CommandSyntax.WRONG_GET_TASK_SYNTAX_MESSAGE);
        }
    }

    private static DeleteTaskCommand createDeleteTaskCommand(String[] tokens)
        throws WrongCommandSyntax, DateFormatIncorrect {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in CommandFactory::createDeleteTaskCommand. ");
        }

        try {
            CommandParameters parameters = getCommandParameters(tokens, MAX_LEN_OF_DELETE_COMMAND);
            if (!DeleteTaskCommand.areValidParameters(parameters)) {
                throw new WrongCommandSyntax("Parameters are not valid. ");
            }

            return new DeleteTaskCommand(parameters);
        } catch (WrongCommandSyntax ex) {
            throw new WrongCommandSyntax(CommandSyntax.WRONG_DELETE_TASK_SYNTAX_MESSAGE);
        }
    }

    private static UpdateTaskCommand createUpdateTaskCommand(String[] tokens)
        throws WrongCommandSyntax, DateFormatIncorrect {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in CommandFactory::createUpdateTaskCommand. ");
        }

        try {
            CommandParameters parameters = getCommandParameters(tokens, MAX_LEN_OF_DELETE_COMMAND);
            if (!UpdateTaskCommand.areValidParameters(parameters)) {
                throw new WrongCommandSyntax("Parameters are not valid. ");
            }

            return new UpdateTaskCommand(parameters);
        } catch (WrongCommandSyntax ex) {
            throw new WrongCommandSyntax(CommandSyntax.WRONG_UPDATE_TASK_SYNTAX_MESSAGE);
        }
    }

    private static FinishTaskCommand createFinishTaskCommand(String[] tokens)
        throws WrongCommandSyntax, DateFormatIncorrect {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in CommandFactory::createFinishTaskCommand. ");
        }

        try {
            CommandParameters parameters = getCommandParameters(tokens, MAX_LEN_OF_DELETE_COMMAND);
            if (!FinishTaskCommand.areValidParameters(parameters)) {
                throw new WrongCommandSyntax("Parameters are not valid. ");
            }

            return new FinishTaskCommand(parameters);
        } catch (WrongCommandSyntax ex) {
            throw new WrongCommandSyntax(CommandSyntax.WRONG_FINISH_TASK_SYNTAX_MESSAGE);
        }
    }

    private static ListTasksCommand createListTasksCommand(String[] tokens)
        throws WrongCommandSyntax, DateFormatIncorrect {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in CommandFactory::createListTasksCommand. ");
        }

        try {
            CommandParameters parameters = getCommandParameters(tokens, MAX_LEN_OF_DELETE_COMMAND);
            if (!ListTasksCommand.areValidParameters(parameters)) {
                throw new WrongCommandSyntax("Parameters are not valid. ");
            }

            return new ListTasksCommand(parameters);
        } catch (WrongCommandSyntax ex) {
            throw new WrongCommandSyntax(CommandSyntax.WRONG_LIST_TASK_SYNTAX_MESSAGE);
        }
    }

    private static ListTasksCommand createListDashboardCommand(String[] tokens)
        throws WrongCommandSyntax, DateFormatIncorrect {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in CommandFactory::createUpdateTaskCommand. ");
        }

        if (tokens.length > MAX_LEN_OF_LIST_DASH_BOARD_COMMAND) {
            throw new WrongCommandSyntax(CommandSyntax.WRONG_LIST_DASHBOARD_SYNTAX_MESSAGE);
        }

        return new ListTasksCommand(new CommandParameters
            .Builder()
            .setDateOfTask(LocalDate.now())
            .build());
    }

    private static AddTaskCommand createAddTaskCommand(String[] tokens)
        throws WrongCommandSyntax, DateFormatIncorrect {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in CommandFactory::createListTasksCommand. ");
        }

        return new AddTaskCommand(getCommandParameters(tokens, MAX_LEN_OF_ADD_TASK_COMMAND));
    }

    public static TaskCommand of(String input) throws WrongCommandSyntax, DateFormatIncorrect {
        if (input == null) {
            throw new IllegalArgumentException("Null reference in CommandFactory::of. ");
        }

        String[] tokens = input.split(" ");
        return switch (tokens[INDEX_OF_COMMAND_NAME]) {
            case "get-task" -> createGetTaskCommand(tokens);
            case "delete-task" -> createDeleteTaskCommand(tokens);
            case "update-task" -> createUpdateTaskCommand(tokens);
            case "finish-task" -> createFinishTaskCommand(tokens);
            case "list-dashboard" -> createListDashboardCommand(tokens);
            case "list-tasks" -> createListTasksCommand(tokens);
            case "add-task" -> createAddTaskCommand(tokens);
            default -> new WrongCommand(tokens[INDEX_OF_COMMAND_NAME]);
        };
    }
}
