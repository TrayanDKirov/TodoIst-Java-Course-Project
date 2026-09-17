package bg.sofia.uni.fmi.mjt.project.todoist.server.commands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.accountcommand.WrongCommand;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.colaborationcommands.AddCollaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands.AddTask;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands.ListTasks;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.CommandException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.UsernameIsTaken;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.InMemoryTodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.UserImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandFactoryTest {
    private static final String testPassword = "testPassword";
    private static final User testUser = new UserImpl("testUser", testPassword);
    private static final TodoIstRepository testRepository = new InMemoryTodoIstRepository();

    private final CommandFactory factory = new CommandFactory(testUser, testPassword, testRepository);

    static {
        try {
            testRepository.addUser(testUser);
        } catch (UsernameIsTaken e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testConstructorWithNullUser() {
        assertThrows(IllegalArgumentException.class, () -> new CommandFactory(null, testPassword, testRepository),
            "CommandFactory constructor should throw IllegalArgumentException when given null user. ");
    }

    @Test
    void testConstructorWithNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> new CommandFactory(testUser, null, testRepository),
            "CommandFactory constructor should throw IllegalArgumentException when given null passwordOfUser. ");
    }

    @Test
    void testConstructorWithNullRepository() {
        assertThrows(IllegalArgumentException.class, () -> new CommandFactory(testUser, testPassword, null),
            "CommandFactory constructor should throw IllegalArgumentException when given null repository. ");
    }

    @Test
    void testOfWithNull() {
        assertThrows(IllegalArgumentException.class, () -> factory.of(null),
            "CommandFactory::of should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testCreatesAddTaskCommand() throws CommandException {
        assertTrue(factory.of("add-task name=cleanKitchen") instanceof AddTask,
            "CommandFactory::of should return AddTask when given correct add-task command. ");
    }

    @Test
    void testCreatesListTasksCommand() throws CommandException {
        assertTrue(factory.of("list-tasks") instanceof ListTasks,
            "CommandFactory::of should return ListTasks when given correct list-tasks command. ");
    }

    @Test
    void testCreatesAddCollaborationCommand() throws CommandException {
        assertTrue(factory.of("add-collaboration collaboration=factoryProject") instanceof AddCollaboration,
            "CommandFactory::of should return AddCollaboration when given correct add-collaboration command. ");
    }

    @Test
    void testCreatesHelpCommand() throws CommandException {
        assertTrue(factory.of("help") instanceof HelpCommand,
            "CommandFactory::of should return HelpCommand when given correct help command. ");
    }

    @Test
    void testCreatesWrongCommand() throws CommandException {
        assertTrue(factory.of("fly-to-moon") instanceof WrongCommand,
            "CommandFactory::of should return WrongCommand when given unknown command. ");
    }

    @Test
    void testListTasksWithCollaborationFlag() throws CommandException {
        testRepository.addCollaboration(testUser.getUsername(), testPassword, "factoryCollabProject");

        assertTrue(factory.of("list-tasks collaboration=factoryCollabProject") instanceof ListTasks,
            "CommandFactory::of should route list-tasks with collaboration flag to the collaboration's tasks. ");
    }
}
