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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class UpdateTaskTest {
    @Test
    void testConstructorWithNullTaskCluster() {
        assertThrows(IllegalArgumentException.class, () -> new UpdateTask(null, new String[4]),
            "UpdateTask constructor should throw IllegalArgumentException when given null taskCluster. ");
    }

    @Test
    void testConstructorWithNullTokens() {
        assertThrows(IllegalArgumentException.class, () -> new UpdateTask(new TaskClusterImpl(), null),
            "UpdateTask constructor should throw IllegalArgumentException when given null tokens. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxTooManyParams() {
        String[] tokens = ("update-task name=nameOfTask date=10/02/2025 newDate=12/02/2025 " +
            "newDueDate=15/02/2025 newDescription=description collaboration=project")
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new UpdateTask(new TaskClusterImpl(), tokens),
            "UpdateTask constructor should throw WrongCommandSyntax when given too many tokens. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWrongParamType() {
        String[] tokens = ("update-task name=nameOfTask date=10/02/2025 collaboration=project " +
            "newDueDate=15/02/2025 newDescription=description")
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new UpdateTask(new TaskClusterImpl(), tokens),
            "UpdateTask constructor should throw WrongCommandSyntax when given parameter of wrong type. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenIsWithoutNameParam() {
        String[] tokens = ("update-task date=10/02/2025" +
            "newDueDate=15/02/2025 newDescription=description")
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new UpdateTask(new TaskClusterImpl(), tokens),
            "UpdateTask constructor should throw WrongCommandSyntax when it isn't given name param. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenNameParamInNotFirst() {
        String[] tokens = ("update-task date=10/02/2025 newDate=12/02/2025 " +
            "newDueDate=15/02/2025 name=nameOfTask newDescription=description")
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new UpdateTask(new TaskClusterImpl(), tokens),
            "UpdateTask constructor should throw WrongCommandSyntax when name param isn't first. ");
    }

    @Test
    void testUpdatesDescriptionDueDateOfInboxTask() throws CommandException {
        TaskCluster taskCluster = new TaskClusterImpl();

        LocalDate dueDate = DateStringFormater.stringToDate("14/02/2025");
        taskCluster.addTask(new Task.Builder("nameOfTask").setDescription("description").setDueDate(dueDate).build());

        String[] tokens = ("update-task name=nameOfTask " +
            "newDueDate=15/02/2025 newDescription=newDescription")
            .split(" ");

        try {
            new UpdateTask(taskCluster, tokens).execute();
        } catch (TaskNotFound ex) {
            fail("UpdateTask command should not throw TaskNotFound when inbox task is in taskCluster. ");
        }

        assertEquals("newDescription", taskCluster.getTask("nameOfTask").getDescription(),
            "Description should be updated after UpdateTask command");

        assertEquals(DateStringFormater.stringToDate("15/02/2025"), taskCluster.getTask("nameOfTask").getDueDate(),
            "DueDate should be updated after UpdateTask command. ");
    }

    @Test
    void testUpdatesDescriptionDueDateOfDatedTask() throws CommandException {
        TaskCluster taskCluster = new TaskClusterImpl();

        LocalDate date = DateStringFormater.stringToDate("13/02/2025");
        LocalDate dueDate = DateStringFormater.stringToDate("14/02/2025");
        taskCluster.addTask(new Task.Builder("nameOfTask").setDate(date)
            .setDescription("description").setDueDate(dueDate).build());

        String[] tokens = ("update-task name=nameOfTask date=13/02/2025 " +
            "newDueDate=15/02/2025 newDescription=newDescription")
            .split(" ");

        try {
            new UpdateTask(taskCluster, tokens).execute();
        } catch (TaskNotFound ex) {
            fail("UpdateTask command should not throw TaskNotFound when inbox task is in taskCluster. ");
        }

        assertEquals("newDescription", taskCluster.getTask("nameOfTask", date).getDescription(),
            "Description should be updated after UpdateTask command");

        assertEquals(DateStringFormater.stringToDate("15/02/2025"), taskCluster.getTask("nameOfTask", date).getDueDate(),
            "DueDate should be updated after UpdateTask command. ");
    }

    @Test
    void testUpdateInboxToDatedTask() throws CommandException {
        TaskCluster taskCluster = new TaskClusterImpl();

        LocalDate newDate = DateStringFormater.stringToDate("13/02/2025");
        LocalDate dueDate = DateStringFormater.stringToDate("14/02/2025");
        taskCluster.addTask(new Task.Builder("nameOfTask")
            .setDescription("description").setDueDate(dueDate).build());

        String[] tokens = ("update-task name=nameOfTask newDate=13/02/2025 " +
            "newDueDate=15/02/2025 newDescription=newDescription")
            .split(" ");

        try {
            new UpdateTask(taskCluster, tokens).execute();
        } catch (TaskNotFound ex) {
            fail("UpdateTask command should not throw TaskNotFound when inbox task is in taskCluster. ");
        }

        assertEquals("newDescription", taskCluster.getTask("nameOfTask", newDate).getDescription(),
            "Description should be updated after UpdateTask command");

        assertEquals(DateStringFormater.stringToDate("15/02/2025"), taskCluster.getTask("nameOfTask", newDate).getDueDate(),
            "DueDate should be updated after UpdateTask command. ");
    }
}