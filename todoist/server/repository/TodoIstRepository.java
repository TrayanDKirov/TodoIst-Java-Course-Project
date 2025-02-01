package bg.sofia.uni.fmi.mjt.project.todoist.server.repository;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.UserNameIsTakenException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;

public interface TodoIstRepository {
    void load();

    boolean isLoaded();

    void save();

    boolean isSaved();

    boolean isUsernameTaken(String username);

    User getUser(String username) throws UserNotFoundException;

    void addUser(User user) throws UserNameIsTakenException;
}
