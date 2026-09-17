package bg.sofia.uni.fmi.mjt.project.todoist.server.collaboration;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.UserIsAlreadyInTheCollaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;

import java.util.Set;

public interface Collaboration {
    String getName();

    TaskCluster getTasks();

    String getCreator();

    boolean isUserParticipant(String username);

    void addUser(String username) throws UserIsAlreadyInTheCollaboration;

    Set<String> getParticipants();
}
