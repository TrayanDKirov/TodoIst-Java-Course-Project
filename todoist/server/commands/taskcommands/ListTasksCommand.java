package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.CommandParameters;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.TaskNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.Task;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskUtils;

import java.util.Map;

public class ListTasksCommand implements TaskCommand {
    private static final String OUTPUT_FOR_DATE = "Tasks found for date - ";
    private static final String OUTPUT_FROM_INBOX = "Tasks shown from your inbox: " + System.lineSeparator();

    private CommandParameters parameters;

    public static boolean areValidParameters(CommandParameters parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("Null reference in DeleteTaskCommand::areValidParameters. ");
        }

        return !parameters.isNameSet() && !parameters.isNewDateSet()
            && !parameters.isNewDueDateSet() && !parameters.isNewDescriptionSet();
    }

    public ListTasksCommand(CommandParameters parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("Null reference in ListTasksCommand constructor. ");
        }

        this.parameters = parameters;
    }

    private StringBuilder getOutputForDate() {
        if (!parameters.isDateSet()) {
            throw new RuntimeException(
                "ListTasksCommand::getOutputForDate should not be called when date is not set. ");
        }

        return new StringBuilder(OUTPUT_FOR_DATE)
            .append(TaskUtils.fromDateToString(parameters.date()))
            .append(":");
    }

    private StringBuilder getOutputForInbox() {
        return new StringBuilder(OUTPUT_FROM_INBOX);
    }

    @Override
    public String execute(TaskCluster taskClusterOfUser) throws TaskNotFound {
        if (taskClusterOfUser == null) {
            throw new IllegalArgumentException("Null reference in ListTasksCommand::execute");
        }

        Map<String, Task> tasks;
        StringBuilder result = new StringBuilder();
        if (!parameters.isDateSet()) {
            tasks = taskClusterOfUser.getTasks();

            result.append(getOutputForInbox());
        } else {
            tasks = taskClusterOfUser.getTasks(parameters.date());

            result.append(getOutputForDate());
        }

        return result.append(TaskUtils.listTasks(tasks))
            .toString().intern();
    }
}
