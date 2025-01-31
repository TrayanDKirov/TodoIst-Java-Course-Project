package bg.sofia.uni.fmi.mjt.project.todoist.server.model;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.TaskNotFound;

import java.time.LocalDate;
import java.util.Map;

public interface TaskCluster {
    void addTask(Task taskToAdd);

    Task getTask(String name) throws TaskNotFound;

    Task getTask(String name, LocalDate date) throws TaskNotFound;

    boolean deleteTask(String nameOfTask);

    boolean deleteTask(String nameOfTask, LocalDate date);

    Map<String, Task> getTasks();

    Map<String, Task> getTasks(LocalDate date);
}