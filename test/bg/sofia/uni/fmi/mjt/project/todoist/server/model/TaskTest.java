package bg.sofia.uni.fmi.mjt.project.todoist.server.model;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.task.TaskParameterNotSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskTest {
    @Test
    void testBuilderConstructorWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> new Task.Builder(null),
            "Task.Builder constructor should throw IllegalArgumentException when given null name. ");
    }

    @Test
    void testBuilderSetDateWithNull() {
        Task.Builder builder = new Task.Builder("task");

        assertThrows(IllegalArgumentException.class, () -> builder.setDate(null),
            "Task.Builder::setDate should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testBuilderSetDueDateWithNull() {
        Task.Builder builder = new Task.Builder("task");

        assertThrows(IllegalArgumentException.class, () -> builder.setDueDate(null),
            "Task.Builder::setDueDate should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testBuilderSetDescriptionWithNull() {
        Task.Builder builder = new Task.Builder("task");

        assertThrows(IllegalArgumentException.class, () -> builder.setDescription(null),
            "Task.Builder::setDescription should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testGetDateWhenNotSetThrows() {
        Task task = new Task.Builder("task").build();

        assertThrows(TaskParameterNotSet.class, task::getDate,
            "Task::getDate should throw TaskParameterNotSet when date is not set. ");
    }

    @Test
    void testGetDueDateWhenNotSetThrows() {
        Task task = new Task.Builder("task").build();

        assertThrows(TaskParameterNotSet.class, task::getDueDate,
            "Task::getDueDate should throw TaskParameterNotSet when due date is not set. ");
    }

    @Test
    void testGetDescriptionWhenNotSetThrows() {
        Task task = new Task.Builder("task").build();

        assertThrows(TaskParameterNotSet.class, task::getDescription,
            "Task::getDescription should throw TaskParameterNotSet when description is not set. ");
    }

    @Test
    void testGetAssigneeWhenNotSetThrows() {
        Task task = new Task.Builder("task").build();

        assertThrows(TaskParameterNotSet.class, task::getAssignee,
            "Task::getAssignee should throw TaskParameterNotSet when assignee is not set. ");
    }

    @Test
    void testSetAssigneeWithNull() {
        Task task = new Task.Builder("task").build();

        assertThrows(IllegalArgumentException.class, () -> task.setAssignee(null),
            "Task::setAssignee should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testSetAssigneeAndGetIt() {
        Task task = new Task.Builder("task").build();
        task.setAssignee("ivan");

        assertEquals("ivan", task.getAssignee(),
            "Task::getAssignee should return the assignee that was set. ");
    }

    @Test
    void testNewTaskIsNotCompleted() {
        Task task = new Task.Builder("task").build();

        assertFalse(task.isCompleted(),
            "A new Task should not be completed. ");
    }

    @Test
    void testCompleteFirstTimeReturnsTrue() {
        Task task = new Task.Builder("task").build();

        assertTrue(task.complete(),
            "Task::complete should return true the first time it's called. ");
        assertTrue(task.isCompleted(),
            "Task::complete should mark the task as completed. ");
    }

    @Test
    void testCompleteSecondTimeReturnsFalse() {
        Task task = new Task.Builder("task").build();
        task.complete();

        assertFalse(task.complete(),
            "Task::complete should return false when task is already completed. ");
    }

    @Test
    void testUpdateTaskWithNull() {
        Task task = new Task.Builder("task").build();

        assertThrows(IllegalArgumentException.class, () -> task.updateTask(null),
            "Task::updateTask should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testUpdateTaskOnlyChangesSetFields() {
        Task task = new Task.Builder("task").setDescription("old").build();
        Task update = new Task.Builder("ignoredName").setDescription("new").build();

        task.updateTask(update);

        assertEquals("new", task.getDescription(),
            "Task::updateTask should update description when it's set in the given task. ");
        assertFalse(task.isDateSet(),
            "Task::updateTask should not set date when it's not set in the given task. ");
    }
}
