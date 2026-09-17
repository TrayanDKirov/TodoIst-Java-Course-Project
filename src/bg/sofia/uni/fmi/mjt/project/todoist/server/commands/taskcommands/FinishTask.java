package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.task.TaskNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.Task;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

public class FinishTask extends NameDateCommand {
    private static final String OUTPUT = "Task successfully finished. ";

    public static final String SYNTAX = "\"finish-task name=<name of task> |date=<date of task>|\"";
    public static final String EXPLANATION = "to finish task from your inbox or from a certain date. ";

    private static final int MAX_LEN_OF_FINISH_TASK_COMMAND = 3;

    public FinishTask(TaskCluster taskCluster, String[] tokens) throws WrongCommandSyntax {
        if (tokens == null) {
            throw new IllegalArgumentException("Null reference in NameDateCommand constructor. ");
        }

        if (tokens.length > MAX_LEN_OF_FINISH_TASK_COMMAND) {
            throw new WrongCommandSyntax("Too many parameters in finish-task command. ");
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

        task.complete();

        return OUTPUT;
    }
}
