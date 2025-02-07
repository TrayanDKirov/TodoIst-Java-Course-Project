package bg.sofia.uni.fmi.mjt.project.todoist.server.model;

import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.CommandParameters;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.TaskParameterNotSet;

import java.time.LocalDate;
import java.util.Objects;

public class Task {
    private String name;
    private LocalDate date;
    private LocalDate dueDate;
    private String description;

    private boolean completed;

    private Task(Builder builder) {
        if (builder == null) {
            throw new IllegalArgumentException("Null reference in Task constructor");
        }

        this.name = builder.name;
        this.date = builder.date;
        this.dueDate = builder.dueDate;
        this.description = builder.description;

        this.completed = false;
    }

    public String getName() {
        return this.name;
    }

    public boolean isDateSet() {
        return date != null;
    }

    public LocalDate getDate() {
        if (date == null) {
            throw new TaskParameterNotSet("Date has not been set");
        }

        return this.date;
    }

    public boolean isDueDateSet() {
        return dueDate != null;
    }

    public LocalDate getDueDate() {
        if (dueDate == null) {
            throw new TaskParameterNotSet("Due date has not been set");
        }

        return this.dueDate;
    }

    public boolean isDescriptionSet() {
        return description != null;
    }

    public String getDescription() {
        if (description == null) {
            throw new TaskParameterNotSet("Description has not been set");
        }

        return this.description;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public boolean complete() {
        if (completed) {
            return false;
        }

        completed = true;
        return true;
    }

    public static class Builder {
        private String name;
        private LocalDate date;
        private LocalDate dueDate;
        private String description;

        public Builder(String name) {
            if (name == null) {
                throw new IllegalArgumentException("Null reference in Task::Builder constructor");
            }

            this.name = name;
        }

        public Builder setDate(LocalDate date) {
            if (date == null) {
                throw new IllegalArgumentException("Null reference in Task::Builder::setDate");
            }

            this.date = date;

            return this;
        }

        public Builder setDueDate(LocalDate dueDate) {
            if (dueDate == null) {
                throw new IllegalArgumentException("Null reference in Task::Builder::setDueDate");
            }

            this.dueDate = dueDate;

            return this;
        }

        public Builder setDescription(String description) {
            if (description == null) {
                throw new IllegalArgumentException("Null reference in Task::Builder::setDate");
            }

            this.description = description;

            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }

    public String getInfo() {
        StringBuilder result = new StringBuilder("Name: ").append(name)
            .append(" Completed: ").append(completed);

        result.append(System.lineSeparator()).append("Date: ");

        if (isDateSet()) {
            result.append(TaskUtils.fromDateToString(date));
        } else {
            result.append("none");
        }

        result.append(System.lineSeparator()).append("Due date: ");

        if (isDueDateSet()) {
            result.append(TaskUtils.fromDateToString(dueDate));
        } else {
            result.append("none");
        }

        result.append(System.lineSeparator()).append("Description: ");

        if (isDescriptionSet()) {
            result.append(description);
        } else {
            result.append("none");
        }

        return result.toString().intern();
    }

    public void updateTask(Task taskWithNewFields) {
        if (taskWithNewFields == null) {
            throw new IllegalArgumentException("Null reference in Task::updateTask. ");
        }

        if (taskWithNewFields.isDateSet()) {
            this.date = taskWithNewFields.date;
        }

        if (taskWithNewFields.isDueDateSet()) {
            this.dueDate = taskWithNewFields.dueDate;
        }

        if (taskWithNewFields.isDescriptionSet()) {
            this.description = taskWithNewFields.description;
        }
    }

    @Override
    public int hashCode() {
        if (isDateSet()) {
            return Objects.hash(name, date);
        }

        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        Task otherTask = (Task) other;
        if (isDateSet() && otherTask.isDateSet()) {
            return date.equals(otherTask.date) && name.equals(otherTask.name);
        }
        if (!isDateSet() && !otherTask.isDateSet()) {
            return name.equals(otherTask.name);
        }

        return false;
    }
}
