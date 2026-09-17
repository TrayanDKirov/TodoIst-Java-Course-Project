package bg.sofia.uni.fmi.mjt.project.todoist.server.repository;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.UserNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.UsernameIsTaken;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.WrongPassword;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.CollaborationAlreadyExist;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.CollaborationCreatorException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.CollaborationNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.UserNotInCollaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.UserImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryTodoIstRepositoryTest {
    @Test
    void testIsUsernameTakenWithNull() {
        TodoIstRepository repository = new InMemoryTodoIstRepository();

        assertThrows(IllegalArgumentException.class, () -> repository.isUsernameTaken(null),
            "InMemoryTodoIstRepository::isUsernameTaken should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testIsUsernameTakenWhenNotTaken() {
        TodoIstRepository repository = new InMemoryTodoIstRepository();

        assertFalse(repository.isUsernameTaken("user"),
            "InMemoryTodoIstRepository::isUsernameTaken should return false when username is not taken. ");
    }

    @Test
    void testAddUserWithNull() {
        TodoIstRepository repository = new InMemoryTodoIstRepository();

        assertThrows(IllegalArgumentException.class, () -> repository.addUser(null),
            "InMemoryTodoIstRepository::addUser should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testAddUserAndGetUser() throws UsernameIsTaken, UserNotFound {
        TodoIstRepository repository = new InMemoryTodoIstRepository();
        User user = new UserImpl("user", "pass");
        repository.addUser(user);

        assertEquals("user", repository.getUser("user").getUsername(),
            "InMemoryTodoIstRepository::getUser should return the user that was added. ");
        assertTrue(repository.isUsernameTaken("user"),
            "InMemoryTodoIstRepository::isUsernameTaken should return true after user was added. ");
    }

    @Test
    void testAddUserWithTakenUsernameThrows() throws UsernameIsTaken {
        TodoIstRepository repository = new InMemoryTodoIstRepository();
        repository.addUser(new UserImpl("user", "pass"));

        assertThrows(UsernameIsTaken.class, () -> repository.addUser(new UserImpl("user", "otherPass")),
            "InMemoryTodoIstRepository::addUser should throw UsernameIsTaken when username is already taken. ");
    }

    @Test
    void testGetUserNotFound() {
        TodoIstRepository repository = new InMemoryTodoIstRepository();

        assertThrows(UserNotFound.class, () -> repository.getUser("missingUser"),
            "InMemoryTodoIstRepository::getUser should throw UserNotFound when user does not exist. ");
    }

    @Test
    void testAddCollaborationWithWrongPassword() throws UsernameIsTaken {
        TodoIstRepository repository = new InMemoryTodoIstRepository();
        repository.addUser(new UserImpl("user", "pass"));

        assertThrows(WrongPassword.class, () -> repository.addCollaboration("user", "wrongPass", "project"),
            "InMemoryTodoIstRepository::addCollaboration should throw WrongPassword when given wrong password. ");
    }

    @Test
    void testAddCollaborationTwiceThrows() throws UsernameIsTaken, CollaborationAlreadyExist,
        UserNotFound, WrongPassword {
        TodoIstRepository repository = new InMemoryTodoIstRepository();
        repository.addUser(new UserImpl("user", "pass"));
        repository.addCollaboration("user", "pass", "project");

        assertThrows(CollaborationAlreadyExist.class, () -> repository.addCollaboration("user", "pass", "project"),
            "InMemoryTodoIstRepository::addCollaboration should throw CollaborationAlreadyExist " +
                "when name is already taken. ");
    }

    @Test
    void testDeleteCollaborationNotFound() {
        TodoIstRepository repository = new InMemoryTodoIstRepository();

        assertThrows(CollaborationNotFound.class,
            () -> repository.deleteCollaboration("user", "pass", "missingProject"),
            "InMemoryTodoIstRepository::deleteCollaboration should throw CollaborationNotFound " +
                "when collaboration does not exist. ");
    }

    @Test
    void testDeleteCollaborationByNonCreatorThrows() throws UsernameIsTaken, CollaborationAlreadyExist,
        UserNotFound, WrongPassword {
        TodoIstRepository repository = new InMemoryTodoIstRepository();
        repository.addUser(new UserImpl("creator", "pass"));
        repository.addUser(new UserImpl("other", "pass"));
        repository.addCollaboration("creator", "pass", "project");

        assertThrows(CollaborationCreatorException.class,
            () -> repository.deleteCollaboration("other", "pass", "project"),
            "InMemoryTodoIstRepository::deleteCollaboration should throw CollaborationCreatorException " +
                "when deleter is not the creator. ");
    }

    @Test
    void testAddAndDeleteCollaborationCorrectly() throws UsernameIsTaken, CollaborationAlreadyExist,
        UserNotFound, WrongPassword, CollaborationCreatorException, CollaborationNotFound {
        TodoIstRepository repository = new InMemoryTodoIstRepository();
        repository.addUser(new UserImpl("creator", "pass"));
        repository.addCollaboration("creator", "pass", "project");

        repository.deleteCollaboration("creator", "pass", "project");

        assertThrows(CollaborationNotFound.class,
            () -> repository.getCollaboration("creator", "pass", "project"),
            "InMemoryTodoIstRepository::deleteCollaboration should remove the collaboration. ");
    }

    @Test
    void testGetCollaborationWhenUserNotParticipant() throws UsernameIsTaken, CollaborationAlreadyExist,
        UserNotFound, WrongPassword {
        TodoIstRepository repository = new InMemoryTodoIstRepository();
        repository.addUser(new UserImpl("creator", "pass"));
        repository.addUser(new UserImpl("outsider", "pass"));
        repository.addCollaboration("creator", "pass", "project");

        assertThrows(UserNotInCollaboration.class,
            () -> repository.getCollaboration("outsider", "pass", "project"),
            "InMemoryTodoIstRepository::getCollaboration should throw UserNotInCollaboration " +
                "when the user is not a participant. ");
    }
}
