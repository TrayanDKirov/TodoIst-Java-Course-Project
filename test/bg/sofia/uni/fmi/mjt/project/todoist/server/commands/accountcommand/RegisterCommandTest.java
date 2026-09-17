package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.accountcommand;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.AccountException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.UsernameIsTaken;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.InMemoryTodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class RegisterCommandTest {
    private static final String testUsername = "testUsername";
    private static final String testPassword = "testPassword";

    private static final AccountCommand testRegisterCommand = new RegisterCommand(testUsername, testPassword);

    @Test
    void testRegisterCommandConstructorWithNullUsername() {
        assertThrows(IllegalArgumentException.class, () -> new RegisterCommand(null, testPassword),
            "RegisterCommand constructor should throw IllegalArgumentException when give null. ");
    }

    @Test
    void testRegisterCommandConstructorWithNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> new RegisterCommand(testUsername, null),
            "RegisterCommand constructor should throw IllegalArgumentException when give null. ");
    }

    @Test
    void testExecuteWithNull() {
        assertThrows(IllegalArgumentException.class, () -> testRegisterCommand.execute(null),
            "Method execute of RegisterCommand should throw IllegalArgumentException when give null. ");
    }

    @Test
    void testExecuteWithCorrectInputUsername() {
        TodoIstRepository repository = new InMemoryTodoIstRepository();

        try {
            testRegisterCommand.execute(repository);

            assertEquals(testUsername, repository.getUser(testUsername).getUsername(),
                "RegisterCommand::execute should add the user to the repository with it's username. ");
        } catch (AccountException ex) {
            fail();
        }
    }

    @Test
    void testExecuteWithCorrectInputPassword() {
        TodoIstRepository repository = new InMemoryTodoIstRepository();

        try {
            testRegisterCommand.execute(repository);

            assertTrue(repository.getUser(testUsername).isPasswordCorrect(testPassword),
                "RegisterCommand::execute should add the user to the repository with it's username. ");
        } catch (AccountException ex) {
            fail();
        }
    }

    @Test
    void testExecuteThrowsUsernameIsTaken() {
        TodoIstRepository repository = new InMemoryTodoIstRepository();

        RegisterCommand newRegisterCommand = new RegisterCommand(testUsername, "otherPassword");

        try {
            testRegisterCommand.execute(repository);

            assertThrows(UsernameIsTaken.class, () -> newRegisterCommand.execute(repository),
                "RegisterCommand::execute should throw UsernameIsTaken if username is taken. ");
        } catch (AccountException ex) {
            fail();
        }
    }
}