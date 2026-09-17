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

class ListCollaborationsTest {
    private static final String testPassword = "testPassword";
    private static final User testUser = new UserImpl("testUser", testPassword);
    private static final TodoIstRepository testRepository = new InMemoryTodoIstRepository();
    private static final String[] testTokens = new String[1];

    static {
        try {
            testRepository.addUser(testUser);
        } catch (UsernameIsTaken e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testConstructorWithNullUser() {
        assertThrows(IllegalArgumentException.class, () -> new ListCollaborations(
                null, testPassword, testRepository, testTokens),
            "ListCollaborations constructor should throw IllegalArgumentException when given null user. ");
    }

    @Test
    void testConstructorWithNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> new ListCollaborations(
                testUser, null, testRepository, testTokens),
            "ListCollaborations constructor should throw IllegalArgumentException when given null passwordOfUser. ");
    }

    @Test
    void testConstructorWithNullRepository() {
        assertThrows(IllegalArgumentException.class, () -> new ListCollaborations(
                testUser, testPassword, null, testTokens),
            "ListCollaborations constructor should throw IllegalArgumentException when given null repository. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenGivenParams() {
        String[] tokens = "list-collaborations collaboration=project"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new ListCollaborations(
                testUser, testPassword, testRepository, tokens),
            "ListCollaborations constructor should throw WrongCommandSyntax when given parameters. ");
    }

    @Test
    void testListsCollaborationCorrectly() throws CommandException {
        testRepository.addCollaboration(testUser.getUsername(), testPassword, "listedProject");

        String[] tokens = "list-collaborations"
            .split(" ");

        String output = new ListCollaborations(testUser, testPassword, testRepository, tokens).execute();

        assertTrue(output.contains("listedProject"),
            "ListCollaborations command output should contain the name of the collaboration user is part of. ");
    }
}
