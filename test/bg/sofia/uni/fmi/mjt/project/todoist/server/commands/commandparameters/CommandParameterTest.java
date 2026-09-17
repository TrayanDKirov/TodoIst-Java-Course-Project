package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.commandparameters;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongCommandSyntax;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandParameterTest {
    @Test
    void testRecordConstructorWithNullParam() {
        assertThrows(IllegalArgumentException.class, () -> new CommandParameter(null, ParameterType.NAME_OF_TASK),
            "CommandParameter constructor should throw IllegalArgumentException when given null param. ");
    }

    @Test
    void testRecordConstructorWithNullType() {
        assertThrows(IllegalArgumentException.class, () -> new CommandParameter("name", null),
            "CommandParameter constructor should throw IllegalArgumentException when given null type. ");
    }

    @Test
    void testParseParameterWithNull() {
        assertThrows(IllegalArgumentException.class, () -> CommandParameter.parseParameter(null),
            "CommandParameter::parseParameter should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testParseParameterWithoutEquals() {
        assertThrows(WrongCommandSyntax.class, () -> CommandParameter.parseParameter("name"),
            "CommandParameter::parseParameter should throw WrongCommandSyntax when given a string without \"=\". ");
    }

    @Test
    void testParseParameterWithTooManyEquals() {
        assertThrows(WrongCommandSyntax.class, () -> CommandParameter.parseParameter("name=task=other"),
            "CommandParameter::parseParameter should throw WrongCommandSyntax when given too many \"=\". ");
    }

    @Test
    void testParseParameterReturnsCorrectParamAndType() throws WrongCommandSyntax {
        CommandParameter parameter = CommandParameter.parseParameter("name=cleanKitchen");

        assertEquals("cleanKitchen", parameter.param(),
            "CommandParameter::parseParameter should return correct param value. ");
        assertEquals(ParameterType.NAME_OF_TASK, parameter.type(),
            "CommandParameter::parseParameter should return correct param type. ");
    }

    @Test
    void testParseParameterWithUnknownDefinitionName() throws WrongCommandSyntax {
        CommandParameter parameter = CommandParameter.parseParameter("unknown=value");

        assertEquals(ParameterType.UNKNOWN_PARAMETER, parameter.type(),
            "CommandParameter::parseParameter should return UNKNOWN_PARAMETER type for unknown definition name. ");
    }

    @Test
    void testParseArrayOfParametersWithNullTokens() {
        assertThrows(IllegalArgumentException.class, () -> CommandParameter.parseArrayOfParameters(null, 0),
            "CommandParameter::parseArrayOfParameters should throw IllegalArgumentException when given null tokens. ");
    }

    @Test
    void testParseArrayOfParametersWithInvalidBegin() {
        String[] tokens = "add-task name=cleanKitchen".split(" ");

        assertThrows(IllegalArgumentException.class, () -> CommandParameter.parseArrayOfParameters(tokens, 5),
            "CommandParameter::parseArrayOfParameters should throw IllegalArgumentException " +
                "when given invalid begin index. ");
    }

    @Test
    void testParseArrayOfParametersReturnsCorrectArray() throws WrongCommandSyntax {
        String[] tokens = "add-task name=cleanKitchen description=tidy".split(" ");

        CommandParameter[] parameters = CommandParameter.parseArrayOfParameters(tokens, 1);

        assertEquals(2, parameters.length,
            "CommandParameter::parseArrayOfParameters should return an array starting from the given index. ");
        assertEquals("cleanKitchen", parameters[0].param(),
            "CommandParameter::parseArrayOfParameters should parse each token correctly. ");
    }
}
