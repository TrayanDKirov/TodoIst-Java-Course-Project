package bg.sofia.uni.fmi.mjt.project.todoist.server.model;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.CommandException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.DateFormatIncorrect;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

public class TaskUtils {
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final DateTimeFormatter FORMATER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    public static LocalDate fromStringToDate(String dateInString) throws DateFormatIncorrect {
        if (dateInString == null) {
            throw new IllegalArgumentException("Null reference in TaskUtils::fromStringToDate. ");
        }

        try {
            return LocalDate.parse(dateInString, FORMATER);
        } catch (DateTimeParseException parseException) {
            throw new DateFormatIncorrect("Date format is incorrect. Correct format is: " + DATE_FORMAT);
        }
    }

    public static String fromDateToString(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Null reference in TaskUtils::fromDateToString. ");
        }

        return date.format(FORMATER);
    }

    public static StringBuilder listTasks(Map<String, Task> tasksMap) {
        if (tasksMap == null) {
            throw new IllegalArgumentException("Null reference in TaskUtils::listTasks");
        }

        Collection<Task> tasks = tasksMap.values();

        StringBuilder result = new StringBuilder();
        for (Task currTask : tasks) {
            result.append(System.lineSeparator()).append(currTask.getInfo());
        }

        return result;
    }
}
