package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.CommandException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.task.TaskAlreadyExists;
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

class GetTaskTest {
    @Test
    void testConstructorWithNullTaskCluster() {
        assertThrows(IllegalArgumentException.class, () -> new GetTask(null, new String[3]),
            "GetTask constructor should throw IllegalArgumentException when given null taskCluster. ");
    }

    @Test
    void testConstructorWithNullTokens() {
        assertThrows(IllegalArgumentException.class, () -> new GetTask(new TaskClusterImpl(), null),
            "GetTask constructor should throw IllegalArgumentException when given null tokens. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxTooManyParams() {
        String[] tokens = "get-task name=nameOfTask date=10/02/2025 dueDate=15/02/2025 description=description"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new GetTask(new TaskClusterImpl(), tokens),
            "GetTask constructor should throw WrongCommandSyntax when given too many tokens. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWrongParamType() {
        String[] tokens = "get-task name=nameOfTask collaboration=Project"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new GetTask(new TaskClusterImpl(), tokens),
            "GetTask constructor should throw WrongCommandSyntax when given parameter of wrong time. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenIsWithoutNameParam() {
        String[] tokens = "get-task date=07/02/2025"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new GetTask(new TaskClusterImpl(), tokens),
            "GetTask constructor should throw WrongCommandSyntax when it isn't given name param. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenNameParamInNotFirst() {
        String[] tokens = "get-task date=16/02/2025 name=nameOfTask"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new GetTask(new TaskClusterImpl(), tokens),
            "GetTask constructor should throw WrongCommandSyntax when name param isn't first. ");
    }

    @Test
    void testGivesCorrectInboxTaskInfo() throws TaskAlreadyExists {
        TaskCluster taskCluster = new TaskClusterImpl();
        taskCluster.addTask(new Task.Builder("nameOfTask").setDescription("description").build());
        String[] tokens = "get-task name=nameOfTask"
            .split(" ");

        String output = "";
        try {
            output = new GetTask(taskCluster, tokens).execute();
        } catch (WrongCommandSyntax ex) {
            fail("GetTask constructor should not throw WrongCommandSyntax syntax is right");
        } catch (TaskNotFound ex) {
            fail("GetTask command should not throw TaskNotFound when task is in the taskCluster. ");
        }

        String expected = "Task found. Here is it:" + System.lineSeparator() +
            "Name: nameOfTask Completed: false" + System.lineSeparator() +
            "Date: none" + System.lineSeparator() +
            "Due date: none" + System.lineSeparator() +
            "Description: description" + System.lineSeparator() +
            "Assignee: none";
        assertEquals(expected, output,
            "Output of GetTask command for inbox task is incorrect");
    }

    @Test
    void testGivesCorrectDatedTaskInfo() throws CommandException {
        TaskCluster taskCluster = new TaskClusterImpl();

        LocalDate date = DateStringFormater.stringToDate("17/02/2025");
        taskCluster.addTask(new Task.Builder("nameOfTask").setDate(date).setDescription("description").build());
        String[] tokens = "get-task name=nameOfTask date=17/02/2025"
            .split(" ");

        String output = "";
        try {
            output = new GetTask(taskCluster, tokens).execute();
        } catch (WrongCommandSyntax ex) {
            fail("GetTask constructor should not throw WrongCommandSyntax syntax when is right");
        } catch (TaskNotFound ex) {
            fail("GetTask command should not throw TaskNotFound when task is in the taskCluster. ");
        }

        String expected = "Task found. Here is it:" + System.lineSeparator() +
            "Name: nameOfTask Completed: false" + System.lineSeparator() +
            "Date: 17/02/2025" + System.lineSeparator() +
            "Due date: none" + System.lineSeparator() +
            "Description: description" + System.lineSeparator() +
            "Assignee: none";
        assertEquals(expected, output,
            "Output of GetTask command for dated task is incorrect");
    }
}