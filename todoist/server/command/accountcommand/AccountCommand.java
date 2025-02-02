package bg.sofia.uni.fmi.mjt.project.todoist.server.command.accountcommand;

import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.AccoutException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;

public interface AccountCommand {

    AccountCommandResult execute(TodoIstRepository repository) throws AccoutException;

}
