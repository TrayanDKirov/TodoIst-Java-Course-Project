package bg.sofia.uni.fmi.mjt.project.todoist.server.collaboration;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.collaboration.UserIsAlreadyInTheCollaboration;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskClusterImpl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CollaborationImpl implements Collaboration, Serializable {
    private static final long serialVersionUID = 1111;

    private String collaborationName;
    private TaskCluster taskCluster;
    private Set<String> participantsUsernames;
    private String creatorUsername;

    public CollaborationImpl(String collaborationName, String creatorUsername) {
        if (collaborationName == null || creatorUsername == null) {
            throw new IllegalArgumentException("Null reference in CollaborationImpl constructor. ");
        }

        this.collaborationName = collaborationName;
        this.creatorUsername = creatorUsername;

        this.taskCluster = new TaskClusterImpl();
        this.participantsUsernames = new HashSet<>();
        this.participantsUsernames.add(creatorUsername);
    }

    @Override
    public String getName() {
        return this.collaborationName;
    }

    @Override
    public synchronized TaskCluster getTasks() {
        return this.taskCluster;
    }

    @Override
    public synchronized boolean isUserParticipant(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Null reference in Collaboration::isUserParticipant. ");
        }

        return participantsUsernames.contains(username);
    }

    @Override
    public synchronized String getCreator() {
        return this.creatorUsername;
    }

    @Override
    public synchronized void addUser(String username) throws UserIsAlreadyInTheCollaboration {
        if (username == null) {
            throw new IllegalArgumentException("Null reference in CollaborationImpl::addUser");
        }

        if (isUserParticipant(username)) {
            throw new UserIsAlreadyInTheCollaboration("User " + username + " is already in the collaboration. ");
        }

        participantsUsernames.add(username);
    }

    @Override
    public synchronized Set<String> getParticipants() {
        return Set.copyOf(participantsUsernames);
    }
}
