package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.CommandParameter;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.ParameterType;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.task.TaskNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.DateStringFormater;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.Task;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

import java.time.LocalDate;

public class UpdateTask extends TaskCommand {
    private static final String OUTPUT = "Task successfully updated. ";

    private static final String NAME_OF_TASK_WITH_UPDATED_FIELDS = "taskWithUpdatedFields";

    public static final String SYNTAX = "\"update-task name=<name of task> |date=<date of task>| " +
        "|newDate=<new date>| |newDueDate=<new due date>| |newDescription=<new description>|\"";
    public static final String EXPLANATION =
        "to update information about a task from your inbox or from a certain date. ";

    private static final int MAX_LEN_OF_UPDATE_TASK = 6;
    private static final int NAME_INDEX = 1;

    private String name;
    private LocalDate date;
    private Task taskWithUpdatedFields;

    public boolean isDateParameterSet() {
        return date != null;
    }

    private Task getTaskWithUpdatedFields(CommandParameter[] parameters) throws WrongCommandSyntax {
        if (parameters == null) {
            throw new IllegalArgumentException("Null reference in UpdateTask::getTaskWithUpdatedFields. ");
        }
        Task.Builder taskBuilder = new Task.Builder(NAME_OF_TASK_WITH_UPDATED_FIELDS);

        if (parameters[NAME_INDEX - 1].type() != ParameterType.NAME_OF_TASK) {
            throw new WrongCommandSyntax("First param must be \"name=<name of task>\". ");
        }
        this.name = parameters[NAME_INDEX - 1].param();

        for (int i = NAME_INDEX; i < parameters.length; i++) {
            switch (parameters[i].type()) {
                case DATE_OF_TASK -> this.date = DateStringFormater.stringToDate(parameters[i].param());
                case NEW_DATE -> {
                    LocalDate newDate = DateStringFormater.stringToDate(parameters[i].param());
                    taskBuilder.setDate(newDate);
                }
                case NEW_DUE_DATE -> {
                    LocalDate newDueDate = DateStringFormater.stringToDate(parameters[i].param());
                    taskBuilder.setDueDate(newDueDate);
                }
                case NEW_DESCRIPTION -> taskBuilder.setDescription(parameters[i].param());
                default -> throw new WrongCommandSyntax(
                    "Command update-task accepts only name, date, newDate, newDueDate and newDescription parameters. ");
            }
        }

        return taskBuilder.build();
    }

    public UpdateTask(TaskCluster taskCluster, String[] tokens) throws WrongCommandSyntax {
        super(taskCluster);
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in UpdateTask constructor. ");
        }

        if (tokens.length > MAX_LEN_OF_UPDATE_TASK) {
            throw new WrongCommandSyntax("Too many parameters in command. ");
        }

        CommandParameter[] parameters = CommandParameter.parseArrayOfParameters(tokens, NAME_INDEX);

        this.taskWithUpdatedFields = getTaskWithUpdatedFields(parameters);
    }

    @Override
    public String execute() throws TaskNotFound {
        if (!isDateParameterSet()) {
            taskCluster.updateTask(name, taskWithUpdatedFields);
        } else {
            taskCluster.updateTask(name, date, taskWithUpdatedFields);
        }

        return OUTPUT;
    }
}
