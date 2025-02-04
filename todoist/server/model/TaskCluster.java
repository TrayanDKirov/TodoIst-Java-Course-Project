package bg.sofia.uni.fmi.mjt.project.todoist.server.model;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.TaskNotFound;

import java.time.LocalDate;
import java.util.Map;

public interface TaskCluster {
    void addTask(Task taskToAdd);

    Task getTask(String name) throws TaskNotFound;

    Task getTask(String name, LocalDate date) throws TaskNotFound;

    void replaceTask(String name, Task taskToReplace);

    void replaceTask(String name, LocalDate date, Task taskToReplace);

    void deleteTask(String nameOfTask) throws TaskNotFound;

    void deleteTask(String nameOfTask, LocalDate date) throws TaskNotFound;

    Map<String, Task> getTasks() throws TaskNotFound;

    Map<String, Task> getTasks(LocalDate date) throws TaskNotFound;
}