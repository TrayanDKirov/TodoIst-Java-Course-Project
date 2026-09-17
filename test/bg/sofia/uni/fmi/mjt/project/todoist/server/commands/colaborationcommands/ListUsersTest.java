package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.colaborationcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.CommandException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.UsernameIsTaken;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.InMemoryTodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.UserImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListUsersTest {
    private static final String testPassword = "testPassword";
    private static final User testUser = new UserImpl("testUser", testPassword);
    private static final TodoIstRepository testRepository = new InMemoryTodoIstRepository();
    private static final String[] testTokens = new String[2];

    static {
        try {
            testRepository.addUser(testUser);
        } catch (UsernameIsTaken e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testConstructorWithNullUser() {
        assertThrows(IllegalArgumentException.class, () -> new ListUsers(
                null, testPassword, testRepository, testTokens),
            "ListUsers constructor should throw IllegalArgumentException when given null user. ");
    }

    @Test
    void testConstructorWithNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> new ListUsers(
                testUser, null, testRepository, testTokens),
            "ListUsers constructor should throw IllegalArgumentException when given null passwordOfUser. ");
    }

    @Test
    void testConstructorWithNullRepository() {
        assertThrows(IllegalArgumentException.class, () -> new ListUsers(
                testUser, testPassword, null, testTokens),
            "ListUsers constructor should throw IllegalArgumentException when given null repository. ");
    }

    @Test
    void testConstructorWithNullTokens() {
        assertThrows(IllegalArgumentException.class, () -> new ListUsers(
                testUser, testPassword, testRepository, null),
            "ListUsers constructor should throw IllegalArgumentException when given null tokens. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenGivenNoParams() {
        String[] tokens = "list-users"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new ListUsers(
                testUser, testPassword, testRepository, tokens),
            "ListUsers constructor should throw WrongCommandSyntax when given no params. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenGivenWrongTypeParam() {
        String[] tokens = "list-users name=project"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new ListUsers(
                testUser, testPassword, testRepository, tokens),
            "ListUsers constructor should throw WrongCommandSyntax when given wrong type param. ");
    }

    @Test
    void testListsParticipantsCorrectly() throws CommandException {
        testRepository.addCollaboration(testUser.getUsername(), testPassword, "usersProject");

        String[] tokens = "list-users collaboration=usersProject"
            .split(" ");

        String output = new ListUsers(testUser, testPassword, testRepository, tokens).execute();

        assertTrue(output.contains(testUser.getUsername()),
            "ListUsers command output should contain the creator as a participant. ");
    }
}
