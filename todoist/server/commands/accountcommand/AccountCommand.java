package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.accountcommand;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.AccountException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;

public interface AccountCommand {

    AccountCommandResult execute(TodoIstRepository repository) throws AccountException;

}
