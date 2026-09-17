package bg.sofia.uni.fmi.mjt.project.todoist.server.model;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.DateFormatIncorrect;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateStringFormater {
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final DateTimeFormatter FORMATER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    public static LocalDate stringToDate(String dateInString) throws DateFormatIncorrect {
        if (dateInString == null) {
            throw new IllegalArgumentException("Null reference in DateToStringFormater::fromStringToDate. ");
        }

        try {
            return LocalDate.parse(dateInString, FORMATER);
        } catch (DateTimeParseException parseException) {
            throw new DateFormatIncorrect("Date format is incorrect. Correct format is: " + DATE_FORMAT);
        }
    }

    public static String dateToString(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Null reference in DateToStringFormater::fromDateToString. ");
        }

        return date.format(FORMATER);
    }
}
