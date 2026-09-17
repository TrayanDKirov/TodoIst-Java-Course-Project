package bg.sofia.uni.fmi.mjt.project.todoist.server.user;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.WrongPassword;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.UserIsAlreadyInTheCollaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.UserNotInCollaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskClusterImpl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class UserImpl implements User, Serializable {
    private static final long serialVersionUID = 1111;

    private String username;
    private String password;

    private TaskCluster tasks;
    private Set<String> collaborationsNames;

    public UserImpl(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Null reference in UserImpl constructor. ");
        }

        this.username = username;
        this.password = password;

        this.tasks = new TaskClusterImpl();
        this.collaborationsNames = new HashSet<>();
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isPasswordCorrect(String passwordToCheck) {
        if (passwordToCheck == null) {
            throw new IllegalArgumentException("Null reference in UserImpl::isPasswordCorrect. ");
        }

        return password.equals(passwordToCheck);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) throws WrongPassword {
        if (oldPassword == null || newPassword == null) {
            throw new IllegalArgumentException("Null reference in UserImpl::changePassword. ");
        }

        if (!isPasswordCorrect(oldPassword)) {
            throw new WrongPassword("Old password is incorrect. ");
        }

        this.password = newPassword;
    }

    @Override
    public TaskCluster getTasks(String password) throws WrongPassword {
        if (password == null) {
            throw new IllegalArgumentException("Null reference in UserImpl::getTasks. ");
        }

        if (!isPasswordCorrect(password)) {
            throw new WrongPassword("Password is incorrect. You don't have access to this data");
        }

        return this.tasks;
    }

    @Override
    public boolean hasCollaboration(String collaborationName) {
        if (collaborationName == null) {
            throw new IllegalArgumentException("Null reference in UserImpl::hasCollaboration. ");
        }

        return collaborationsNames.contains(collaborationName);
    }

    @Override
    public void addCollaboration(String collaborationName) throws UserIsAlreadyInTheCollaboration {
        if (collaborationName == null) {
            throw new IllegalArgumentException("Null reference in UserImpl::addCollaboration. ");
        }

        if (hasCollaboration(collaborationName)) {
            throw new UserIsAlreadyInTheCollaboration("User is already in collaboration. ");
        }

        collaborationsNames.add(collaborationName);
    }

    @Override
    public void removeCollaboration(String collaborationName) throws UserNotInCollaboration {
        if (collaborationName == null) {
            throw new IllegalArgumentException("Null reference in UserImpl::removeCollaboration. ");
        }

        if (!hasCollaboration(collaborationName)) {
            throw new UserNotInCollaboration("You are not in the collaboration you want to delete. ");
        }

        collaborationsNames.remove(collaborationName);
    }

    @Override
    public Set<String> getCollaborations() {
        return Set.copyOf(collaborationsNames);
    }
}