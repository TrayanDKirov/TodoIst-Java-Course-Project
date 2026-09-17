package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.Command;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

public abstract class TaskCommand implements Command {
    protected TaskCluster taskCluster;

    public TaskCommand(TaskCluster taskCluster) {
        if (taskCluster == null) {
            throw new IllegalArgumentException("Null reference in TaskCommand constructor. ");
        }

        this.taskCluster = taskCluster;
    }
}
