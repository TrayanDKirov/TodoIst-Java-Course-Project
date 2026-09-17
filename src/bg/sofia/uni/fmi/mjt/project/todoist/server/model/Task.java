package bg.sofia.uni.fmi.mjt.project.todoist.server.model;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.task.TaskParameterNotSet;

import java.io.Serializable;
import java.time.LocalDate;

public class Task implements Serializable {
    private static final long serialVersionUID = -4064278095145497837L;

    private String name;
    private LocalDate date;
    private LocalDate dueDate;
    private String description;

    private boolean completed;
    private String assignee;

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

    public boolean isAssigneeSet() {
        return assignee != null;
    }

    public String getAssignee() {
        if (assignee == null) {
            throw new TaskParameterNotSet("Assignee has not been set");
        }
        return this.assignee;
    }

    public void setAssignee(String assignee) {
        if (assignee == null) {
            throw new IllegalArgumentException("Null reference in Task::setAssignee");
        }
        this.assignee = assignee;
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
            result.append(DateStringFormater.dateToString(date));
        } else {
            result.append("none");
        }

        result.append(System.lineSeparator()).append("Due date: ");

        if (isDueDateSet()) {
            result.append(DateStringFormater.dateToString(dueDate));
        } else {
            result.append("none");
        }

        result.append(System.lineSeparator()).append("Description: ");

        if (isDescriptionSet()) {
            result.append(description);
        } else {
            result.append("none");
        }

        result.append(System.lineSeparator()).append("Assignee: ");
        if (isAssigneeSet()) {
            result.append(assignee);
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
}
