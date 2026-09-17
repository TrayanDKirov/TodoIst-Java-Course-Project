package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.colaborationcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.collaboration.Collaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.CommandException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.UsernameIsTaken;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.UserNotInCollaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.Task;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.InMemoryTodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.UserImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AssignTaskTest {
    private static final String testPassword = "testPassword";
    private static final User testUser = new UserImpl("testUser", testPassword);
    private static final TodoIstRepository testRepository = new InMemoryTodoIstRepository();
    private static final String[] testTokens = new String[4];

    static {
        try {
            testRepository.addUser(testUser);
        } catch (UsernameIsTaken e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testConstructorWithNullUser() {
        assertThrows(IllegalArgumentException.class, () -> new AssignTask(
                null, testPassword, testRepository, testTokens),
            "AssignTask constructor should throw IllegalArgumentException when given null user. ");
    }

    @Test
    void testConstructorWithNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> new AssignTask(
                testUser, null, testRepository, testTokens),
            "AssignTask constructor should throw IllegalArgumentException when given null passwordOfUser. ");
    }

    @Test
    void testConstructorWithNullRepository() {
        assertThrows(IllegalArgumentException.class, () -> new AssignTask(
                testUser, testPassword, null, testTokens),
            "AssignTask constructor should throw IllegalArgumentException when given null repository. ");
    }

    @Test
    void testConstructorWithNullTokens() {
        assertThrows(IllegalArgumentException.class, () -> new AssignTask(
                testUser, testPassword, testRepository, null),
            "AssignTask constructor should throw IllegalArgumentException when given null tokens. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenGivenTooFewParams() {
        String[] tokens = "assign-task collaboration=project user=ivan"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new AssignTask(
                testUser, testPassword, testRepository, tokens),
            "AssignTask constructor should throw WrongCommandSyntax when given too few params. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenGivenWrongTypeParam() {
        String[] tokens = "assign-task name=project user=ivan task=clean"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new AssignTask(
                testUser, testPassword, testRepository, tokens),
            "AssignTask constructor should throw WrongCommandSyntax when given wrong type param. ");
    }

    @Test
    void testAssignsTaskCorrectly() throws CommandException {
        testRepository.addCollaboration(testUser.getUsername(), testPassword, "assignProject");
        Collaboration collaboration = testRepository.getCollaboration(
            testUser.getUsername(), testPassword, "assignProject");
        collaboration.getTasks().addTask(new Task.Builder("cleanKitchen").build());

        String[] tokens = "assign-task collaboration=assignProject user=testUser task=cleanKitchen"
            .split(" ");

        new AssignTask(testUser, testPassword, testRepository, tokens).execute();

        assertEquals("testUser", collaboration.getTasks().getTask("cleanKitchen").getAssignee(),
            "AssignTask command should set the assignee of the task. ");
    }

    @Test
    void testThrowsUserNotInCollaboration() throws CommandException {
        testRepository.addCollaboration(testUser.getUsername(), testPassword, "otherProject");
        Collaboration collaboration = testRepository.getCollaboration(
            testUser.getUsername(), testPassword, "otherProject");
        collaboration.getTasks().addTask(new Task.Builder("washDishes").build());

        User outsideUser = new UserImpl("outsideUser", testPassword);
        testRepository.addUser(outsideUser);

        String[] tokens = "assign-task collaboration=otherProject user=outsideUser task=washDishes"
            .split(" ");

        assertThrows(UserNotInCollaboration.class, () -> new AssignTask(
                testUser, testPassword, testRepository, tokens).execute(),
            "AssignTask command should throw UserNotInCollaboration when assignee is not a participant. ");
    }
}
