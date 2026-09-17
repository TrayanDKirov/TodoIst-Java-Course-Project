package bg.sofia.uni.fmi.mjt.project.todoist.server.model;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.task.TaskAlreadyExists;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.task.TaskNotFound;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TaskClusterImpl implements TaskCluster, Serializable {
    private static final long serialVersionUID = -544486395372461478L;

    private Map<String, Task> inboxTasks;
    private Map<LocalDate, Map<String, Task>> datedTasks;

    public TaskClusterImpl() {
        this.inboxTasks = new HashMap<>();
        this.datedTasks = new HashMap<>();
    }

    private synchronized void addTaskToInbox(Task taskToAdd) throws TaskAlreadyExists {
        if (taskToAdd == null) {
            throw new IllegalArgumentException("Null reference in TaskClusterImpl::addTaskToInbox");
        }

        if (inboxTasks.containsKey(taskToAdd.getName())) {
            throw new TaskAlreadyExists("Task with this name in your inbox already exists. ");
        }

        inboxTasks.put(taskToAdd.getName(), taskToAdd);
    }

    private synchronized void addDatedTask(Task taskToAdd) throws TaskAlreadyExists {
        if (taskToAdd == null) {
            throw new IllegalArgumentException("Null reference in TaskClusterImpl::addDatedTask");
        }

        if (datedTasks.containsKey(taskToAdd.getDate())) {
            if (datedTasks.get(taskToAdd.getDate()).containsKey(taskToAdd.getName())) {
                throw new TaskAlreadyExists("Task with this name and date already exists. ");
            }

            datedTasks.get(taskToAdd.getDate()).put(taskToAdd.getName(), taskToAdd);
            return;
        }

        Map<String, Task> newDateTasks = new LinkedHashMap<>();
        newDateTasks.put(taskToAdd.getName(), taskToAdd);

        datedTasks.put(taskToAdd.getDate(), newDateTasks);
    }

    @Override
    public synchronized void addTask(Task taskToAdd) throws TaskAlreadyExists {
        if (taskToAdd == null) {
            throw new IllegalArgumentException("Null reference in TaskClusterImpl::addTask");
        }

        if (!taskToAdd.isDateSet()) {
            addTaskToInbox(taskToAdd);
            return;
        }

        addDatedTask(taskToAdd);
    }

    @Override
    public synchronized Task getTask(String name) throws TaskNotFound {
        if (name == null) {
            throw new IllegalArgumentException("Null reference in TaskClusterImpl::getTask(name)");
        }

        Task toReturn = inboxTasks.get(name);
        if (toReturn == null) {
            throw new TaskNotFound("Task with this name was not found. ");
        }

        return toReturn;
    }

    @Override
    public synchronized Task getTask(String name, LocalDate date) throws TaskNotFound {
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
    public synchronized void updateTask(String name, Task taskWithUpdatedFields) throws TaskNotFound {
        if (name == null || taskWithUpdatedFields == null) {
            throw new IllegalArgumentException("Null reference in TaskCluster::updateTask. ");
        }

        Task taskToUpdate = getTask(name);
        deleteTask(name);

        taskToUpdate.updateTask(taskWithUpdatedFields);

        try {
            addTask(taskToUpdate);
        } catch (TaskAlreadyExists exception) {
            throw new RuntimeException(
                "This should not be possible. Task still exits after being deleted. TaskClusterImpl::UpdateTask. ");
        }
    }

    @Override
    public synchronized void updateTask(String name, LocalDate date, Task taskWithUpdatedFields) throws TaskNotFound {
        if (name == null || taskWithUpdatedFields == null) {
            throw new IllegalArgumentException("Null reference in TaskCluster::updateTask. ");
        }

        Task taskToUpdate = getTask(name, date);
        deleteTask(name, date);

        taskToUpdate.updateTask(taskWithUpdatedFields);

        try {
            addTask(taskToUpdate);
        } catch (TaskAlreadyExists exception) {
            throw new RuntimeException(
                "This should not be possible. Task still exits after being deleted. TaskClusterImpl::UpdateTask. ");
        }
    }

    @Override
    public synchronized void deleteTask(String nameOfTask) throws TaskNotFound {
        if (nameOfTask == null) {
            throw new IllegalArgumentException("Null reference in TaskCluster::deleteTask(name)");
        }

        Task deletedTask = inboxTasks.remove(nameOfTask);

        if (deletedTask == null) {
            throw new TaskNotFound("Task with this name was not found. ");
        }
    }

    @Override
    public synchronized void deleteTask(String nameOfTask, LocalDate date) throws TaskNotFound {
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
    public synchronized Map<String, Task> getTasks() throws TaskNotFound {
        if (inboxTasks.isEmpty()) {
            throw new TaskNotFound("No tasks found in inbox. ");
        }

        return Map.copyOf(inboxTasks);
    }

    @Override
    public synchronized Map<String, Task> getTasks(LocalDate date) throws TaskNotFound {
        if (!datedTasks.containsKey(date)) {
            throw new TaskNotFound("No tasks for date: " + DateStringFormater.dateToString(date) + ". ");
        }

        return Map.copyOf(datedTasks.get(date));
    }

    @Override
    public String listTasks() {
        Collection<Task> tasks = inboxTasks.values();

        StringBuilder result = new StringBuilder("Tasks from inbox: ");
        for (Task currTask : tasks) {
            result.append(System.lineSeparator())
                .append(System.lineSeparator()).append(currTask.getInfo());
        }

        return result.toString().intern();
    }

    @Override
    public String listTasks(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Null reference in TaskClusterImpl. ");
        }

        if (!datedTasks.containsKey(date)) {
            return "No tasks for date - " + DateStringFormater.dateToString(date) + ". ";
        }

        StringBuilder result = new StringBuilder("Tasks for date - " + DateStringFormater.dateToString(date) + ". ");

        Collection<Task> tasks = datedTasks.get(date).values();
        for (Task currTask : tasks) {
            result.append(System.lineSeparator())
                .append(System.lineSeparator()).append(currTask.getInfo());
        }

        return result.toString().intern();
    }
}
