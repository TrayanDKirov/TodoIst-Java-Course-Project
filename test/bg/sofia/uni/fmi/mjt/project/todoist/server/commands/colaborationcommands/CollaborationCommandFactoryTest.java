package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.colaborationcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.CommandException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.InMemoryTodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.UserImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollaborationCommandFactoryTest {
    private static final String testPassword = "testPassword";
    private static final User testUser = new UserImpl("testUser", testPassword);
    private static final TodoIstRepository testRepository = new InMemoryTodoIstRepository();

    private final CollaborationCommandFactory factory =
        new CollaborationCommandFactory(testUser, testPassword, testRepository);

    @Test
    void testConstructorWithNullUser() {
        assertThrows(IllegalArgumentException.class, () -> new CollaborationCommandFactory(
                null, testPassword, testRepository),
            "CollaborationCommandFactory constructor should throw IllegalArgumentException when given null user. ");
    }

    @Test
    void testConstructorWithNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> new CollaborationCommandFactory(
                testUser, null, testRepository),
            "CollaborationCommandFactory constructor should throw IllegalArgumentException when given null password. ");
    }

    @Test
    void testConstructorWithNullRepository() {
        assertThrows(IllegalArgumentException.class, () -> new CollaborationCommandFactory(
                testUser, testPassword, null),
            "CollaborationCommandFactory constructor should throw IllegalArgumentException when given null repository. ");
    }

    @Test
    void testOfWithNull() {
        assertThrows(IllegalArgumentException.class, () -> factory.of(null),
            "CollaborationCommandFactory::of should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testCreatesAddCollaborationCommand() throws CommandException {
        assertTrue(factory.of("add-collaboration collaboration=factoryProject") instanceof AddCollaboration,
            "CollaborationCommandFactory::of should return AddCollaboration when given correct command. ");
    }

    @Test
    void testCreatesListCollaborationsCommand() throws CommandException {
        assertTrue(factory.of("list-collaborations") instanceof ListCollaborations,
            "CollaborationCommandFactory::of should return ListCollaborations when given correct command. ");
    }

    @Test
    void testCreatesListUsersCommand() throws CommandException {
        assertTrue(factory.of("list-users collaboration=factoryProject") instanceof ListUsers,
            "CollaborationCommandFactory::of should return ListUsers when given correct command. ");
    }
}
