package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.command;

import bg.sofia.uni.fmi.mjt.project.todoist.server.model.Task;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

public class AddTaskCommand implements Command {
    private static final String OUTPUT = "Successfully added task. ";
    private Task taskToAdd;

    public AddTaskCommand(Task taskToAdd) {
        if (taskToAdd == null) {
            throw new IllegalArgumentException("Null reference in AddTaskCommand constructor. ");
        }

        this.taskToAdd = taskToAdd;
    }

    @Override
    public String execute(TaskCluster taskClusterOfUser) {
        if (taskClusterOfUser == null) {
            throw new IllegalArgumentException("Null reference in AddTaskCommand::execute. ");
        }

        taskClusterOfUser.addTask(taskToAdd);

        return OUTPUT;
    }
}
