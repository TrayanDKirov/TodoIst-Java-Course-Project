package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.accountcommand;

import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.UserImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountCommandResultTest {
    private static final String testUsername = "testUsername";
    private static final String testPassword = "testPassword";

    private static final User testUser = new UserImpl(testUsername, testPassword);

    private static String testOutput = "testOutput";

    @Test
    void testBuilderConstructorWithNull() {
        assertThrows(IllegalArgumentException.class, () -> new AccountCommandResult.Builder(null),
            "AccountCommandResult.Builder should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testSetUserAndPasswordWithNullUsername() {
        AccountCommandResult.Builder builder = new AccountCommandResult.Builder(testOutput);
        assertThrows(IllegalArgumentException.class, () -> builder.setUserAndPassword(null, testPassword),
            "AccountCommandResult.Builder::setUserAndPassword should throw IllegalArgumentException when given null user. ");
    }

    @Test
    void testSetUserAndPasswordWithNullPassword() {
        AccountCommandResult.Builder builder = new AccountCommandResult.Builder(testOutput);
        assertThrows(IllegalArgumentException.class, () -> builder.setUserAndPassword(testUser, null),
            "AccountCommandResult.Builder::setUserAndPassword should throw IllegalArgumentException when given null password. ");
    }

    @Test
    void testGetUser() {
        AccountCommandResult testResult = new AccountCommandResult.Builder(testOutput)
            .setUserAndPassword(testUser, testPassword)
            .build();

        assertEquals(testUser.getUsername(), testResult.getUser().getUsername(),
            "AccountCommandResult::getUser should return correct user");
    }

    @Test
    void testGetPasswordOfUser() {
        AccountCommandResult testResult = new AccountCommandResult.Builder(testOutput)
            .setUserAndPassword(testUser, testPassword)
            .build();

        assertEquals(testPassword, testResult.getPasswordOfUser(),
            "AccountCommandResult::getPasswordOfUser should return correct password. ");
    }

    @Test
    void testGetUserWhenItsNotSet() {
        AccountCommandResult testResult = new AccountCommandResult.Builder(testOutput)
            .build();

        assertThrows(RuntimeException.class, () -> testResult.getUser(),
            "AccountCommandResult::getUser should throw RuntimeException when user is not set. ");
    }

    @Test
    void testGetPasswordOfUserWhenItsNotSet() {
        AccountCommandResult testResult = new AccountCommandResult.Builder(testOutput)
            .build();

        assertThrows(RuntimeException.class, () -> testResult.getPasswordOfUser(),
            "AccountCommandResult::getPasswordOfUser should throw RuntimeException when password is not set. ");
    }
}