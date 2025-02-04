package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.command;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.TaskNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.Task;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

import java.time.LocalDate;

public class FinishTaskCommand implements Command {
    private static final String OUTPUT = "Task successfully finnished. ";

    private String nameOfTask;
    private LocalDate dateOfTask;

    public boolean isDateSet() {
        return dateOfTask != null;
    }

    private FinishTaskCommand(Builder builder) {
        if (builder  == null) {
            throw new IllegalArgumentException("Null reference in DeleteTaskCommand constructor. ");
        }

        this.nameOfTask = builder.nameOfTask;
        this.dateOfTask = builder.dateOfTask;
    }

    public static class Builder {
        private String nameOfTask;
        private LocalDate dateOfTask;

        public Builder(String nameOfTask) {
            if (nameOfTask == null) {
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

        public FinishTaskCommand build() {
            return new FinishTaskCommand(this);
        }
    }

    @Override
    public String execute(TaskCluster taskCluster) throws TaskNotFound {
        if (taskCluster == null) {
            throw new IllegalArgumentException("Null reference in FinishTaskCommand::execute. ");
        }

        Task task;
        if (!isDateSet()) {
            task = taskCluster.getTask(nameOfTask);
        } else {
            task = taskCluster.getTask(nameOfTask, dateOfTask);
        }

        task.complete();

        return OUTPUT;
    }
}
