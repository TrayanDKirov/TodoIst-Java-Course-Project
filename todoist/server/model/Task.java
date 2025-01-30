package bg.sofia.uni.fmi.mjt.project.todoist.server.model;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.TaskParameterNotSet;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String name;
    private LocalDateTime date;
    private LocalDateTime dueDate;
    private String description;

    private Task(Builder builder) {
        if (builder == null) {
            throw new IllegalArgumentException("Null reference in Task constructor");
        }

        this.name = builder.name;
        this.date = builder.date;
        this.dueDate = builder.dueDate;
        this.description = builder.description;
    }

    public String getName() {
        return this.name;
    }

    public boolean isDateSet() {
        return date != null;
    }

    public LocalDateTime getDate() {
        if (date == null) {
            throw new TaskParameterNotSet("Date has not been set");
        }

        return this.date;
    }

    public boolean isDueDateSet() {
        return dueDate != null;
    }

    public LocalDateTime getDueDate() {
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

    public static class Builder {
        private String name;
        private LocalDateTime date;
        private LocalDateTime dueDate;
        private String description;

        public Builder(String name) {
            if (name == null) {
                throw new IllegalArgumentException("Null reference in Task::Builder constructor");
            }

            this.name = name;
        }

        void setDate(String dateStr) {
            if (dateStr == null) {
                throw new IllegalArgumentException("Null reference in Task::Builder::setDate");
            }


        }

        void setDueDate(String dueDateStr) {
            if (dueDateStr == null) {
                throw new IllegalArgumentException("Null reference in Task::Builder::setDueDate");
            }


        }

        void setDescription(String description) {
            if (description == null) {
                throw new IllegalArgumentException("Null reference in Task::Builder::setDate");
            }

            this.description = description;
        }

        public Task build() {
            return new Task(this);
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
