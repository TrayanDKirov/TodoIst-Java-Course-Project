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

class ListTasksTest {
    @Test
    void testTokensConstructorWithNullTaskCluster() {
        assertThrows(IllegalArgumentException.class, () -> new ListTasks(null, new String[3]),
            "ListTasks constructor should throw IllegalArgumentException when given null taskCluster. ");
    }

    @Test
    void testDateConstructorWithNullTaskCluster() {
        assertThrows(IllegalArgumentException.class, () -> new ListTasks(null, LocalDate.now()),
            "ListTasks constructor should throw IllegalArgumentException when given null taskCluster. ");
    }

    @Test
    void testConstructorWithNullDate() {
        LocalDate nullDate = null;
        assertThrows(IllegalArgumentException.class, () -> new ListTasks(new TaskClusterImpl(), nullDate),
            "ListTasks constructor should throw IllegalArgumentException when given null tokens. ");
    }

    @Test
    void testConstructorWithNullTokens() {
        String[] nullTokens = null;
        assertThrows(IllegalArgumentException.class, () -> new ListTasks(new TaskClusterImpl(), nullTokens),
            "ListTasks constructor should throw IllegalArgumentException when given null tokens. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxTooManyParams() {
        String[] tokens = "list-tasks date=10/02/2025 dueDate=15/02/2025"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new ListTasks(new TaskClusterImpl(), tokens),
            "ListTasks constructor should throw WrongCommandSyntax when given too many tokens. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWrongParamType() {
        String[] tokens = "list-tasks name=nameOfTask"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new ListTasks(new TaskClusterImpl(), tokens),
            "ListTasks constructor should throw WrongCommandSyntax when given parameter of wrong time. ");
    }

    @Test
    void testListsCorrectInboxTasksInfo() throws CommandException {
        TaskCluster taskCluster = new TaskClusterImpl();

        LocalDate dueDate = DateStringFormater.stringToDate("10/02/2025");
        taskCluster.addTask(new Task.Builder("task1").setDueDate(dueDate)
            .setDescription("this-is-task2").build());
        taskCluster.addTask(new Task.Builder("task2").setDescription("this-is-task2").build());

        String[] tokens = "list-tasks"
            .split(" ");

        String output = "";
        try {
            output = new ListTasks(taskCluster, tokens).execute();
        } catch (WrongCommandSyntax ex) {
            fail("ListTasks constructor should not throw WrongCommandSyntax syntax is right");
        }

        String expected = "Tasks from inbox: " +
            System.lineSeparator() + System.lineSeparator() +
            "Name: task1 Completed: false" + System.lineSeparator() +
            "Date: none" + System.lineSeparator() +
            "Due date: 10/02/2025" + System.lineSeparator() +
            "Description: this-is-task2" + System.lineSeparator() +
            "Assignee: none" + System.lineSeparator() +
            System.lineSeparator() +
            "Name: task2 Completed: false" + System.lineSeparator() +
            "Date: none" + System.lineSeparator() +
            "Due date: none" + System.lineSeparator() +
            "Description: this-is-task2" + System.lineSeparator() +
            "Assignee: none";
        assertEquals(expected, output,
            "Output of ListTasks command for inbox task is incorrect");
    }

    @Test
    void testListsCorrectDatedTasksInfo() throws CommandException {
        TaskCluster taskCluster = new TaskClusterImpl();

        LocalDate date = DateStringFormater.stringToDate("10/02/2025");
        LocalDate dueDate = DateStringFormater.stringToDate("10/02/2025");
        taskCluster.addTask(new Task.Builder("task1").setDate(date).setDueDate(dueDate)
            .setDescription("this-is-task2").build());
        taskCluster.addTask(new Task.Builder("task2").setDate(date).setDescription("this-is-task2").build());

        String[] tokens = "list-tasks date=10/02/2025"
            .split(" ");

        String output = "";
        try {
            output = new ListTasks(taskCluster, tokens).execute();
        } catch (WrongCommandSyntax ex) {
            fail("ListTasks constructor should not throw WrongCommandSyntax syntax when is right");
        }

        String expected = "Tasks for date - 10/02/2025. " +
            System.lineSeparator() + System.lineSeparator() +
            "Name: task1 Completed: false" + System.lineSeparator() +
            "Date: 10/02/2025" + System.lineSeparator() +
            "Due date: 10/02/2025" + System.lineSeparator() +
            "Description: this-is-task2" + System.lineSeparator() +
            "Assignee: none" + System.lineSeparator() +
            System.lineSeparator() +
            "Name: task2 Completed: false" + System.lineSeparator() +
            "Date: 10/02/2025" + System.lineSeparator() +
            "Due date: none" + System.lineSeparator() +
            "Description: this-is-task2" + System.lineSeparator() +
            "Assignee: none";
        assertEquals(expected, output,
            "Output of ListTasks command for inbox task is incorrect");
    }
}