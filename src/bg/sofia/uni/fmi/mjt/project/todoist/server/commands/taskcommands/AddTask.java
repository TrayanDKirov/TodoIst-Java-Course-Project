package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.CommandParameter;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.ParameterType;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.task.TaskAlreadyExists;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.DateStringFormater;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.Task;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.Task.Builder;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

public class AddTask extends TaskCommand {
    private static final String OUTPUT = "Successfully added task. ";

    public static final String SYNTAX = "\"add-task name=<name of task> " +
        "|date=<date>| |dueDate=<due date>| |description=<description>|\"";
    public static final String EXPLANATION = "to add task to your inbox or to certain date. ";

    private static final int MAX_LEN_OF_ADD_TASK_COMMAND = 5;
    private static final int NAME_INDEX = 1;

    private Task taskToAdd;

    private static void parseOptionalParams(String[] tokens, Builder taskBuilder) throws WrongCommandSyntax {
        if (tokens == null || taskBuilder == null) {
            throw new IllegalArgumentException("Null reference in AddTask::parseOptionalParams. ");
        }

        for (int i = NAME_INDEX + 1; i < tokens.length; i++) {
            CommandParameter currParam = CommandParameter.parseParameter(tokens[i]);

            switch (currParam.type()) {
                case DATE_OF_TASK ->
                    taskBuilder.setDate(DateStringFormater.stringToDate(currParam.param()));

                case DUE_DATE_OF_TASK ->
                    taskBuilder.setDueDate(DateStringFormater.stringToDate(currParam.param()));

                case DESCRIPTION_OF_TASK ->
                    taskBuilder.setDescription(currParam.param());

                default -> throw new WrongCommandSyntax(
                    "Command add-task accepts only name, date, dueDate and description parameters. ");
            }
        }
    }

    private static Task parseParametersIntoTask(String[] tokens) throws WrongCommandSyntax {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in AddTask::parseParametersIntoTask");
        }

        CommandParameter currParam = CommandParameter.parseParameter(tokens[NAME_INDEX]);
        if (currParam.type() != ParameterType.NAME_OF_TASK) {
            throw new WrongCommandSyntax("First param in add-task command should be name=<nameOfTask>. ");
        }

        Task.Builder taskBuilder = new Task.Builder(currParam.param());
        parseOptionalParams(tokens, taskBuilder);

        return taskBuilder.build();
    }

    public AddTask(TaskCluster taskCluster, String[] tokens) throws WrongCommandSyntax {
        super(taskCluster);

        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in AddTask constructor. ");
        }

        this.taskToAdd = parseParametersIntoTask(tokens);

        if (tokens.length > MAX_LEN_OF_ADD_TASK_COMMAND) {
            throw new WrongCommandSyntax("Too many param. Command add-task takes at most "
                + (MAX_LEN_OF_ADD_TASK_COMMAND - 1) + ". ");
        }
    }

    @Override
    public String execute() throws TaskAlreadyExists {
        taskCluster.addTask(taskToAdd);

        return OUTPUT;
    }
}
