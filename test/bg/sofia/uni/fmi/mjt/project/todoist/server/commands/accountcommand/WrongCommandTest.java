package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.accountcommand;

import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.InMemoryTodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WrongCommandTest {
    @Test
    void testConstructorWithNull() {
        assertThrows(IllegalArgumentException.class, () -> new WrongCommand(null),
            "WrongCommand constructor should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testExecuteWithNullRepository() {
        assertThrows(IllegalArgumentException.class, () -> new WrongCommand("fly-to-moon").execute(null),
            "WrongCommand::execute should throw IllegalArgumentException when given null repository. ");
    }

    @Test
    void testExecuteReturnsOutputContainingCommandUsed() {
        TodoIstRepository repository = new InMemoryTodoIstRepository();

        String output = new WrongCommand("fly-to-moon").execute(repository).getOutput();

        assertTrue(output.contains("fly-to-moon"),
            "WrongCommand::execute should return output containing the unknown command used. ");
    }

    @Test
    void testNoArgExecuteReturnsSameOutput() {
        String output = new WrongCommand("fly-to-moon").execute();

        assertTrue(output.contains("fly-to-moon"),
            "WrongCommand::execute() should return output containing the unknown command used. ");
    }
}
