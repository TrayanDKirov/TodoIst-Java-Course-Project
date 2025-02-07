package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.CommandParameters;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.TaskNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.Task;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

public class UpdateTaskCommand implements TaskCommand {
    private static final String OUTPUT = "Task successfully updated. ";

    private static final String NAME_OF_TASK_WITH_UPDATED_FIELDS = "taskWithUpdatedFields";

    private CommandParameters parameters;

    private Task taskWithUpdatedFields;

    public static boolean areValidParameters(CommandParameters parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("Null reference in UpdateTaskCommand::areValidParameters. ");
        }

        return parameters.isNameSet();
    }

    private Task getTaskWithUpdatedFields() {
        Task.Builder taskBuilder = new Task.Builder(NAME_OF_TASK_WITH_UPDATED_FIELDS);

        if (parameters.isNewDateSet()) {
            taskBuilder.setDate(parameters.newDate());
        }

        if (parameters.isNewDueDateSet()) {
            taskBuilder.setDueDate(parameters.newDueDate());
        }

        if (parameters.isNewDescriptionSet()) {
            taskBuilder.setDescription(parameters.newDescription());
        }

        return taskBuilder.build();
    }

    public UpdateTaskCommand(CommandParameters parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("Null reference in DeleteTaskCommand constructor. ");
        }

        if (!parameters.isNameSet()) {
            throw new IllegalArgumentException(
                "Parameters in UpdateTaskCommand constructor should have name set. ");
        }

        this.parameters = parameters;
        this.taskWithUpdatedFields = getTaskWithUpdatedFields();
    }

    @Override
    public String execute(TaskCluster taskCluster) throws TaskNotFound {
        if (taskCluster == null) {
            throw new IllegalArgumentException("Null reference in UpdateTaskCommand::execute. ");
        }

        if (!parameters.isDateSet()) {
            taskCluster.updateTask(parameters.name(), taskWithUpdatedFields);
        } else {
            taskCluster.updateTask(parameters.name(), parameters.date(), taskWithUpdatedFields);
        }

        return OUTPUT;
    }
}
