package bg.sofia.uni.fmi.mjt.project.todoist.server.repository;

import bg.sofia.uni.fmi.mjt.project.todoist.server.collaboration.Collaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.collaboration.CollaborationImpl;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.UsernameIsTaken;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.UserNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.WrongPassword;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.CollaborationAlreadyExist;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.CollaborationCreatorException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.CollaborationNotFound;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.UserIsAlreadyInTheCollaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.UserNotInCollaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InMemoryTodoIstRepository implements TodoIstRepository, Serializable {
    private static final long serialVersionUID = 667540538635017698L;

    private static final String DATA_FILE_EXTENSION = ".dat";

    private Map<String, User> users;
    private Map<String, Collaboration> colaborations;

    private boolean isFileValid(File file) {
        return file.isFile() && file.getName().endsWith(DATA_FILE_EXTENSION);
    }

    public InMemoryTodoIstRepository() {
        this.users = new HashMap<>();
        this.colaborations = new HashMap<>();
    }

    public static InMemoryTodoIstRepository loadRepository(File dataFile) {
        if (dataFile.length() == 0) {
            return new InMemoryTodoIstRepository();
        }

        try (ObjectInputStream objectStream = new ObjectInputStream(new FileInputStream(dataFile))) {

            return (InMemoryTodoIstRepository) objectStream.readObject();

        } catch (FileNotFoundException exception) {
            throw new RuntimeException("Error with data dile. ", exception);
        } catch (IOException exception) {
            throw new RuntimeException("Error with reading from file. ", exception);
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException("Error with class. ", exception);
        }
    }

    public static void saveRepository(File dataFile, InMemoryTodoIstRepository repository) {
        if (dataFile == null || repository == null) {
            throw new IllegalArgumentException("Null reference in InMemoryTodoIstRepository::save. ");
        }

        try (ObjectOutputStream objectStream = new ObjectOutputStream(new FileOutputStream(dataFile))) {

            objectStream.writeObject(repository);

        } catch (FileNotFoundException exception) {
            throw new RuntimeException("Error with data dile. ", exception);
        } catch (IOException exception) {
            throw new RuntimeException("Error with reading from file. ", exception);
        }
    }

    @Override
    public boolean isUsernameTaken(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Null reference in InMemoryTodoIstRepository::isUsernameTaken");
        }

        return users.containsKey(username);
    }

    @Override
    public User getUser(String username) throws UserNotFound {
        if (username == null) {
            throw new IllegalArgumentException("Null reference in InMemoryTodoIstRepository::getUser");
        }

        User toReturn = users.get(username);
        if (toReturn != null) {
            return toReturn;
        }

        throw new UserNotFound("User with this user name was not found. ");
    }

    @Override
    public void addUser(User user) throws UsernameIsTaken {
        if (user == null) {
            throw new IllegalArgumentException("Null reference in InMemoryTodoIstRepository::addUser");
        }

        if (isUsernameTaken(user.getUsername())) {
            throw new UsernameIsTaken("User name is already taken. ");
        }

        users.put(user.getUsername(), user);
    }

    @Override
    public void addCollaboration(String username, String password, String collaborationName)
        throws CollaborationAlreadyExist, UserNotFound, WrongPassword {
        if (username == null || password == null || collaborationName == null) {
            throw new IllegalArgumentException("Null reference in InMemoryTodoIstRepository::addCollaboration. ");
        }

        if (colaborations.containsKey(collaborationName)) {
            throw new CollaborationAlreadyExist("Collaboration with this name already exists in the system. ");
        }

        User creator = getUser(username);
        if (!creator.isPasswordCorrect(password)) {
            throw new WrongPassword("Wrong password for creator of collaboration. ");
        }

        try {
            creator.addCollaboration(collaborationName);
        } catch (UserIsAlreadyInTheCollaboration exception) {
            throw new RuntimeException("UserIsAlreadyInTheCollaboration should not be thrown. ", exception);
        }

        Collaboration collaboration = new CollaborationImpl(collaborationName, username);

        colaborations.put(collaborationName, collaboration);
    }

    @Override
    public void deleteCollaboration(String username, String password, String collaborationName)
        throws CollaborationNotFound, CollaborationCreatorException, UserNotFound, WrongPassword {
        if (username == null || password == null || collaborationName == null) {
            throw new IllegalArgumentException("Null reference in InMemoryTodoIstRepository::deleteCollaboration. ");
        }

        if (!colaborations.containsKey(collaborationName)) {
            throw new CollaborationNotFound("Collaboration with this name does not exist in the system. ");
        }

        Collaboration collaboration = colaborations.get(collaborationName);
        if (!getUser(username).isPasswordCorrect(password)) {
            throw new WrongPassword("Password of creator is invalid. ");
        }

        if (!collaboration.getCreator().equals(username)) {
            throw new CollaborationCreatorException(
                "Collaboration can only be delete by it's creator - " + collaboration.getCreator() + ". ");
        }

        Set<String> participants = collaboration.getParticipants();
        try {
            for (String currParticipant : participants) {
                getUser(currParticipant).removeCollaboration(collaborationName);
            }
        } catch (UserNotInCollaboration exception) {
            throw new RuntimeException("UserNotInCollaboration should not be thrown. ", exception);
        }

        colaborations.remove(collaborationName);
    }

    @Override
    public Collaboration getCollaboration(String username, String password, String collaborationName)
        throws CollaborationNotFound, UserNotInCollaboration, UserNotFound, WrongPassword {
        if (username == null || password == null || collaborationName == null) {
            throw new IllegalArgumentException("Null reference in InMemoryTodoIstRepository::getCollaboration. ");
        }

        User user = getUser(username);
        if (!user.isPasswordCorrect(password)) {
            throw new WrongPassword("Password of user is invalid when trying to get collaboration. ");
        }

        if (!colaborations.containsKey(collaborationName)) {
            throw new CollaborationNotFound("Collaboration with this name does not exist in the system. ");
        }

        Collaboration collaboration = colaborations.get(collaborationName);
        if (!collaboration.isUserParticipant(username) || !user.hasCollaboration(collaborationName)) {
            throw new UserNotInCollaboration("You are not in this collaboration. ");
        }

        return collaboration;
    }
}
