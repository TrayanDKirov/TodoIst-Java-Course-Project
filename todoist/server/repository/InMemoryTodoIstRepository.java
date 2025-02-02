package bg.sofia.uni.fmi.mjt.project.todoist.server.repository;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.UserNameIsTakenException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;

import java.io.File;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class InMemoryTodoIstRepository implements TodoIstRepository {
    private static final String DATA_FILE_EXTENSION = ".dat";

    private Map<String, User> users;
    private File dataFile;

    private boolean isLoaded = true;
    private boolean isSaved;

    private boolean isFileValid(File file) {
        return file.isFile() && file.getName().endsWith(DATA_FILE_EXTENSION);
    }

    public InMemoryTodoIstRepository(Path pathToDataFile) {
        if (pathToDataFile == null) {
            throw new IllegalArgumentException("Null reference in InMemoryTodoIstRepository constructor. ");
        }

        File file = pathToDataFile.toFile();
        if (!isFileValid(file)) {
            throw new IllegalArgumentException("Path isn't to a valid file(should be to .dat file). ");
        }

        this.users = new LinkedHashMap<>();
        this.dataFile = file;
    }

    @Override
    public void load() {

    }

    @Override
    public boolean isLoaded() {
        return this.isLoaded;
    }

    @Override
    public void save() {

    }

    @Override
    public boolean isSaved() {
        return this.isSaved;
    }

    @Override
    public boolean isUsernameTaken(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Null reference in InMemoryTodoIstRepository::isUsernameTaken");
        }

        return users.containsKey(username);
    }

    @Override
    public User getUser(String username) throws UserNotFoundException {
        if (username == null) {
            throw new IllegalArgumentException("Null reference in InMemoryTodoIstRepository::getUser");
        }

        User toReturn = users.get(username);
        if (toReturn != null) {
            return toReturn;
        }

        throw new UserNotFoundException("User with this user name was not found. ");
    }

    @Override
    public void addUser(User user) throws UserNameIsTakenException {
        if (user == null) {
            throw new IllegalArgumentException("Null reference in InMemoryTodoIstRepository::addUser");
        }

        if (isUsernameTaken(user.getUsername())) {
            throw new UserNameIsTakenException("User name is already taken. ");
        }

        users.put(user.getUsername(), user);
    }
}
