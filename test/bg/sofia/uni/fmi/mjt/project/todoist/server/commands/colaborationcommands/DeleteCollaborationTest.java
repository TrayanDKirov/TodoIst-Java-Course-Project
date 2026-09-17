package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.colaborationcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.CommandException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.UsernameIsTaken;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.CollaborationCreatorException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.CollaborationNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.InMemoryTodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.UserImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeleteCollaborationTest {
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
        assertThrows(IllegalArgumentException.class, () -> new DeleteCollaboration(
                null, testPassword, testRepository, testTokens),
            "DeleteCollaboration constructor should throw IllegalArgumentException when given null user. ");
    }

    @Test
    void testConstructorWithNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> new DeleteCollaboration(
                testUser, null, testRepository, testTokens),
            "DeleteCollaboration constructor should throw IllegalArgumentException when given null passwordOfUser. ");
    }

    @Test
    void testConstructorWithNullRepository() {
        assertThrows(IllegalArgumentException.class, () -> new DeleteCollaboration(
                testUser, testPassword, null, testTokens),
            "DeleteCollaboration constructor should throw IllegalArgumentException when given null repository. ");
    }

    @Test
    void testConstructorWithNullTokens() {
        assertThrows(IllegalArgumentException.class, () -> new DeleteCollaboration(
                testUser, testPassword, testRepository, null),
            "DeleteCollaboration constructor should throw IllegalArgumentException when given null tokens. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenGivenNoParams() throws CommandException {
        String[] tokens = "delete-collaboration"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new DeleteCollaboration(
                testUser, testPassword, testRepository, tokens),
            "DeleteCollaboration command should throw WrongCommandSyntax when given no params. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenGivenTooManyParams() throws CommandException {
        String[] tokens = "delete-collaboration collaboration=project user=ivan"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new DeleteCollaboration(
                testUser, testPassword, testRepository, tokens),
            "DeleteCollaboration command should throw WrongCommandSyntax when given too many params. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenGivenWrongTypeParam() throws CommandException {
        String[] tokens = "delete-collaboration name=project"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new DeleteCollaboration(
                testUser, testPassword, testRepository, tokens),
            "DeleteCollaboration command should throw WrongCommandSyntax when given wrong type param. ");
    }

    @Test
    void testThrowsCollaborationNotFound() {
        String[] tokens = "delete-collaboration collaboration=project"
            .split(" ");

        assertThrows(CollaborationNotFound.class, () -> new DeleteCollaboration(
                testUser, testPassword, testRepository, tokens).execute(),
            "DeleteCollaboration command should throw CollaborationNotFound " +
                "when there is no collaboration in system. ");
    }

    @Test
    void testThrowsUserNotInCollaboration() throws CommandException {
        String[] tokens = "delete-collaboration collaboration=project"
            .split(" ");

        User user2 = new UserImpl("User2", testPassword);
        testRepository.addUser(user2);
        testRepository.addCollaboration(user2.getUsername(), testPassword, "project");

        assertThrows(CollaborationCreatorException.class, () -> new DeleteCollaboration(
                testUser, testPassword, testRepository, tokens).execute(),
            "DeleteCollaboration command should throw CollaborationCreatorException when user is " +
                "not the creator of the collaboration he wants to delete. ");
    }
}