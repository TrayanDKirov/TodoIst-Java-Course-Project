package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.CommandParameters;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.TaskNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.Task;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

public class FinishTaskCommand implements TaskCommand {
    private static final String OUTPUT = "Task successfully finished. ";

    private CommandParameters parameters;

    public static boolean areValidParameters(CommandParameters parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("Null reference in FinishTaskCommand::areValidParameters. ");
        }

        return parameters.isNameSet() && !parameters.isNewDateSet()
            && !parameters.isNewDueDateSet() && !parameters.isNewDescriptionSet();
    }

    public FinishTaskCommand(CommandParameters parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("Null reference in FinishTaskCommand constructor. ");
        }

        if (!parameters.isNameSet()) {
            throw new IllegalArgumentException("Parameters in FinishTaskCommand constructor should be set. ");
        }

        this.parameters = parameters;
    }

    @Override
    public String execute(TaskCluster taskCluster) throws TaskNotFound {
        if (taskCluster == null) {
            throw new IllegalArgumentException("Null reference in FinishTaskCommand::execute. ");
        }

        Task task;
        if (!parameters.isDateSet()) {
            task = taskCluster.getTask(parameters.name());
        } else {
            task = taskCluster.getTask(parameters.name(), parameters.date());
        }

        task.complete();

        return OUTPUT;
    }
}
