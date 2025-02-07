package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.CommandParameters;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.TaskNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.Task;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

public class GetTaskCommand implements TaskCommand {
    private static final String OUTPUT = "Task found. Here is it:" + System.lineSeparator();

    private CommandParameters parameters;

    public static boolean areValidParameters(CommandParameters parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("Null reference in GetTaskCommand::areValidParameters. ");
        }

        return parameters.isNameSet() && !parameters.isNewDateSet()
            && !parameters.isNewDueDateSet() && !parameters.isNewDescriptionSet();
    }

    public GetTaskCommand(CommandParameters parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("Null reference in GetTaskCommand constructor. ");
        }

        if (!parameters.isNameSet()) {
            throw new IllegalArgumentException("Parameters in GetTaskCommand constructor should be set. ");
        }


        this.parameters = parameters;
    }

    @Override
    public String execute(TaskCluster taskCluster) throws TaskNotFound {
        if (taskCluster == null) {
            throw new IllegalArgumentException("Null reference in GetTaskCommand::execute");
        }

        Task task;
        if (!parameters.isDateSet()) {
            task = taskCluster.getTask(parameters.name());
        } else {
            task = taskCluster.getTask(parameters.name(), parameters.date());
        }

        return OUTPUT + task.getInfo();
    }
}
