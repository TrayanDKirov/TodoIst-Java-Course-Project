package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.accountcommand;

import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;

public class HelpAccountCommand implements AccountCommand {
    private static final String OUTPUT;

    public static final String SYNTAX = "\"help\"";

    static {
        OUTPUT = "Possible commands: " + System.lineSeparator()
            + RegisterCommand.SYNTAX + " - to register into the system. " + System.lineSeparator()
            + LoginCommand.SYNTAX + " - to log into your existing account. ";
    }

    @Override
    public AccountCommandResult execute(TodoIstRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Null reference HelpAccountCommand::execute");
        }

        return new AccountCommandResult.Builder(OUTPUT)
            .build();
    }
}
