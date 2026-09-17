package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.accountcommand;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;

public class AccountCommandFactory {
    private static final int LEN_OF_LOGIN_COMMAND = 3;
    private static final int LEN_OF_REGISTER_COMMAND = 3;
    private static final int LEN_OF_HELP_COMMAND = 1;

    private static final int COMMAND_NAME_INDEX = 0;
    private static final int INDEX_OF_USERNAME = 1;
    private static final int INDEX_OF_PASSWORD = 2;

    private static AccountCommand createLoginCommand(String[] tokens) throws WrongCommandSyntax {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in AccountCommandFactory::createLoginCommand. ");
        }

        if (tokens.length != LEN_OF_LOGIN_COMMAND) {
            throw new WrongCommandSyntax(LoginCommand.WRONG_SYNTAX_MESSAGE);
        }

        return new LoginCommand(tokens[INDEX_OF_USERNAME], tokens[INDEX_OF_PASSWORD]);
    }

    private static AccountCommand createRegisterCommand(String[] tokens) throws WrongCommandSyntax {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in AccountCommandFactory::createLoginCommand. ");
        }

        if (tokens.length != LEN_OF_REGISTER_COMMAND) {
            throw new WrongCommandSyntax(RegisterCommand.WRONG_SYNTAX_MESSAGE);
        }

        return new RegisterCommand(tokens[INDEX_OF_USERNAME], tokens[INDEX_OF_PASSWORD]);
    }

    private static AccountCommand createHelpCommand(String[] tokens) throws WrongCommandSyntax {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in AccountCommandFactory::createHelpCommand. ");
        }

        if (tokens.length != LEN_OF_HELP_COMMAND) {
            throw new WrongCommandSyntax("Wrong syntax of help command. " + HelpAccountCommand.SYNTAX);
        }

        return new HelpAccountCommand();
    }

    public static AccountCommand of(String input) throws WrongCommandSyntax {
        if (input == null) {
            throw new IllegalArgumentException("Null reference in AccountCommandFactory::of. ");
        }

        String[] tokens = input.split(" ");

        return switch (tokens[0]) {
            case "login" -> createLoginCommand(tokens);
            case "register" -> createRegisterCommand(tokens);
            case "help" -> createHelpCommand(tokens);
            default -> new WrongCommand(tokens[0]);
        };
    }
}
