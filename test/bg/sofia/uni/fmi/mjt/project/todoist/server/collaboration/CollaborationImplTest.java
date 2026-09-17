package bg.sofia.uni.fmi.mjt.project.todoist.server.collaboration;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.UserIsAlreadyInTheCollaboration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollaborationImplTest {
    @Test
    void testConstructorWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> new CollaborationImpl(null, "creator"),
            "CollaborationImpl constructor should throw IllegalArgumentException when given null name. ");
    }

    @Test
    void testConstructorWithNullCreator() {
        assertThrows(IllegalArgumentException.class, () -> new CollaborationImpl("project", null),
            "CollaborationImpl constructor should throw IllegalArgumentException when given null creator. ");
    }

    @Test
    void testCreatorIsParticipantByDefault() {
        Collaboration collaboration = new CollaborationImpl("project", "creator");

        assertTrue(collaboration.isUserParticipant("creator"),
            "CollaborationImpl creator should be a participant by default. ");
    }

    @Test
    void testGetCreatorReturnsCorrectUsername() {
        Collaboration collaboration = new CollaborationImpl("project", "creator");

        assertEquals("creator", collaboration.getCreator(),
            "CollaborationImpl::getCreator should return the correct creator. ");
    }

    @Test
    void testGetNameReturnsCorrectName() {
        Collaboration collaboration = new CollaborationImpl("project", "creator");

        assertEquals("project", collaboration.getName(),
            "CollaborationImpl::getName should return the correct name. ");
    }

    @Test
    void testAddUserCorrectly() throws UserIsAlreadyInTheCollaboration {
        Collaboration collaboration = new CollaborationImpl("project", "creator");
        collaboration.addUser("newUser");

        assertTrue(collaboration.isUserParticipant("newUser"),
            "CollaborationImpl::addUser should add the user as a participant. ");
    }

    @Test
    void testAddUserTwiceThrows() throws UserIsAlreadyInTheCollaboration {
        Collaboration collaboration = new CollaborationImpl("project", "creator");
        collaboration.addUser("newUser");

        assertThrows(UserIsAlreadyInTheCollaboration.class, () -> collaboration.addUser("newUser"),
            "CollaborationImpl::addUser should throw UserIsAlreadyInTheCollaboration " +
                "when adding the same user twice. ");
    }

    @Test
    void testIsUserParticipantWithNull() {
        Collaboration collaboration = new CollaborationImpl("project", "creator");

        assertThrows(IllegalArgumentException.class, () -> collaboration.isUserParticipant(null),
            "CollaborationImpl::isUserParticipant should throw IllegalArgumentException when given null. ");
    }

    @Test
    void testGetTasksReturnsEmptyTaskCluster() {
        Collaboration collaboration = new CollaborationImpl("project", "creator");

        assertNotNull(collaboration.getTasks(),
            "CollaborationImpl::getTasks should never return null. ");
    }
}
