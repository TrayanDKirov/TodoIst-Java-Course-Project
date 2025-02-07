package bg.sofia.uni.fmi.mjt.project.todoist.server.user;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.WrongPassword;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskClusterImpl;

public class UserImpl implements User {
    private String username;
    private String password;

    private TaskCluster tasks;

    public UserImpl(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Null reference in UserImpl constructor. ");
        }

        this.username = username;
        this.password = password;

        this.tasks = new TaskClusterImpl();
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isPasswordCorrect(String passwordToCheck) {
        return password.equals(passwordToCheck);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) throws WrongPassword {
        if (!isPasswordCorrect(oldPassword)) {
            throw new WrongPassword("Old password is incorrect. ");
        }

        this.password = newPassword;
    }

    @Override
    public TaskCluster getTasks(String password) throws WrongPassword {
        if (!isPasswordCorrect(password)) {
            throw new WrongPassword("Password is incorrect. You don't have access to this data");
        }

        return this.tasks;
    }
}