package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters;

public enum ParameterType {
    NAME_OF_TASK,
    DATE_OF_TASK,
    DUE_DATE_OF_TASK,
    DESCRIPTION_OF_TASK,
    NEW_DATE,
    NEW_DUE_DATE,
    NEW_DESCRIPTION,
    COLLABORATION_NAME,
    USERNAME,
    TASK_NAME,
    UNKNOWN_PARAMETER;

    public static final String DEF_FOR_NAME_OF_TASK = "name";
    public static final String DEF_FOR_DATE_OF_TASK  = "date";
    public static final String DEF_FOR_DUE_DATE_OF_TASK = "dueDate";
    public static final String DEF_FOR_DESCRIPTION_OF_TASK = "description";
    public static final String DEF_FOR_NEW_DATE = "newDate";
    public static final String DEF_FOR_NEW_DUE_DATE = "newDueDate";
    public static final String DEF_FOR_NEW_DESCRIPTION = "newDescription";
    public static final String DEF_FOR_COLLABORATION_NAME = "collaboration";
    public static final String DEF_FOR_USERNAME = "user";
    public static final String DEF_FOR_TASK_NAME = "task";

    public static ParameterType of(String definitionName) {
        if (definitionName == null) {
            throw new IllegalArgumentException("Null reference in ParameterType::of. ");
        }

        return switch (definitionName) {
            case DEF_FOR_NAME_OF_TASK -> ParameterType.NAME_OF_TASK;
            case DEF_FOR_DATE_OF_TASK -> ParameterType.DATE_OF_TASK;
            case DEF_FOR_DUE_DATE_OF_TASK -> ParameterType.DUE_DATE_OF_TASK;
            case DEF_FOR_DESCRIPTION_OF_TASK -> ParameterType.DESCRIPTION_OF_TASK;
            case DEF_FOR_NEW_DATE -> ParameterType.NEW_DATE;
            case DEF_FOR_NEW_DUE_DATE -> ParameterType.NEW_DUE_DATE;
            case DEF_FOR_NEW_DESCRIPTION -> ParameterType.NEW_DESCRIPTION;
            case DEF_FOR_COLLABORATION_NAME -> ParameterType.COLLABORATION_NAME;
            case DEF_FOR_USERNAME -> ParameterType.USERNAME;
            case DEF_FOR_TASK_NAME -> ParameterType.TASK_NAME;
            default -> ParameterType.UNKNOWN_PARAMETER;
        };
    }
}
