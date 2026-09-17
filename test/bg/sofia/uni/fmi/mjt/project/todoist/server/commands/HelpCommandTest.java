package bg.sofia.uni.fmi.mjt.project.todoist.server.commands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HelpCommandTest {
    @Test
    void testConstructorWithNullTokens() {
        assertThrows(IllegalArgumentException.class, () -> new HelpCommand(null),
            "HelpCommand constructor should throw IllegalArgumentException when given null tokens. ");
    }

    @Test
    void testThrowsWrongCommandSyntaxWhenGivenTooManyParams() {
        String[] tokens = "help add-task now"
            .split(" ");

        assertThrows(WrongCommandSyntax.class, () -> new HelpCommand(tokens),
            "HelpCommand constructor should throw WrongCommandSyntax when given too many params. ");
    }

    @Test
    void testExecuteWithoutParamReturnsGeneralHelp() throws WrongCommandSyntax {
        String[] tokens = "help"
            .split(" ");

        String output = new HelpCommand(tokens).execute();

        assertTrue(output.contains("add-task"),
            "HelpCommand::execute without a param should list the possible commands. ");
    }

    @Test
    void testExecuteWithKnownCommandReturnsItsSyntax() throws WrongCommandSyntax {
        String[] tokens = "help add-task"
            .split(" ");

        String output = new HelpCommand(tokens).execute();

        assertTrue(output.contains("add-task"),
            "HelpCommand::execute with a known command should return its syntax and explanation. ");
    }

    @Test
    void testExecuteWithUnknownCommandReturnsUnknownMessage() throws WrongCommandSyntax {
        String[] tokens = "help fly-to-moon"
            .split(" ");

        String output = new HelpCommand(tokens).execute();

        assertEquals("Unknown command. ", output,
            "HelpCommand::execute with an unknown command name should return \"Unknown command. \". ");
    }
}
