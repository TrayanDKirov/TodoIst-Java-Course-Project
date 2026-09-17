package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.CommandException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskClusterImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class TaskCommandFactoryTest {
    private TaskCommandFactory taskCommandFactory = new TaskCommandFactory(new TaskClusterImpl());

    @Test
    void testCreateAddTaskCommand() {
        try {
            TaskCommand command = taskCommandFactory.of("add-task name=clean-kitchen date=07/02/2025 description=i-should-clean-the-kitchen");

            assertTrue(command instanceof AddTask,
                "TaskCommandFactory.of should return AddTaskCommand when given correct command. ");
        } catch (CommandException ex) {
            fail("TaskCommandFactory::of should not throw WrongCommandSyntax when syntax is correct. ");
        }
    }

    @Test
    void testCreateDeleteTaskCommand() {
        try {
            TaskCommand command = taskCommandFactory.of("delete-task name=testTask");

            assertTrue(command instanceof DeleteTask,
                "TaskCommandFactory.of should return DeleteTaskCommand when given correct command. ");
        } catch (CommandException ex) {
            fail();
        }
    }

    @Test
    void testCreateFinishTaskCommand() {
        try {
            TaskCommand command = taskCommandFactory.of("finish-task name=testTask");

            assertTrue(command instanceof FinishTask,
                "TaskCommandFactory.of should return UpdateTaskCommand when given correct command. ");
        } catch (CommandException ex) {
            fail("TaskCommandFactory::of should not throw WrongCommandSyntax when syntax is correct. ");
        }
    }

    @Test
    void testCreateGetTaskCommand() {
        try {
            TaskCommand command = taskCommandFactory.of("get-task name=testTask");

            assertTrue(command instanceof GetTask,
                "TaskCommandFactory.of should return GetTaskCommand when given correct command. ");
        } catch (CommandException ex) {
            fail();
        }
    }

    @Test
    void testCreateListDashBoardCommand() {
        try {
            TaskCommand command = taskCommandFactory.of("list-dashboard");

            assertTrue(command instanceof ListTasks,
                "TaskCommandFactory.of should return ListTasksCommand when given correct command. ");
        } catch (CommandException ex) {
            fail("TaskCommandFactory::of should not throw WrongCommandSyntax when syntax is correct. ");
        }
    }

    @Test
    void testCreateListTasksCommand() {
        try {
            TaskCommand command = taskCommandFactory.of("list-tasks");

            assertTrue(command instanceof ListTasks,
                "TaskCommandFactory.of should return ListTasksCommand when given correct command. ");
        } catch (WrongCommandSyntax ex) {
            fail("TaskCommandFactory::of should not throw WrongCommandSyntax when syntax is correct. ");
        }
    }

    @Test
    void testCreateUpdateTaskCommand() {
        try {
            TaskCommand command = taskCommandFactory.of("update-task name=testTask newDescription=just-a-taskCommand");

            assertTrue(command instanceof UpdateTask,
                "TaskCommandFactory.of should return UpdateTaskCommand when given correct command. ");
        } catch (CommandException ex) {
            fail("TaskCommandFactory::of should not throw WrongCommandSyntax when syntax is correct. ");
        }
    }

    @Test
    void testConstructorWithNull() {
        assertThrows(IllegalArgumentException.class, () -> new TaskCommandFactory(null),
            "TaskCommandFactory constructor should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testOfMethodWithNull() {
        assertThrows(IllegalArgumentException.class, () -> new TaskCommandFactory(new TaskClusterImpl()).of(null),
            "TaskCommandFactory::of should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenGivenWrongListDashboardCommand() {
        String wrongListDashboardCommand = "list-dashboard dueDate=02/02/25";

        assertThrows(WrongCommandSyntax.class, () -> new TaskCommandFactory(new TaskClusterImpl()).of(wrongListDashboardCommand),
            "TaskCommandFactory::of should throw WrongSyntaxCommand when given wrong list-dashboard command. ");
    }
}