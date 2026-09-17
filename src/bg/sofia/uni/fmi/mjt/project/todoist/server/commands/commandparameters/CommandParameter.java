package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;

public record CommandParameter(String param, ParameterType type) {
    private static final String DEFINITION_SEPARATOR = "=";
    private static final int DEFINITION_TOKENS = 2;

    private static final int INDEX_OF_DEFINITION_NAME = 0;
    private static final int INDEX_OF_DEFINITION = 1;

    public CommandParameter {
        if (param == null || type == null) {
            throw new IllegalArgumentException("Null reference in CommandParameter constructor. ");
        }
    }

    public static CommandParameter parseParameter(String str) throws WrongCommandSyntax {
        if (str == null) {
            throw new IllegalArgumentException("Null reference in CommandParameter::parseParameter. ");
        }

        String[] tokens = str.split(DEFINITION_SEPARATOR);
        if (tokens.length != DEFINITION_TOKENS) {
            throw new WrongCommandSyntax("Parameter definitions should be in the format: <definitionName>=<param>. ");
        }

        String definitionName = tokens[INDEX_OF_DEFINITION_NAME];
        String definition = tokens[INDEX_OF_DEFINITION];

        return new CommandParameter(definition, ParameterType.of(definitionName));
    }

    public static CommandParameter[] parseArrayOfParameters(String[] tokens, int begin) throws WrongCommandSyntax {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in CommandParameter::parseArrayOfParameters. ");
        }

        if (begin < 0 || begin >= tokens.length) {
            throw new IllegalArgumentException("Invalid begin parameter in CommandParameter::parseArrayOfParameters");
        }

        CommandParameter[] parameters = new CommandParameter[tokens.length - begin];

        for (int i = begin, currParam = 0; currParam < parameters.length; i++, currParam++) {
            parameters[currParam] = parseParameter(tokens[i]);
        }

        return parameters;
    }
}
