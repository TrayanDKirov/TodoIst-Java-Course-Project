package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.colaborationcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.collaboration.Collaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.CommandException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.UserNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.UsernameIsTaken;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.CollaborationNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.UserIsAlreadyInTheCollaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.InMemoryTodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.UserImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddUserTest {
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
        assertThrows(IllegalArgumentException.class, () -> new AddUser(
                null, testPassword, testRepository, testTokens),
            "AddUser constructor should throw IllegalArgumentException when given null user. ");
    }

    @Test
    void testConstructorWithNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> new AddUser(
                testUser, null, testRepository, testTokens),
            "AddUser constructor should throw IllegalArgumentException when given null passwordOfUser. ");
    }

    @Test
    void testConstructorWithNullRepository() {
        assertThrows(IllegalArgumentException.class, () -> new AddUser(
                testUser, testPassword, null, testTokens),
            "AddUser constructor should throw IllegalArgumentException when given null repository. ");
    }

    @Test
    void testConstructorWithNullTokens() {
        assertThrows(IllegalArgumentException.class, () -> new AddUser(
                testUser, testPassword, testRepository, null),
            "AddUser constructor should throw IllegalArgumentException when given null tokens. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenGivenNoParams() throws CommandException {
        String[] tokens = "add-user"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new AddUser(
                testUser, testPassword, testRepository, tokens).execute(),
            "AddUser command should throw WrongCommandSyntax when given no params. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenGivenTooManyParams() throws CommandException {
        String[] tokens = "add-user collaboration=project user=ivan name=now"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new AddUser(
                testUser, testPassword, testRepository, tokens).execute(),
            "AddUser command should throw WrongCommandSyntax when given too many params. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenGivenWrongTypeParam() throws CommandException {
        String[] tokens = "add-user name=project user=user"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new AddUser(
                testUser, testPassword, testRepository, tokens).execute(),
            "AddUser command should throw WrongCommandSyntax when given wrong type param. ");
    }

    @Test
    void testWorksCorrectly() throws CommandException {
        testRepository.addCollaboration(testUser.getUsername(), testPassword, "testCollaboration");
        Collaboration collaboration = testRepository.getCollaboration(
            testUser.getUsername(), testPassword, "testCollaboration");

        User userToAdd = new UserImpl("userToAdd", "pass");
        testRepository.addUser(userToAdd);

        String[] tokens = "add-user collaboration=testCollaboration user=userToAdd"
            .split(" ");

        try {
            new AddUser(testUser, testPassword, testRepository, tokens).execute();
        } catch (CollaborationNotFound ex) {
            fail("Collaboration should be found in the repository. ");
        }  catch (UserNotFound ex) {
            fail("User to add should be found in the repository. ");
        } catch (UserIsAlreadyInTheCollaboration ex) {
            fail("User to add should not be in the collaboration. ");
        }

        assertTrue(userToAdd.hasCollaboration("testCollaboration"),
            "User to add should be in the collaboration after AddUser command. ");
        assertTrue(collaboration.isUserParticipant("userToAdd"),
            "User to add should be in the collaboration after AddUser command. ");
    }
}