package bg.sofia.uni.fmi.mjt.project.todoist.server.user;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongPasswordException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

public interface User {
    String getUsername();

    boolean isPasswordCorrect(String passwordToCheck);

    void changePassword(String oldPassword, String newPassword) throws WrongPasswordException;

    TaskCluster getTasks(String password) throws WrongPasswordException;
}
