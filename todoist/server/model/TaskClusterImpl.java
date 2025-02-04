package bg.sofia.uni.fmi.mjt.project.todoist.server.model;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.TaskNotFound;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class TaskClusterImpl implements TaskCluster {
    private Map<String, Task> inboxTasks;
    private Map<LocalDate, Map<String, Task>> datedTasks;

    public TaskClusterImpl() {
        this.inboxTasks = new LinkedHashMap<>();
        this.datedTasks = new LinkedHashMap<>();
    }

    private void addTaskToInbox(Task taskToAdd) {
        if (taskToAdd == null) {
            throw new IllegalArgumentException("Null reference in TaskClusterImpl::addTaskToInbox");
        }

        inboxTasks.put(taskToAdd.getName(), taskToAdd);
    }

    private void addDatedTask(Task taskToAdd) {
        if (taskToAdd == null) {
            throw new IllegalArgumentException("Null reference in TaskClusterImpl::addDatedTask");
        }

        if (datedTasks.containsKey(taskToAdd.getDate())) {
            datedTasks.get(taskToAdd.getDate()).put(taskToAdd.getName(), taskToAdd);
        }

        Map<String, Task> newDateTasks = new LinkedHashMap<>();
        newDateTasks.put(taskToAdd.getName(), taskToAdd);

        datedTasks.put(taskToAdd.getDate(), newDateTasks);
    }

    @Override
    public void addTask(Task taskToAdd) {
        if (taskToAdd == null) {
            throw new IllegalArgumentException("Null reference in TaskClusterImpl::addTask");
        }

        if (!taskToAdd.isDateSet()) {
            addTaskToInbox(taskToAdd);
        }

        addDatedTask(taskToAdd);
    }

    @Override
    public Task getTask(String name) throws TaskNotFound {
        if (name == null) {
            throw new IllegalArgumentException("Null reference in TaskClusterImpl::getTask(name)");
        }

        Task toReturn =  inboxTasks.get(name);
        if (toReturn == null) {
            throw new TaskNotFound("Task with this name was not found. ");
        }

        return toReturn;
    }

    @Override
    public Task getTask(String name, LocalDate date) throws TaskNotFound {
        if (name == null || date == null) {
            throw new IllegalArgumentException("Null reference in TaskCluster::getTask(name, date)");
        }

        if (datedTasks.containsKey(date)) {
            Task toReturn = datedTasks.get(date).get(name);
            if (toReturn != null) {
                return toReturn;
            }
        }

        throw new TaskNotFound("Task with this name and date was not found. ");
    }

    @Override
    public void deleteTask(String nameOfTask) throws TaskNotFound {
        if (nameOfTask == null) {
            throw new IllegalArgumentException("Null reference in TaskCluster::deleteTask(name)");
        }

        Task deletedTask = inboxTasks.remove(nameOfTask);

        if (deletedTask == null) {
            throw new TaskNotFound("Task with this name was not found. ");
        }
    }

    @Override
    public void deleteTask(String nameOfTask, LocalDate date) throws TaskNotFound {
        if (nameOfTask == null || date == null) {
            throw new IllegalArgumentException("Null reference in TaskCluster::deleteTask(name, date)");
        }

        if (datedTasks.containsKey(date)) {
            Task deletedTask = datedTasks.get(date).remove(nameOfTask);

            if (datedTasks.get(date).isEmpty()) {
                datedTasks.remove(date);
            }

            if (deletedTask != null) {
                return;
            }
        }

        throw new TaskNotFound("Task with this name and date was not found. ");
    }

    @Override
    public Map<String, Task> getTasks() throws TaskNotFound {
        if (inboxTasks.isEmpty()) {
            throw new TaskNotFound("No tasks found in inbox. ");
        }
        return Map.copyOf(inboxTasks);
    }

    @Override
    public Map<String, Task> getTasks(LocalDate date) throws TaskNotFound {
        if (!datedTasks.containsKey(date)) {
            throw new TaskNotFound("No tasks for date: " + TaskUtils.fromDateToString(date) + ". ");
        }

        return Map.copyOf(datedTasks.get(date));
    }
}
