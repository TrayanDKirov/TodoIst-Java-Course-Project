package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.CommandException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

public interface TaskCommand {
    String execute(TaskCluster taskClusterOfUser) throws CommandException;
}
