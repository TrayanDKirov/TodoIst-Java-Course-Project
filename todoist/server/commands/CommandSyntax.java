package bg.sofia.uni.fmi.mjt.project.todoist.server.commands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands.UpdateTaskCommand;

public class CommandSyntax {
    public static final String REGISTER_SYNTAX = "\"register <username> <password>\"";
    public static final String LOGIN_SYNTAX = "\"login <username> <password>\"";
    public static final String HELP_SYNTAX = "\"help\"";

    public static final String DELETE_INBOX_TASK_SYNTAX = "\"delete-task name=<name of task>\"";
    public static final String DELETE_DATED_TASK_SYNTAX = "\"delete-task name=<name of task> date=<date of task>\"";

    public static final String GET_INBOX_TASK_SYNTAX = "\"get-task name=<name of task>\"";
    public static final String GET_DATED_TASK_SYNTAX = "\"get-task name=<name of task> date=<date of task>\"";

    public static final String UPDATE_INBOX_TASK_SYNTAX = "\"get-task name=<name of task> " +
        "|newDate=<new date>| |newDueDate=<new due date>| |newDescription=<new description>|\"";
    public static final String UPDATE_DATED_TASK_SYNTAX = "\"get-task name=<name of task> date=<date of task> " +
        "|newDate=<new date>| |newDueDate=<new due date>| |newDescription=<new description>|\"";

    public static final String ADD_TASK_SYNTAX = "\"add-task name=<name of task> " +
        "|date=<date>| |dueDate=<due date>| |description=<description>|\"";

    public static final String FINISH_INBOX_TASK_SYNTAX = "\"finish-task name=<name of task>";
    public static final String FINISH_DATED_TASK_SYNTAX = "\"finish-task name=<name of task> date=<date of task>";

    public static final String LIST_DASHBOARD_SYNTAX = "\"list-dashboard\"";

    public static final String LIST_TASKS_SYNTAX = "\"list-tasks\"";
    public static final String LIST_DATED_TASKS_SYNTAX = "\"list-tasks date=date\"";

    public static final String WRONG_REGISTER_SYNTAX_MESSAGE =
        "Wrong syntax of register command. Right syntax: " + System.lineSeparator()
        + REGISTER_SYNTAX;

    public static final String WRONG_LOGIN_SYNTAX_MESSAGE =
        "Wrong syntax of login command. Right syntax: " + System.lineSeparator()
        + LOGIN_SYNTAX;

    public static final String WRONG_DELETE_TASK_SYNTAX_MESSAGE =
        "Wrong syntax of delete-task command. Right syntaxes: " + System.lineSeparator()
        + DELETE_INBOX_TASK_SYNTAX + System.lineSeparator()
        + DELETE_DATED_TASK_SYNTAX;

    public static final String WRONG_GET_TASK_SYNTAX_MESSAGE =
        "Wrong syntax of get-task command. Right syntaxes: " + System.lineSeparator()
            + GET_INBOX_TASK_SYNTAX + System.lineSeparator()
            + GET_DATED_TASK_SYNTAX;

    public static final String WRONG_ADD_TASK_SYNTAX_MESSAGE =
        "Wrong syntax of add-task command. Right syntaxes: " + System.lineSeparator()
            + ADD_TASK_SYNTAX;

    public static final String WRONG_UPDATE_TASK_SYNTAX_MESSAGE =
        "Wrong syntax of update-task command. Right syntaxes: " + System.lineSeparator()
            + UPDATE_INBOX_TASK_SYNTAX + System.lineSeparator()
            + UPDATE_DATED_TASK_SYNTAX;

    public static final String WRONG_FINISH_TASK_SYNTAX_MESSAGE =
        "Wrong syntax of finish-task command. Right syntaxes: " + System.lineSeparator()
            + FINISH_INBOX_TASK_SYNTAX + System.lineSeparator()
            + FINISH_DATED_TASK_SYNTAX;

    public static final String WRONG_LIST_TASK_SYNTAX_MESSAGE =
        "Wrong syntax of list-tasks command. Right syntaxes: " + System.lineSeparator()
            + LIST_TASKS_SYNTAX + System.lineSeparator()
            + LIST_DATED_TASKS_SYNTAX;

    public static final String WRONG_LIST_DASHBOARD_SYNTAX_MESSAGE =
        "Wrong syntax of list-dashboard command. Right syntaxes: " + System.lineSeparator()
            + LIST_DASHBOARD_SYNTAX;
}
