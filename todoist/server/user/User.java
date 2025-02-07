package bg.sofia.uni.fmi.mjt.project.todoist.server.user;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongPassword;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

public interface User {
    String getUsername();

    boolean isPasswordCorrect(String passwordToCheck);

    void changePassword(String oldPassword, String newPassword) throws WrongPassword;

    TaskCluster getTasks(String password) throws WrongPassword;
}
