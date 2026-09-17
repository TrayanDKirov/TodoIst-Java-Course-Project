package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.CommandParameter;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters.ParameterType;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.DateStringFormater;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

import java.time.LocalDate;

public abstract class NameDateCommand extends TaskCommand {
    private static final int NAME_INDEX = 1;
    private static final int DATE_INDEX = 2;

    protected String name;
    protected LocalDate date;

    public boolean isDateParameterSet() {
        return date != null;
    }

    private void parseNameAndDate(String[] tokens) throws WrongCommandSyntax {
        CommandParameter currParam = CommandParameter.parseParameter(tokens[NAME_INDEX]);
        if (currParam.type() != ParameterType.NAME_OF_TASK) {
            throw new WrongCommandSyntax("First param must be name=<name of task>. ");
        }
        this.name = currParam.param();

        if (DATE_INDEX < tokens.length) {
            currParam = CommandParameter.parseParameter(tokens[DATE_INDEX]);
            if (currParam.type() != ParameterType.DATE_OF_TASK) {
                throw new WrongCommandSyntax("Second param must be \"date=<date of task>\". ");
            }
            this.date = DateStringFormater.stringToDate(currParam.param());
        }
    }

    public NameDateCommand(TaskCluster taskCluster, String[] tokens) throws WrongCommandSyntax {
        super(taskCluster);
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in NameDateCommand constructor. ");
        }

        if (NAME_INDEX >= tokens.length) {
            throw new WrongCommandSyntax("Command should have at least one command. ");
        }

        parseNameAndDate(tokens);
    }
}
