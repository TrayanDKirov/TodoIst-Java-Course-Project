package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.colaborationcommands;

import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.Command;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;

public abstract class CollaborationCommand implements Command {
    protected User user;
    protected String passwordOfUser;
    protected TodoIstRepository repository;

    public CollaborationCommand(User user, String passwordOfUser, TodoIstRepository repository) {
        if (user == null || passwordOfUser == null || repository == null) {
            throw new IllegalArgumentException("Null reference in CollaborationCommand constructor. ");
        }

        this.user = user;
        this.passwordOfUser = passwordOfUser;
        this.repository = repository;
    }
}
