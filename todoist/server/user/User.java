package bg.sofia.uni.fmi.mjt.project.todoist.server.user;

import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

public interface User {
    String getUsername();

    boolean changePassword(String oldPassword, String newPassword);

    TaskCluster getTasks(String password);
}
