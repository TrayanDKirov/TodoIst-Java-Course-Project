package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.accountcommand;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class AccountCommandFactoryTest {
    private static final String testUsername = "testUsername";
    private static final String testPassword = "testPassword";

    @Test
    void testOfWithNull() {
        String input = null;
        assertThrows(IllegalArgumentException.class, () -> AccountCommandFactory.of(input),
            "AccountCommandFactory.of should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testOfCreateLoginCommand() {
        try {
            AccountCommand command = AccountCommandFactory.of("login " + testUsername + " " + testPassword);

            assertTrue(command instanceof LoginCommand,
                "AccountCommandFactory.of should return LoginCommand when given valid login command. ");
        } catch (WrongCommandSyntax ex) {
            fail();
        }
    }

    @Test
    void testOfCreateWrongLoginCommandWithLessParams() {
        assertThrows(WrongCommandSyntax.class, () ->  AccountCommandFactory.of("login " + testUsername + " "),
            "AccountCommandFactory.of should throw WrongCommandSyntax when given invalid login command(with 1 params). ");
    }

    @Test
    void testOfCreateWrongLoginCommandWithMoreParams() {
        assertThrows(WrongCommandSyntax.class, () ->  AccountCommandFactory.of(
            "login " + testUsername + " " + testPassword + " " + testPassword),
            "AccountCommandFactory.of should throw WrongCommandSyntax when given invalid login command(with 3 params). ");
    }

    @Test
    void testOfCreateRegisterCommand() {
        try {
            AccountCommand command = AccountCommandFactory.of("register " + testUsername + " " + testPassword);

            assertTrue(command instanceof RegisterCommand,
                "AccountCommandFactory.of should return RegisterCommand when given valid register command. ");
        } catch (WrongCommandSyntax ex) {
            fail();
        }
    }

    @Test
    void testOfCreateWrongRegisterCommandWithLessParams() {
        assertThrows(WrongCommandSyntax.class, () ->  AccountCommandFactory.of("register " + testUsername + " "),
            "AccountCommandFactory.of should throw WrongCommandSyntax when given invalid register command(with 1 params). ");
    }

    @Test
    void testOfCreateWrongRegisterCommandWithMoreParams() {
        assertThrows(WrongCommandSyntax.class, () ->  AccountCommandFactory.of(
                "register " + testUsername + " " + testPassword + " " + testPassword),
            "AccountCommandFactory.of should throw WrongCommandSyntax when given invalid register command(with 3 params). ");
    }

    @Test
    void testOfCreateHelpCommand() {
        try {
            AccountCommand command = AccountCommandFactory.of("help");

            assertTrue(command instanceof HelpAccountCommand,
                "AccountCommandFactory.of should return HelpAccountCommand when given valid help command. ");
        } catch (WrongCommandSyntax ex) {
            fail();
        }
    }

    @Test
    void testOfCreateWrongHelpCommandWithMoreParams() {
        assertThrows(WrongCommandSyntax.class, () ->  AccountCommandFactory.of("help now"),
            "AccountCommandFactory.of should throw WrongCommandSyntax when given invalid help command(with more than 1 params). ");
    }

    @Test
    void testOfCreateWrongCommand() {
        try {
            AccountCommand command = AccountCommandFactory.of("wrong-command-name");

            assertTrue(command instanceof WrongCommand,
                "AccountCommandFactory.of should return WrongCommand when given invalid command. ");
        } catch (WrongCommandSyntax ex) {
            fail();
        }
    }
}