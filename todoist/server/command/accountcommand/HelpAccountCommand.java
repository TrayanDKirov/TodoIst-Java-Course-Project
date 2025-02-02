package bg.sofia.uni.fmi.mjt.project.todoist.server.command.accountcommand;

import bg.sofia.uni.fmi.mjt.project.todoist.server.command.CommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;

public class HelpAccountCommand implements AccountCommand {
    private static final String OUTPUT;

    static {
        OUTPUT = "Possible commands: " + System.lineSeparator()
            + CommandSyntax.REGISTER_SYNTAX + " - to register into the system. " + System.lineSeparator()
            + CommandSyntax.LOGIN_SYNTAX + " - to log into your existing account. ";
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
