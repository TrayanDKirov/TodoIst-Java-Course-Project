package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.CommandException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.task.TaskNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.DateStringFormater;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.Task;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskClusterImpl;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class DeleteTaskTest {
    @Test
    void testConstructorWithNull() {
        assertThrows(IllegalArgumentException.class, () -> new DeleteTask(null, new String[3]),
            "DeleteTask constructor should throw IllegalArgumentException when given null taskCluster. ");
    }

    @Test
    void testConstructorWithNullTokens() {
        assertThrows(IllegalArgumentException.class, () -> new DeleteTask(new TaskClusterImpl(), null),
            "DeleteTask constructor should throw IllegalArgumentException when given null tokens. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxTooManyParams() {
        String[] tokens = "delete-task name=nameOfTask date=10/02/2025 dueDate=15/02/2025 description=description"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new DeleteTask(new TaskClusterImpl(), tokens),
            "DeleteTask constructor should throw WrongCommandSyntax when given too many tokens. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWrongParamType() {
        String[] tokens = "delete-task name=nameOfTask collaboration=Project"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new DeleteTask(new TaskClusterImpl(), tokens),
            "DeleteTask constructor should throw WrongCommandSyntax when given parameter of wrong time. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenIsWithoutNameParam() {
        String[] tokens = "delete-task date=07/02/2025"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new DeleteTask(new TaskClusterImpl(), tokens),
            "DeleteTask constructor should throw WrongCommandSyntax when it isn't given name param. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenNameParamInNotFirst() {
        String[] tokens = "delete-task date=16/02/2025 name=nameOfTask"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new DeleteTask(new TaskClusterImpl(), tokens),
            "DeleteTask constructor should throw WrongCommandSyntax when name param isn't first. ");
    }

    @Test
    void testDeletesInboxTaskCorrectly() throws CommandException {
        TaskCluster taskCluster = new TaskClusterImpl();
        taskCluster.addTask(new Task.Builder("nameOfTask").setDescription("description").build());
        String[] tokens = "delete-task name=nameOfTask"
            .split(" ");

        try {
            new DeleteTask(taskCluster, tokens).execute();
        } catch (TaskNotFound ex) {
            fail("DeleteTask command should not throw TaskNotFound when task is in the cluster. ");
        }

        assertThrows(TaskNotFound.class, () -> taskCluster.getTask("nameOfTask"),
            "DeleteTask command should find and remove from the cluster. ");
    }

    @Test
    void testDeletesDatedTaskCorrectly() throws CommandException {
        TaskCluster taskCluster = new TaskClusterImpl();
        LocalDate date = DateStringFormater.stringToDate("10/02/2025");

        taskCluster.addTask(new Task.Builder("nameOfTask").setDate(date).setDescription("description").build());
        String[] tokens = "delete-task name=nameOfTask date=10/02/2025"
            .split(" ");

        try {
            new DeleteTask(taskCluster, tokens).execute();
        } catch (TaskNotFound ex) {
            fail("DeleteTask command should not throw TaskNotFound when task is in the cluster. ");
        }

        assertThrows(TaskNotFound.class, () -> taskCluster.getTask("nameOfTask", date),
            "DeleteTask command should find and remove from the cluster. ");
    }

    @Test
    void testThrowsTaskNotFound() throws CommandException {
        TaskCluster taskCluster = new TaskClusterImpl();
        LocalDate date = DateStringFormater.stringToDate("10/02/2025");

        String[] tokens = "delete-task name=nameOfTask date=10/02/2025"
            .split(" ");

        assertThrows(TaskNotFound.class, () -> new DeleteTask(taskCluster, tokens).execute(),
            "DeleteTask command should throw TaskNotFound when task isn't in the cluster. ");
    }

    @Test
    void testDeletesInboxTaskCorrectlyWhenThereIsDatedTaskWithTheSameName() throws CommandException {
        TaskCluster taskCluster = new TaskClusterImpl();
        taskCluster.addTask(new Task.Builder("nameOfTask").setDescription("description").build());

        LocalDate date = DateStringFormater.stringToDate("10/02/2025");
        taskCluster.addTask(new Task.Builder("nameOfTask").setDate(date).setDescription("description").build());

        String[] tokens = "delete-task name=nameOfTask"
            .split(" ");

        try {
            new DeleteTask(taskCluster, tokens).execute();
        } catch (TaskNotFound ex) {
            fail("DeleteTask command should not throw TaskNotFound when task is in the cluster. ");
        }

        assertThrows(TaskNotFound.class, () -> taskCluster.getTask("nameOfTask"),
            "DeleteTask command should find and remove inbox " +
                "when there is dated task with the same name from the cluster. ");

        try {
            taskCluster.getTask("nameOfTask", date);
        } catch (TaskNotFound ex) {
            fail("DeleteTask command should not delete dated task when it is not given date parameter. ");
        }
    }
}