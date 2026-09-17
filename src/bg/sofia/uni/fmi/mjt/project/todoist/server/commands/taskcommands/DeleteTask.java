package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.task.TaskNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

public class DeleteTask extends NameDateCommand {
    private static final String OUTPUT = "Task successfully deleted. ";

    public static final String SYNTAX = "\"delete-task name=<name of task> |date=<date of task>|\"";
    public static final String EXPLANATION = "to delete task from your inbox or from a certain date. ";

    private static final int MAX_LEN_OF_DELETE_COMMAND = 3;

    public DeleteTask(TaskCluster taskCluster, String[] tokens) throws WrongCommandSyntax {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in NameDateCommand constructor. ");
        }

        if (tokens.length > MAX_LEN_OF_DELETE_COMMAND) {
            throw new WrongCommandSyntax("Too many parameters in delete-task command. ");
        }

        super(taskCluster, tokens);
    }

    @Override
    public String execute() throws TaskNotFound {
        if (!isDateParameterSet()) {
            taskCluster.deleteTask(name);
        } else {
            taskCluster.deleteTask(name, date);
        }

        return OUTPUT;
    }
}
