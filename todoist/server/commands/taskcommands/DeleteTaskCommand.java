package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.CommandParameters;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.TaskNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

public class DeleteTaskCommand implements TaskCommand {
    private static final String OUTPUT = "Task successfully deleted. ";

    private CommandParameters parameters;

    public static boolean areValidParameters(CommandParameters parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("Null reference in DeleteTaskCommand::areValidParameters. ");
        }

        return parameters.isNameSet() && !parameters.isNewDateSet()
            && !parameters.isNewDueDateSet() && !parameters.isNewDescriptionSet();
    }

    public DeleteTaskCommand(CommandParameters parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("Null reference in DeleteTaskCommand constructor. ");
        }

        if (!parameters.isNameSet()) {
            throw new IllegalArgumentException("Parameters in DeleteTaskCommand constructor should be set. ");
        }

        this.parameters = parameters;
    }

    @Override
    public String execute(TaskCluster taskClusterOfUser) throws TaskNotFound {
        if (taskClusterOfUser == null) {
            throw new IllegalArgumentException("Null reference in DeleteTaskCommand::execute. ");
        }

        if (!parameters.isDateSet()) {
            taskClusterOfUser.deleteTask(parameters.name());
        } else {
            taskClusterOfUser.deleteTask(parameters.name(), parameters.date());
        }

        return OUTPUT;
    }
}
