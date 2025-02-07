package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.accountcommand;

import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands.TaskCommand;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;

public class WrongCommand implements AccountCommand, TaskCommand {
    private String output;

    public WrongCommand(String commandUsed) {
        if (commandUsed == null) {
            throw new IllegalArgumentException("Null reference in WrongCommand constructor. ");
        }

        this.output = "Unknown command used - " +  commandUsed + ". Use command \"help\" to see all commands. ";
    }

    @Override
    public AccountCommandResult execute(TodoIstRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Null reference in WrongCommand::execute. ");
        }

        return new AccountCommandResult.Builder(output)
            .build();
    }

    @Override
    public String execute(TaskCluster taskCluster) {
        return output;
    }
}
