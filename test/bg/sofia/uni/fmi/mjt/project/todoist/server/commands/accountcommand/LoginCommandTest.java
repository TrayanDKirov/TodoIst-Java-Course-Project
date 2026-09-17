package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.accountcommand;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.AccountException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.UserNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.WrongPassword;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.InMemoryTodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginCommandTest {
    private static final String testUsername = "testUsername";
    private static final String testPassword = "testPassword";

    private static final AccountCommand testRegisterCommand = new RegisterCommand(testUsername, testPassword);
    private static final AccountCommand testLoginCommand = new LoginCommand(testUsername, testPassword);

    @Test
    void testLoginCommandConstructorWithNullUsername() {
        assertThrows(IllegalArgumentException.class, () -> new LoginCommand(null, testPassword),
            "LoginCommand constructor should throw IllegalArgumentException when give null. ");
    }

    @Test
    void testLoginCommandConstructorWithNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> new LoginCommand(testUsername, null),
            "LoginCommand constructor should throw IllegalArgumentException when give null. ");
    }

    @Test
    void testExecuteWithNull() {
        assertThrows(IllegalArgumentException.class, () -> testLoginCommand.execute(null),
            "Method execute of LoginCommand should throw IllegalArgumentException when give null. ");
    }

    @Test
    void testExecuteWithCorrectInputUsername() {
        TodoIstRepository repository = new InMemoryTodoIstRepository();

        try {
            testRegisterCommand.execute(repository);

            assertEquals(testUsername, testLoginCommand.execute(repository).getUser().getUsername(),
                "LoginCommand::execute should return the correct user when he exist in the repository. ");
        } catch (AccountException ex) {
            fail();
        }
    }

    @Test
    void testExecuteWithCorrectInputPassword() {
        TodoIstRepository repository = new InMemoryTodoIstRepository();

        try {
            testRegisterCommand.execute(repository);

            assertTrue(testLoginCommand.execute(repository).getUser().isPasswordCorrect(testPassword),
                "LoginCommand::execute should return the correct user when he exist in the repository. ");
        } catch (AccountException ex) {
            fail();
        }
    }

    @Test
    void testExecuteThrowsUsernameNotFound() {
        TodoIstRepository repository = new InMemoryTodoIstRepository();

        assertThrows(UserNotFound.class, () -> testLoginCommand.execute(repository),
            "LoginCommand::execute should throw UserNotFound if user with this username does not exist. ");
    }

    @Test
    void testExecuteWithWrongPassword() {
        TodoIstRepository repository = new InMemoryTodoIstRepository();

        try {
            testRegisterCommand.execute(repository);
            LoginCommand newLoginCommand = new LoginCommand(testUsername, "wrongPassword");

            assertThrows(WrongPassword.class, () -> newLoginCommand.execute(repository),
                "LoginCommand::execute should throw WrongPassword if users password is incorrect. ");
        } catch (AccountException ex) {
            fail();
        }
    }
}