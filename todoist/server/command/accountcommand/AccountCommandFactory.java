package bg.sofia.uni.fmi.mjt.project.todoist.server.command.accountcommand;

import bg.sofia.uni.fmi.mjt.project.todoist.server.command.CommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntaxException;

public class AccountCommandFactory {
    private static final int RIGHT_COMMAND_TOKENS_LEN = 3;
    private static final int HELP_COMMAND_TOKENS_LEN = 1;

    private static final int USERNAME_INDEX = 1;
    private static final int PASSWORD_INDEX = 1;

    private static AccountCommand createAccountCommand(String[] tokens, AccountCommandType commandType) throws WrongCommandSyntaxException {
        if (tokens == null || commandType == null) {
            throw new IllegalArgumentException("Null reference in AccountCommandFactory::createLoginCommand. ");
        }

        if (tokens.length != RIGHT_COMMAND_TOKENS_LEN) {
            switch (commandType) {
                case REGISTER -> throw new WrongCommandSyntaxException(
                    "Wrong syntax of register command. " + CommandSyntax.REGISTER_SYNTAX);
                case LOGIN ->  throw new WrongCommandSyntaxException(
                    "Wrong syntax of login command. " + CommandSyntax.LOGIN_SYNTAX);
            }
        }

        return switch (commandType) {
            case LOGIN -> new LoginCommand(tokens[USERNAME_INDEX], tokens[PASSWORD_INDEX]);
            case REGISTER -> new RegisterCommand(tokens[USERNAME_INDEX], tokens[PASSWORD_INDEX]);
        };
    }

    private static AccountCommand createHelpCommand(String[] tokens) throws WrongCommandSyntaxException {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in AccountCommandFactory::createHelpCommand. ");
        }

        if (tokens.length != HELP_COMMAND_TOKENS_LEN) {
            throw new WrongCommandSyntaxException("Wrong syntax of help command. " + CommandSyntax.HELP_SYNTAX);
        }

        return new HelpAccountCommand();
    }

    public static AccountCommand of(String input) throws WrongCommandSyntaxException {
        String[] tokens = input.split(" ");

        return switch (tokens[0]) {
            case "login" -> createAccountCommand(tokens, AccountCommandType.LOGIN);
            case "register" -> createAccountCommand(tokens, AccountCommandType.REGISTER);
            case "help" -> createHelpCommand(tokens);
            default -> new WrongCommand(tokens[0]);
        };
    }
}
