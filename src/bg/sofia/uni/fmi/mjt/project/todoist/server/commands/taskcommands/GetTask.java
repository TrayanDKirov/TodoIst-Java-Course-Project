package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.task.TaskNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.Task;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

public class GetTask extends NameDateCommand {
    private static final String OUTPUT = "Task found. Here is it:" + System.lineSeparator();

    public static final String SYNTAX = "\"get-task name=<name of task> |date=<date of task>|\"";
    public static final String EXPLANATION = "to get information about a task from your inbox or from a certain date. ";

    private static final int MAX_LEN_OF_GET_TASK_COMMAND = 3;

    public GetTask(TaskCluster taskCluster, String[] tokens) throws WrongCommandSyntax {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in NameDateCommand constructor. ");
        }

        if (tokens.length > MAX_LEN_OF_GET_TASK_COMMAND) {
            throw new WrongCommandSyntax("Too many parameters in get-task command. ");
        }

        super(taskCluster, tokens);
    }

    @Override
    public String execute() throws TaskNotFound {
        Task task;
        if (!isDateParameterSet()) {
            task = taskCluster.getTask(name);
        } else {
            task = taskCluster.getTask(name, date);
        }

        return OUTPUT + task.getInfo();
    }
}
