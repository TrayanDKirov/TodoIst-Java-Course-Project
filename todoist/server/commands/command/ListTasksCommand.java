package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.command;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.TaskNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.Task;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskUtils;

import java.time.LocalDate;
import java.util.Map;

public class ListTasksCommand implements Command {
    private static final String OUTPUT_FOR_DATE = "Tasks found for date - ";
    private static final String OUTPUT_FROM_INBOX = "Tasks shown from your inbox: " + System.lineSeparator();

    private boolean completed;
    private boolean isCompletedSet;

    private LocalDate date;

    private ListTasksCommand(Builder builder) {
        if (builder == null) {
            throw new IllegalArgumentException("Null reference in ListTasksCommand constructor. ");
        }

        this.completed = builder.completed;
        this.isCompletedSet = builder.isCompletedSet;

        this.date = builder.date;
    }

    private boolean isCompletedSet() {
        return isCompletedSet;
    }

    private boolean isDateSet() {
        return date != null;
    }

    public static class Builder {
        private boolean completed;
        private boolean isCompletedSet;

        private LocalDate date;

        public Builder() {
            this.isCompletedSet = false;
        }

        public Builder setIsCompleted(boolean completed) {
            this.completed = completed;
            this.isCompletedSet = true;

            return this;
        }

        public Builder setDate(LocalDate date) {
            if (date == null) {
                throw new IllegalArgumentException("Null reference in ListTasksCommand::Builder::setDate. ");
            }

            this.date = date;

            return this;
        }

        public ListTasksCommand build() {
            return new ListTasksCommand(this);
        }
    }

    private StringBuilder getOutputForDate() {
        if (!isDateSet()) {
            throw new RuntimeException(
                "ListTasksCommand::getOutputForDate should not be called when date is not set. ");
        }

        return new StringBuilder(OUTPUT_FOR_DATE)
            .append(TaskUtils.fromDateToString(date))
            .append(":");
    }

    private StringBuilder getOutputForInbox() {
        return new StringBuilder(OUTPUT_FROM_INBOX)
            .append(":");
    }

    @Override
    public String execute(TaskCluster taskClusterOfUser) throws TaskNotFound {
        if (taskClusterOfUser == null) {
            throw new IllegalArgumentException("Null reference in ListTasksCommand::execute");
        }

        Map<String, Task> tasks;
        StringBuilder result = new StringBuilder();
        if (!isDateSet()) {
            tasks = taskClusterOfUser.getTasks();

            result.append(getOutputForInbox());
        } else {
            tasks = taskClusterOfUser.getTasks(date);

            result.append(getOutputForDate());
        }

        return result.append(TaskUtils.listTasks(tasks))
            .toString().intern();
    }
}
