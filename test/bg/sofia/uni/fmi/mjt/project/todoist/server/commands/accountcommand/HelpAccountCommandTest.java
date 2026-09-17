package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.accountcommand;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelpAccountCommandTest {
    @Test
    void testExecuteWithNull() {
        assertThrows(IllegalArgumentException.class, () -> new HelpAccountCommand().execute(null),
            "HelpAccountCommand::execute should throw IllegalArgumentException when given null. ");
    }
}