package bg.sofia.uni.fmi.mjt.project.todoist.server.repository;

import bg.sofia.uni.fmi.mjt.project.todoist.server.collaboration.Collaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.UsernameIsTaken;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.UserNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.WrongPassword;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.CollaborationAlreadyExist;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.CollaborationCreatorException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.CollaborationNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.UserNotInCollaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;

public interface TodoIstRepository {
    boolean isUsernameTaken(String username);

    User getUser(String username) throws UserNotFound;

    void addUser(User user) throws UsernameIsTaken;

    void addCollaboration(String username, String password, String collaborationName)
        throws CollaborationAlreadyExist, UserNotFound, WrongPassword;

    void deleteCollaboration(String username, String password, String collaborationName)
        throws CollaborationNotFound, CollaborationCreatorException, UserNotFound, WrongPassword;

    Collaboration getCollaboration(String username, String password, String collaborationName)
        throws CollaborationNotFound, UserNotInCollaboration, UserNotFound, WrongPassword;
}
