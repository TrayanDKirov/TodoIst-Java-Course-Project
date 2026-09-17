package bg.sofia.uni.fmi.mjt.project.todoist.server.user;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.WrongPassword;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.UserIsAlreadyInTheCollaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.UserNotInCollaboration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserImplTest {
    @Test
    void testConstructorWithNullUsername() {
        assertThrows(IllegalArgumentException.class, () -> new UserImpl(null, "pass"),
            "UserImpl constructor should throw IllegalArgumentException when given null username. ");
    }

    @Test
    void testConstructorWithNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> new UserImpl("user", null),
            "UserImpl constructor should throw IllegalArgumentException when given null password. ");
    }

    @Test
    void testIsPasswordCorrectWithCorrectPassword() {
        User user = new UserImpl("user", "pass");

        assertTrue(user.isPasswordCorrect("pass"),
            "UserImpl::isPasswordCorrect should return true when given the correct password. ");
    }

    @Test
    void testIsPasswordCorrectWithWrongPassword() {
        User user = new UserImpl("user", "pass");

        assertFalse(user.isPasswordCorrect("wrongPass"),
            "UserImpl::isPasswordCorrect should return false when given the wrong password. ");
    }

    @Test
    void testChangePasswordWithWrongOldPassword() {
        User user = new UserImpl("user", "pass");

        assertThrows(WrongPassword.class, () -> user.changePassword("wrongPass", "newPass"),
            "UserImpl::changePassword should throw WrongPassword when given the wrong old password. ");
    }

    @Test
    void testChangePasswordCorrectly() throws WrongPassword {
        User user = new UserImpl("user", "pass");
        user.changePassword("pass", "newPass");

        assertTrue(user.isPasswordCorrect("newPass"),
            "UserImpl::changePassword should change the password when given the correct old password. ");
    }

    @Test
    void testGetTasksWithWrongPassword() {
        User user = new UserImpl("user", "pass");

        assertThrows(WrongPassword.class, () -> user.getTasks("wrongPass"),
            "UserImpl::getTasks should throw WrongPassword when given the wrong password. ");
    }

    @Test
    void testHasCollaborationWhenNotAdded() throws WrongPassword {
        User user = new UserImpl("user", "pass");

        assertFalse(user.hasCollaboration("project"),
            "UserImpl::hasCollaboration should return false when user is not part of the collaboration. ");
    }

    @Test
    void testAddCollaborationCorrectly() throws UserIsAlreadyInTheCollaboration, WrongPassword {
        User user = new UserImpl("user", "pass");
        user.addCollaboration("project");

        assertTrue(user.hasCollaboration("project"),
            "UserImpl::addCollaboration should add the collaboration to the user. ");
    }

    @Test
    void testAddCollaborationTwiceThrows() throws UserIsAlreadyInTheCollaboration {
        User user = new UserImpl("user", "pass");
        user.addCollaboration("project");

        assertThrows(UserIsAlreadyInTheCollaboration.class, () -> user.addCollaboration("project"),
            "UserImpl::addCollaboration should throw UserIsAlreadyInTheCollaboration " +
                "when adding the same collaboration twice. ");
    }

    @Test
    void testRemoveCollaborationWhenNotInIt() {
        User user = new UserImpl("user", "pass");

        assertThrows(UserNotInCollaboration.class, () -> user.removeCollaboration("project"),
            "UserImpl::removeCollaboration should throw UserNotInCollaboration " +
                "when user is not part of the collaboration. ");
    }

    @Test
    void testRemoveCollaborationCorrectly() throws UserIsAlreadyInTheCollaboration, UserNotInCollaboration, WrongPassword {
        User user = new UserImpl("user", "pass");
        user.addCollaboration("project");
        user.removeCollaboration("project");

        assertFalse(user.hasCollaboration("project"),
            "UserImpl::removeCollaboration should remove the collaboration from the user. ");
    }
}
