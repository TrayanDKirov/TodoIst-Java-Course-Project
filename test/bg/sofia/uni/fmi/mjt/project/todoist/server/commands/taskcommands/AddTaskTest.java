package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.CommandException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.task.TaskAlreadyExists;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.task.TaskNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.task.TaskParameterNotSet;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.DateStringFormater;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.Task;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskClusterImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class AddTaskTest {
    @Test
    void testConstructorWithNullTaskCluster() {
        assertThrows(IllegalArgumentException.class, () -> new AddTask(null, new String[1]),
            "AddTask constructor should throw IllegalArgumentException when given null taskCluster. ");
    }

    @Test
    void testConstructorWithNullTokens() {
        assertThrows(IllegalArgumentException.class, () -> new AddTask(new TaskClusterImpl(), null),
            "AddTask constructor should throw IllegalArgumentException when given null tokens. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxTooManyParams() {
        String[] tokens = "add-task name=nameOfTask date=10/02/2025 dueDate=15/02/2025 description=description date=12/02/2025"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new AddTask(new TaskClusterImpl(), tokens),
            "AddTask constructor should throw WrongCommandSyntax when given too many tokens. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWrongParamType() {
        String[] tokens = "add-task name=nameOfTask collaboration=Project dueDate=15/02/2025 description=description"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new AddTask(new TaskClusterImpl(), tokens),
            "AddTask constructor should throw WrongCommandSyntax when given parameter of wrong time. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenIsWithoutNameParam() {
        String[] tokens = "add-task date=Project dueDate=15/02/2025 description=description"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new AddTask(new TaskClusterImpl(), tokens),
            "AddTask constructor should throw WrongCommandSyntax when it isn't given name param. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenNameParamInNotFirst() {
        String[] tokens = "add-task date=16/02/2025 dueDate=15/02/2025 name=nameOfTask description=description"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new AddTask(new TaskClusterImpl(), tokens),
            "AddTask constructor should throw WrongCommandSyntax when name param isn't first. ");
    }

    @Test
    void testAddsTaskWithCorrectDescription() throws CommandException {
        String[] tokens = "add-task name=nameOfTask date=16/02/2025 dueDate=15/02/2025 description=description"
            .split(" ");

        TaskCluster taskCluster = new TaskClusterImpl();
        new AddTask(taskCluster, tokens).execute();

        try {
            Task task = taskCluster.getTask("nameOfTask", DateStringFormater.stringToDate("16/02/2025"));

            assertEquals("description", task.getDescription(),
                "Tasks description is not correct after adding it to the system. ");
        } catch (TaskNotFound ex) {
            fail("Task should be added correctly. But isn't found. ");
        } catch (TaskParameterNotSet ex) {
            fail("Task should be added correctly. But it's description parameter is not set. ");
        }
    }

    @Test
    void testAddsTaskWithCorrectDueDate() throws CommandException {
        String[] tokens = "add-task name=nameOfTask date=16/02/2025 dueDate=15/02/2025 description=description"
            .split(" ");

        TaskCluster taskCluster = new TaskClusterImpl();
        new AddTask(taskCluster, tokens).execute();

        try {
            Task task = taskCluster.getTask("nameOfTask", DateStringFormater.stringToDate("16/02/2025"));

            assertEquals(DateStringFormater.stringToDate("15/02/2025"), task.getDueDate(),
                "AddTask constructor should throw WrongCommandSyntax when name param isn't first. ");
        } catch (TaskNotFound ex) {
            fail("Task should be added correctly. But isn't found. ");
        } catch (TaskParameterNotSet ex) {
            fail("Task should be added correctly. But it's due date parameter is not set. ");
        }
    }

    @Test
    void testAddsTaskInInboxIsDescriptionCorrect() throws CommandException {
        String[] tokens = "add-task name=nameOfTask dueDate=15/02/2025 description=description"
            .split(" ");

        TaskCluster taskCluster = new TaskClusterImpl();
        new AddTask(taskCluster, tokens).execute();

        try {
            Task task = taskCluster.getTask("nameOfTask");

            assertEquals("description", task.getDescription(),
                "Tasks description is not correct after adding it to the system. ");
        } catch (TaskNotFound ex) {
            fail("Task should be added correctly. But isn't found. ");
        } catch (TaskParameterNotSet ex) {
            fail("Task should be added correctly. But description. ");
        }
    }

    @Test
    void testAddTwoTasksWithSameName() throws CommandException {
        String[] tokens = "add-task name=nameOfTask dueDate=15/02/2025 description=description"
            .split(" ");
        String[] tokens2 = "add-task name=nameOfTask dueDate=17/02/2025 "
            .split(" ");

        TaskCluster taskCluster = new TaskClusterImpl();
        new AddTask(taskCluster, tokens).execute();
        assertThrows(TaskAlreadyExists.class, () -> new AddTask(taskCluster, tokens2).execute(),
            "Command add-task should throw TaskAlreadyExists when you try to add two task with the same name. ");
    }

    @Test
    void testAddTwoTasksWithSameNameAndDate() throws CommandException {
        String[] tokens = "add-task name=nameOfTask date=12/02/2025 dueDate=15/02/2025 description=description"
            .split(" ");
        String[] tokens2 = "add-task name=nameOfTask description=description-of-second-task date=12/02/2025"
            .split(" ");

        TaskCluster taskCluster = new TaskClusterImpl();
        new AddTask(taskCluster, tokens).execute();
        assertThrows(TaskAlreadyExists.class, () -> new AddTask(taskCluster, tokens2).execute(),
            "Command add-task should throw TaskAlreadyExists when you try to add two task with the same name and date. ");
    }
}