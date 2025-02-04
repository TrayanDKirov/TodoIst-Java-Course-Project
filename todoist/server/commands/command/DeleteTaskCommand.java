package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.command;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.TaskNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

import java.time.LocalDate;

public class DeleteTaskCommand implements Command {
    private static final String OUTPUT = "Task successfully deleted. ";

    private String nameOfTask;
    private LocalDate date;

    public boolean isDateSet() {
        return date != null;
    }

    private DeleteTaskCommand(Builder builder) {
        if (builder  == null) {
            throw new IllegalArgumentException("Null reference in DeleteTaskCommand constructor. ");
        }

        this.nameOfTask = builder.nameOfTask;
        this.date = builder.date;
    }

    public static class Builder {
        private String nameOfTask;
        private LocalDate date;

        public Builder(String nameOfTask) {
            if (nameOfTask == null) {
                throw new IllegalArgumentException("Null reference in DeleteTaskCommand::Builder constructor. ");
            }

            this.nameOfTask = nameOfTask;
        }

        public Builder setDate(LocalDate date) {
            if (date == null) {
                throw new IllegalArgumentException("Null reference in DeleteTaskCommand::Builder::setDate. ");
            }

            this.date = date;

            return this;
        }

        public DeleteTaskCommand build() {
            return new DeleteTaskCommand(this);
        }
    }

    @Override
    public String execute(TaskCluster taskClusterOfUser) throws TaskNotFound {
        if (taskClusterOfUser == null) {
            throw new IllegalArgumentException("Null reference in DeleteTaskCommand::execute. ");
        }

        if (!isDateSet()) {
            taskClusterOfUser.deleteTask(nameOfTask);
        } else {
            taskClusterOfUser.deleteTask(nameOfTask, date);
        }

        return OUTPUT;
    }
}
