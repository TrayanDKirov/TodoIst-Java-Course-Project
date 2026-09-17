package bg.sofia.uni.fmi.mjt.project.todoist.server.commands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.colaborationcommands.AddCollaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.colaborationcommands.AddUser;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.colaborationcommands.AssignTask;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.colaborationcommands.DeleteCollaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.colaborationcommands.ListCollaborations;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.colaborationcommands.ListUsers;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands.AddTask;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands.DeleteTask;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands.FinishTask;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands.GetTask;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands.ListTasks;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands.UpdateTask;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;

public class HelpCommand implements Command {
    private static final String OUTPUT = "Possible task commands: add-task, delete-task, "
        + "finish-task, get-task, list-tasks, update-task." + System.lineSeparator()
        + "Possible collaboration commands: add-collaboration, delete-collaboration, list-collaborations, "
        + "add-user, list-users, assign-task." + System.lineSeparator()
        + "Write help <name of command> - to get explanation of a command. " + System.lineSeparator()
        + "|statement| - means that \"statement\" is optional";

    private static final String EXPLANATION_SEP = " - ";

    private static final int LEN_OF_HELP_COMMAND = 2;
    private static final int LEN_OF_SHORT_HELP_COMMAND = 1;

    private String nameOfCommand;

    private boolean isNameOfCommandSet() {
        return nameOfCommand != null;
    }

    private void parseParameters(String[] tokens) {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in HelpCommand::parseParameters. ");
        }
    }

    public HelpCommand(String[] tokens) throws WrongCommandSyntax {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in HelpCommand constructor. ");
        }

        if (tokens.length > LEN_OF_HELP_COMMAND) {
            throw new WrongCommandSyntax("Help command accepts either no params " +
                "- to get name of other commands or one param - name of command to get its syntax. ");
        } else if (tokens.length == LEN_OF_HELP_COMMAND) {
            this.nameOfCommand = tokens[LEN_OF_HELP_COMMAND - 1];
        }
    }

    @Override
    public String execute() {
        if (!isNameOfCommandSet()) {
            return OUTPUT;
        }

        return switch (nameOfCommand) {
            case "add-task" -> AddTask.SYNTAX + System.lineSeparator() + EXPLANATION_SEP + AddTask.EXPLANATION;
            case "delete-task" -> DeleteTask.SYNTAX + EXPLANATION_SEP + DeleteTask.EXPLANATION;
            case "finish-task" -> FinishTask.SYNTAX + EXPLANATION_SEP + FinishTask.EXPLANATION;
            case "get-task" -> GetTask.SYNTAX + EXPLANATION_SEP + GetTask.EXPLANATION;
            case "list-tasks" -> ListTasks.SYNTAX + EXPLANATION_SEP + ListTasks.EXPLANATION;
            case "update-task" -> UpdateTask.SYNTAX + System.lineSeparator() + EXPLANATION_SEP + UpdateTask.EXPLANATION;
            case "add-collaboration" -> AddCollaboration.SYNTAX + EXPLANATION_SEP + AddCollaboration.EXPLANATION;
            case "delete-collaboration" -> DeleteCollaboration.SYNTAX + EXPLANATION_SEP + DeleteCollaboration.EXPLANATION;
            case "list-collaborations" -> ListCollaborations.SYNTAX + EXPLANATION_SEP + ListCollaborations.EXPLANATION;
            case "add-user" -> AddUser.SYNTAX + EXPLANATION_SEP + AddUser.EXPLANATION;
            case "list-users" -> ListUsers.SYNTAX + EXPLANATION_SEP + ListUsers.EXPLANATION;
            case "assign-task" -> AssignTask.SYNTAX + EXPLANATION_SEP + AssignTask.EXPLANATION;
            default -> "Unknown command. ";
        };
    }
}
