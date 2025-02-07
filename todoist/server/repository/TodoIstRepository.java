package bg.sofia.uni.fmi.mjt.project.todoist.server.repository;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.UsernameIsTaken;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.UserNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;

import java.nio.file.Path;

public interface TodoIstRepository {
    void setDataFilePath(Path pathToDataFile);

    void load();

    boolean isLoaded();

    void save();

    boolean isSaved();

    boolean isUsernameTaken(String username);

    User getUser(String username) throws UserNotFound;

    void addUser(User user) throws UsernameIsTaken;
}
