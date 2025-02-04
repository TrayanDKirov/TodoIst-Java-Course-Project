package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.command;

import bg.sofia.uni.fmi.mjt.project.todoist.server.model.Task;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

import java.time.LocalDate;

public class UpdateTaskCommand implements Command {
    private static final String OUTPUT = "Task successfully updated. ";

    private String nameOfTask;
    private LocalDate dateOfTask;

    private Task taskToReplace;

    private UpdateTaskCommand(Builder builder) {
        if (builder == null) {
            throw new IllegalArgumentException("Null reference in UpdateTaskCommand constructor. ");
        }

        this.nameOfTask = builder.nameOfTask;
        this.dateOfTask = builder.dateOfTask;

        this.taskToReplace = builder.taskToReplace;
    }

    boolean isDateOfTaskSet() {
        return dateOfTask != null;
    }

    public static class Builder {
        private String nameOfTask;
        private LocalDate dateOfTask;

        private Task taskToReplace;

        public Builder(String nameOfTask, Task taskToReplace) {
            if (nameOfTask == null || taskToReplace == null) {
                throw new IllegalArgumentException("Null reference in DeleteTaskCommand::Builder constructor. ");
            }

            this.nameOfTask = nameOfTask;
        }

        public Builder setDate(LocalDate dateOfTask) {
            if (dateOfTask == null) {
                throw new IllegalArgumentException("Null reference in DeleteTaskCommand::Builder::setDate. ");
            }

            this.dateOfTask = dateOfTask;

            return this;
        }

        public UpdateTaskCommand build() {
            return new UpdateTaskCommand(this);
        }
    }


    @Override
    public String execute(TaskCluster taskCluster) {
        if (taskCluster == null) {
            throw new IllegalArgumentException("Null reference in UpdateTaskCommand::execute. ");
        }

        if (!isDateOfTaskSet()) {
            taskCluster.replaceTask(nameOfTask, taskToReplace);
        } else {
            taskCluster.replaceTask(nameOfTask, dateOfTask, taskToReplace);
        }

        return OUTPUT;
    }
}
