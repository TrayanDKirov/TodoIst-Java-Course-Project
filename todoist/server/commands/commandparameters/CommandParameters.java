package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.CommandParameterNotSet;

import java.time.LocalDate;

public class CommandParameters {
    private String name;
    private LocalDate date;

    private LocalDate newDate;
    private LocalDate newDueDate;
    private String newDescription;

    private CommandParameters(Builder builder) {
        if (builder  == null) {
            throw new IllegalArgumentException("Null reference in CommandParameters constructor. ");
        }

        this.name = builder.name;
        this.date = builder.date;

        this.newDate = builder.newDate;
        this.newDueDate = builder.newDueDate;
        this.newDescription = builder.newDescription;
    }

    public boolean isNameSet() {
        return name != null;
    }

    public boolean isDateSet() {
        return date != null;
    }

    public boolean isNewDateSet() {
        return newDate != null;
    }

    public boolean isNewDueDateSet() {
        return newDueDate != null;
    }

    public boolean isNewDescriptionSet() {
        return newDescription != null;
    }

    public String name() {
        if (!isNameSet()) {
            throw new CommandParameterNotSet("You can not access name in CommandParameters if it is not set. ");
        }

        return this.name;
    }

    public LocalDate date() {
        if (!isDateSet()) {
            throw new CommandParameterNotSet("You can not access date in CommandParameters if it is not set. ");
        }

        return this.date;
    }

    public LocalDate newDate() {
        if (!isNewDateSet()) {
            throw new CommandParameterNotSet("You can not access newDate in CommandParameters if it is not set. ");
        }

        return this.newDate;
    }

    public LocalDate newDueDate() {
        if (!isNewDueDateSet()) {
            throw new CommandParameterNotSet("You can not access newDueDate in CommandParameters if it is not set. ");
        }

        return this.newDueDate;
    }

    public String newDescription() {
        if (!isNewDescriptionSet()) {
            throw new CommandParameterNotSet(
                "You can not access newDescription in CommandParameters if it is not set. ");
        }

        return this.newDescription;
    }

    public static class Builder {
        private String name;
        private LocalDate date;

        private LocalDate newDate;
        private LocalDate newDueDate;
        private String newDescription;

        public Builder setName(String name) {
            if (name == null) {
                throw new IllegalArgumentException("Null reference in CommandParameters::Builder::setName. ");
            }

            this.name = name;

            return this;
        }

        public Builder setDateOfTask(LocalDate date) {
            if (date == null) {
                throw new IllegalArgumentException("Null reference in CommandParameters::Builder::setDate. ");
            }

            this.date = date;

            return this;
        }

        public Builder setNewDate(LocalDate newDate) {
            if (newDate == null) {
                throw new IllegalArgumentException("Null reference in CommandParameters::Builder::setUserName. ");
            }

            this.newDate = newDate;

            return this;
        }

        public Builder setNewDueDate(LocalDate newDueDate) {
            if (newDueDate == null) {
                throw new IllegalArgumentException("Null reference in CommandParameters::Builder::setPassword. ");
            }

            this.newDueDate = newDueDate;

            return this;
        }

        public Builder setNewDescription(String newDescription) {
            if (newDescription == null) {
                throw new IllegalArgumentException("Null reference in CommandParameters::Builder::setDateOfTask. ");
            }

            this.newDescription = newDescription;

            return this;
        }

        public CommandParameters build() {
            return new CommandParameters(this);
        }
    }
}
