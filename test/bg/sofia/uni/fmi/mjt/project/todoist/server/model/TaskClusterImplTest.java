package bg.sofia.uni.fmi.mjt.project.todoist.server.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskClusterImplTest {
    @Test
    void testGetTaskWithNull() {
        assertThrows(IllegalArgumentException.class, () -> new TaskClusterImpl().getTask(null),
            "TaskClusterImpl::getTask should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testGetTaskWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> new TaskClusterImpl().getTask(null, LocalDate.now()),
            "TaskClusterImpl::getTask should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testGetTaskWithNullDate() {
        assertThrows(IllegalArgumentException.class, () -> new TaskClusterImpl().getTask("name", null),
            "TaskClusterImpl::getTask should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testDeleteTaskWithNull() {
        assertThrows(IllegalArgumentException.class, () -> new TaskClusterImpl().deleteTask(null),
            "TaskClusterImpl::deleteTask should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testDeleteTaskWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> new TaskClusterImpl().deleteTask(null, LocalDate.now()),
            "TaskClusterImpl::getTdeleteTaskask should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testDeleteTaskWithNullDate() {
        assertThrows(IllegalArgumentException.class, () -> new TaskClusterImpl().deleteTask("name", null),
            "TaskClusterImpl::deleteTask should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testUpdateTaskWithNullTaskToUpdate() {
        assertThrows(IllegalArgumentException.class, () -> new TaskClusterImpl().updateTask(
            "name", LocalDate.now(), null),
            "TaskClusterImpl::updateTask should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testUpdateTaskWithNullName() {
        Task task = new Task.Builder("name").build();

        assertThrows(IllegalArgumentException.class, () -> new TaskClusterImpl().updateTask(
            null, LocalDate.now(), task),
            "TaskClusterImpl::updateTask should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testUpdateTaskWithNullDate() {
        Task task = new Task.Builder("name").build();

        assertThrows(IllegalArgumentException.class, () -> new TaskClusterImpl().updateTask(
            "name", null, task),
            "TaskClusterImpl::updateTask should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testUpdateInboxTaskWithNullTaskToUpdate() {
        assertThrows(IllegalArgumentException.class, () -> new TaskClusterImpl().updateTask(
                "name",  null),
            "TaskClusterImpl::updateTask should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testUpdateInboxTaskWithNullName() {
        Task task = new Task.Builder("name").build();

        assertThrows(IllegalArgumentException.class, () -> new TaskClusterImpl().updateTask(
                null, task),
            "TaskClusterImpl::updateTask should throw IllegalArgumentException when given null. ");
    }
}