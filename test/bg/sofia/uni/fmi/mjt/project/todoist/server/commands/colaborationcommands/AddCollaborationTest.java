package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.colaborationcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.collaboration.Collaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.CommandException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.UsernameIsTaken;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.CollaborationNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.UserNotInCollaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.InMemoryTodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.UserImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class AddCollaborationTest {
    private static String testPassword = "testPassword";
    private static User testUser = new UserImpl("testUser", testPassword);
    private static TodoIstRepository testRepository = new InMemoryTodoIstRepository();
    private static String[] testTokens = new String[2];

    static {
        try {
            testRepository.addUser(testUser);
        } catch (UsernameIsTaken e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testConstructorWithNullUser() {
        assertThrows(IllegalArgumentException.class, () -> new AddCollaboration(
            null, testPassword, testRepository, testTokens),
            "AddCollaboration constructor should throw IllegalArgumentException when given null user. ");
    }

    @Test
    void testConstructorWithNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> new AddCollaboration(
            testUser, null, testRepository, testTokens),
            "AddCollaboration constructor should throw IllegalArgumentException when given null passwordOfUser. ");
    }

    @Test
    void testConstructorWithNullRepository() {
        assertThrows(IllegalArgumentException.class, () -> new AddCollaboration(
            testUser, testPassword, null, testTokens),
            "AddCollaboration constructor should throw IllegalArgumentException when given null repository. ");
    }

    @Test
    void testConstructorWithNullTokens() {
        assertThrows(IllegalArgumentException.class, () -> new AddCollaboration(
            testUser, testPassword, testRepository, null),
            "AddCollaboration constructor should throw IllegalArgumentException when given null tokens. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenGivenNoParams() throws CommandException {
        String[] tokens = "add-collaboration"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new AddCollaboration(
            testUser, testPassword, testRepository, tokens).execute(),
            "AddCollaboration command should throw WrongCommandSyntax when given no params. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenGivenTooManyParams() throws CommandException {
        String[] tokens = "add-collaboration collaboration=project user=ivan"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new AddCollaboration(
                testUser, testPassword, testRepository, tokens).execute(),
            "AddCollaboration command should throw WrongCommandSyntax when given too many params. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenGivenWrongTypeParam() throws CommandException {
        String[] tokens = "add-collaboration name=project"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new AddCollaboration(
                testUser, testPassword, testRepository, tokens).execute(),
            "AddCollaboration command should throw WrongCommandSyntax when given wrong type param. ");
    }

    @Test
    void testAddsCollaborationCorrectly() throws CommandException {
        String[] tokens = "add-collaboration collaboration=project"
            .split(" ");

        new AddCollaboration(testUser, testPassword, testRepository, tokens).execute();
        Collaboration collaboration;
        try {
            collaboration = testRepository.getCollaboration(
                testUser.getUsername(), testPassword, "project");

            assertEquals("project", collaboration.getName(),
                "AddCollaboration command should add collaboration with the given name. ");

            assertTrue(collaboration.isUserParticipant(testUser.getUsername()),
                "AddCollaboration command should add collaboration with and the user should be a participant. ");

            assertEquals(testUser.getUsername(), collaboration.getCreator(),
                "AddCollaboration command should add collaboration with and the user should be a the creator. ");

            assertTrue(testUser.hasCollaboration("project"),
                "AddCollaboration command should add collaboration with and the user should be a the creator. ");
        } catch (CollaborationNotFound e) {
            fail("Collaboration should be in repository after AddCollaboration command. ");
        } catch (UserNotInCollaboration e) {
            fail("User should be in the collaboration after AddCollaboration command. ");
        }
    }
}