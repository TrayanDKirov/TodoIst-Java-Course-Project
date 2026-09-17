package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.CommandParameter;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.ParameterType;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.DateStringFormater;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

import java.time.LocalDate;

public class ListTasks extends TaskCommand {
    private static final int MAX_LEN_OF_LIST_TASK_COMMAND = 2;
    private static final int DATE_INDEX = 1;

    public static final String SYNTAX = "\"list-tasks |date=<date>|\"";
    public static final String EXPLANATION =
        "to list information about all tasks from your inbox or from a certain date. ";

    private LocalDate date;

    public boolean isDateParameterSet() {
        return date != null;
    }

    public ListTasks(TaskCluster taskCluster, LocalDate date) {
        super(taskCluster);

        if (date == null) {
            throw new IllegalArgumentException("Null reference in ListTasks constructor. ");
        }

        this.date = date;
    }

    public void parseParameters(String[] tokens) throws WrongCommandSyntax {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in ListTasks::parseParameters()");
        }

        if (DATE_INDEX >= tokens.length) {
            return ;
        }

        CommandParameter parameter = CommandParameter.parseParameter(tokens[DATE_INDEX]);
        if (parameter.type() != ParameterType.DATE_OF_TASK) {
            throw new WrongCommandSyntax("Command list-tasks accepts only date parameter. ");
        }

        this.date =  DateStringFormater.stringToDate(parameter.param());
    }

    public ListTasks(TaskCluster taskCluster, String[] tokens) throws WrongCommandSyntax {
        super(taskCluster);

        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in ListTasks constructor. ");
        }

        if (tokens.length > MAX_LEN_OF_LIST_TASK_COMMAND) {
            throw new WrongCommandSyntax("Too many parameters in list-tasks command. ");
        }

        parseParameters(tokens);
    }

    @Override
    public String execute() {
        if (!isDateParameterSet()) {
            return taskCluster.listTasks();
        } else {
            return taskCluster.listTasks(date);
        }
    }
}
