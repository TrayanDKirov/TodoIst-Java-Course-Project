package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.command;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.CommandException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

public interface Command {
    String execute(TaskCluster taskClusterOfUser) throws CommandException;
}
