package bg.sofia.uni.fmi.mjt.project.todoist.server.user;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.WrongPassword;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.UserIsAlreadyInTheCollaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.UserNotInCollaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

import java.util.Set;

public interface User {
    String getUsername();

    boolean isPasswordCorrect(String passwordToCheck);

    void changePassword(String oldPassword, String newPassword) throws WrongPassword;

    TaskCluster getTasks(String password) throws WrongPassword;

    boolean hasCollaboration(String collaborationName) throws WrongPassword;

    void addCollaboration(String collaborationName) throws UserIsAlreadyInTheCollaboration;

    void removeCollaboration(String collaborationName) throws UserNotInCollaboration;

    Set<String> getCollaborations();
}
