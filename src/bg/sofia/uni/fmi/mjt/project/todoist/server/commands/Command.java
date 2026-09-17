package bg.sofia.uni.fmi.mjt.project.todoist.server.commands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.CommandException;

public interface Command {
    String execute() throws CommandException;
}
