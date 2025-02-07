package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.CommandParameters;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.TaskAlreadyExists;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.TaskNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.Task;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

public class AddTaskCommand implements TaskCommand {
    private static final String OUTPUT = "Successfully added task. ";

    private CommandParameters parameters;
    private Task taskToAdd;

    private Task getTaskToAdd() {
        Task.Builder taskBuilder = new Task.Builder(parameters.name());

        if (parameters.isDateSet()) {
            taskBuilder.setDate(parameters.date());
        }

        if (parameters.isNewDueDateSet()) {
            taskBuilder.setDueDate(parameters.newDueDate());
        }

        if (parameters.isNewDescriptionSet()) {
            taskBuilder.setDescription(parameters.newDescription());
        }

        return taskBuilder.build();
    }

    public static boolean areValidParameters(CommandParameters parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("Null reference in DeleteTaskCommand::areValidParameters. ");
        }

        return parameters.isNameSet();
    }

    public AddTaskCommand(CommandParameters parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("Null reference in AddTaskCommand constructor. ");
        }

        if (!parameters.isNameSet()) {
            throw new IllegalArgumentException("Parameters in UpdateTaskCommand constructor should have name set. ");
        }

        this.parameters = parameters;
        this.taskToAdd = getTaskToAdd();
    }

    @Override
    public String execute(TaskCluster taskClusterOfUser) throws TaskAlreadyExists {
        if (taskClusterOfUser == null) {
            throw new IllegalArgumentException("Null reference in AddTaskCommand::execute. ");
        }

        taskClusterOfUser.addTask(taskToAdd);

        return OUTPUT;
    }
}
