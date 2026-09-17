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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class FinishTaskTest {
    @Test
    void testConstructorWithNullTaskCluster() {
        assertThrows(IllegalArgumentException.class, () -> new FinishTask(null, new String[3]),
            "FinishTask constructor should throw IllegalArgumentException when given null taskCluster. ");
    }

    @Test
    void testConstructorWithNullTokens() {
        assertThrows(IllegalArgumentException.class, () -> new FinishTask(new TaskClusterImpl(), null),
            "FinishTask constructor should throw IllegalArgumentException when given null tokens. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxTooManyParams() {
        String[] tokens = "finish-task name=nameOfTask date=10/02/2025 dueDate=15/02/2025 description=description"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new FinishTask(new TaskClusterImpl(), tokens),
            "FinishTask constructor should throw WrongCommandSyntax when given too many tokens. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWrongParamType() {
        String[] tokens = "finish-task name=nameOfTask collaboration=Project"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new FinishTask(new TaskClusterImpl(), tokens),
            "FinishTask constructor should throw WrongCommandSyntax when given parameter of wrong time. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenIsWithoutNameParam() {
        String[] tokens = "finish-task date=07/02/2025"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new FinishTask(new TaskClusterImpl(), tokens),
            "FinishTask constructor should throw WrongCommandSyntax when it isn't given name param. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenNameParamInNotFirst() {
        String[] tokens = "finish-task date=16/02/2025 name=nameOfTask"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new FinishTask(new TaskClusterImpl(), tokens),
            "FinishTask constructor should throw WrongCommandSyntax when name param isn't first. ");
    }

    @Test
    void testCompletesCorrectInboxTaskInfo() throws CommandException {
        TaskCluster taskCluster = new TaskClusterImpl();
        taskCluster.addTask(new Task.Builder("nameOfTask").setDescription("description").build());
        String[] tokens = "finish-task name=nameOfTask"
            .split(" ");

        try {
            new FinishTask(taskCluster, tokens).execute();
        } catch (WrongCommandSyntax ex) {
            fail("FinishTask constructor should not throw WrongCommandSyntax syntax is right");
        } catch (TaskNotFound ex) {
            fail("FinishTask command should not throw TaskNotFound when task is in the taskCluster. ");
        }

        assertTrue(taskCluster.getTask("nameOfTask").isCompleted(),
            "FinishTask command should mark inbox task completed. ");
    }

    @Test
    void testGivesCorrectDatedTaskInfo() throws CommandException {
        TaskCluster taskCluster = new TaskClusterImpl();

        LocalDate date = DateStringFormater.stringToDate("17/02/2025");
        taskCluster.addTask(new Task.Builder("nameOfTask").setDate(date).setDescription("description").build());
        String[] tokens = "finish-task name=nameOfTask date=17/02/2025"
            .split(" ");

        try {
            new FinishTask(taskCluster, tokens).execute();
        } catch (WrongCommandSyntax ex) {
            fail("FinishTask constructor should not throw WrongCommandSyntax syntax is right");
        } catch (TaskNotFound ex) {
            fail("FinishTask command should not throw TaskNotFound when task is in the taskCluster. ");
        }

        assertTrue(taskCluster.getTask("nameOfTask", date).isCompleted(),
            "FinishTask command should mark dated task completed. ");
    }
}