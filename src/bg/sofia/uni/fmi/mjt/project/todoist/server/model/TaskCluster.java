package bg.sofia.uni.fmi.mjt.project.todoist.server.model;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.task.TaskAlreadyExists;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.task.TaskNotFound;

import java.time.LocalDate;
import java.util.Map;

public interface TaskCluster {
    void addTask(Task taskToAdd) throws TaskAlreadyExists;

    Task getTask(String name) throws TaskNotFound;

    Task getTask(String name, LocalDate date) throws TaskNotFound;

    void updateTask(String name, Task taskWithUpdatedFields) throws TaskNotFound;

    void updateTask(String name, LocalDate date, Task taskWithUpdatedFields) throws TaskNotFound;

    void deleteTask(String nameOfTask) throws TaskNotFound;

    void deleteTask(String nameOfTask, LocalDate date) throws TaskNotFound;

    Map<String, Task> getTasks() throws TaskNotFound;

    Map<String, Task> getTasks(LocalDate date) throws TaskNotFound;

    String listTasks();

    String listTasks(LocalDate date);
}