package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.accountcommand;

import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;

public class WrongCommand implements AccountCommand {
    private String output;
    private String commandUsed;

    public WrongCommand(String commandUsed) {
        if (commandUsed == null) {
            throw new IllegalArgumentException("Null reference in WrongCommand constructor. ");
        }

        this.commandUsed = commandUsed;
        this.output = "Unknown command used- " +  commandUsed + ". Use command \"help\" to see all commands. ";
    }

    @Override
    public AccountCommandResult execute(TodoIstRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Null reference in WrongCommand::execute. ");
        }

        return new AccountCommandResult.Builder(output)
            .build();
    }
}
