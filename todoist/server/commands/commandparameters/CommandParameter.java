package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;

public record CommandParameter(String parameter, ParameterType type) {
    private static final String DEFINITION_SEPARATOR = "=";
    private static final int DEFINITION_TOKENS = 2;

    private static final int INDEX_OF_DEFINITION_NAME = 0;
    private static final int INDEX_OF_DEFINITION = 1;

    private static final String NAME = "name";
    private static final String DATE = "date";
    private static final String DUE_DATE = "dueDate";
    private static final String DESCRIPTION = "description";

    private static final String NEW_DATE = "newDate";
    private static final String NEW_DUE_DATE = "newDueDate";
    private static final String NEW_DESCRIPTION = "newDescription";

    public CommandParameter {
        if (parameter == null || type == null) {
            throw new IllegalArgumentException("Null reference in CommandParameter constructor. ");
        }
    }

    public static CommandParameter parseParameter(String str) throws WrongCommandSyntax {
        if (str == null) {
            throw new IllegalArgumentException("Null reference in CommandParameter::parseParameter. ");
        }

        String[] tokens = str.split(DEFINITION_SEPARATOR);
        if (tokens.length != DEFINITION_TOKENS) {
            throw new WrongCommandSyntax(""); //!!
        }

        String definitionName = tokens[INDEX_OF_DEFINITION_NAME];
        String definition = tokens[INDEX_OF_DEFINITION];

        return switch (definitionName) {
            case NAME -> new CommandParameter(definition, ParameterType.NAME_OF_TASK);
            case DATE -> new CommandParameter(definition, ParameterType.DATE_OF_TASK);
            case DUE_DATE, NEW_DUE_DATE -> new CommandParameter(definition, ParameterType.NEW_DUE_DATE);
            case DESCRIPTION, NEW_DESCRIPTION -> new CommandParameter(definition, ParameterType.NEW_DESCRIPTION);
            case NEW_DATE -> new CommandParameter(definition, ParameterType.NEW_DATE);
            default -> throw new WrongCommandSyntax(""); //!!
        };
    }
}
