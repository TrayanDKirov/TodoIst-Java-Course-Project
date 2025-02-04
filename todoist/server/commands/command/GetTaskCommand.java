package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.command;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.TaskNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.Task;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskUtils;

import java.io.PrintStream;
import java.time.LocalDate;

public class GetTaskCommand implements Command {
    private static final String OUTPUT = "Task found. Here is it:" + System.lineSeparator();

    private String name;
    private LocalDate date;

    public GetTaskCommand(Builder builder) {
        if (builder == null) {
            throw new IllegalArgumentException("Null reference in GetTaskCommand constructor. ");
        }

        this.name = builder.name;
        this.date = builder.date;
    }

    private boolean isDateSet() {
        return date != null;
    }

    public static class Builder {
        private String name;
        private LocalDate date;

        public Builder(String name) {
            if (name == null) {
                throw new IllegalArgumentException("Null reference in GetTaskCommand::Builder constructor. ");
            }

            this.name = name;
        }

        public Builder setDate(LocalDate date) {
            if (date == null) {
                throw new IllegalArgumentException("Null reference in GetTaskCommand::Builder::setDate. ");
            }

            this.date = date;

            return this;
        }

        public GetTaskCommand build() {
            return new GetTaskCommand(this);
        }
    }

    @Override
    public String execute(TaskCluster taskCluster) throws TaskNotFound {
        if (taskCluster == null) {
            throw new IllegalArgumentException("Null reference in GetTaskCommand::execute");
        }

        Task task;
        if (!isDateSet()) {
            task = taskCluster.getTask(name);
        } else {
            task = taskCluster.getTask(name, date);
        }

        return OUTPUT + task.getInfo();
    }
}
