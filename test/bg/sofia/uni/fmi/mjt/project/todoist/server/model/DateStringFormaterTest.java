package bg.sofia.uni.fmi.mjt.project.todoist.server.model;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.DateFormatIncorrect;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DateStringFormaterTest {
    @Test
    void testStringToDateWithNull() {
        assertThrows(IllegalArgumentException.class, () -> DateStringFormater.stringToDate(null),
            "DateStringFormater::stringToDate should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testStringToDateWithWrongFormat() {
        assertThrows(DateFormatIncorrect.class, () -> DateStringFormater.stringToDate("2025-02-10"),
            "DateStringFormater::stringToDate should throw DateFormatIncorrect when given wrong format. ");
    }

    @Test
    void testStringToDateWithInvalidDate() {
        assertThrows(DateFormatIncorrect.class, () -> DateStringFormater.stringToDate("32/13/2025"),
            "DateStringFormater::stringToDate should throw DateFormatIncorrect when given an invalid date. ");
    }

    @Test
    void testStringToDateWithCorrectFormat() throws DateFormatIncorrect {
        LocalDate date = DateStringFormater.stringToDate("10/02/2025");

        assertEquals(LocalDate.of(2025, 2, 10), date,
            "DateStringFormater::stringToDate should correctly parse a valid date string. ");
    }

    @Test
    void testDateToStringWithNull() {
        assertThrows(IllegalArgumentException.class, () -> DateStringFormater.dateToString(null),
            "DateStringFormater::dateToString should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testDateToStringWithCorrectDate() {
        String result = DateStringFormater.dateToString(LocalDate.of(2025, 2, 10));

        assertEquals("10/02/2025", result,
            "DateStringFormater::dateToString should correctly format a date. ");
    }
}
