package bg.sofia.uni.fmi.mjt.project.todoist.server;

import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.accountcommand.AccountCommand;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.accountcommand.AccountCommandFactory;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.accountcommand.AccountCommandResult;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands.TaskCommand;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.taskcommands.TaskCommandFactory;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.AccountException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.CommandException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.model.TaskCluster;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private TodoIstRepository repository;
    private Socket clientSocket;

    private boolean isLoggedIn;
    private User currUser;
    private String passwordToCurrUser;

    public ClientHandler(Socket clientSocket, TodoIstRepository repository) {
        if (clientSocket == null || repository == null) {
            throw new IllegalArgumentException("Null reference in ClientHandler constructor. ");
        }

        if (!repository.isLoaded()) {
            throw new IllegalArgumentException("Repository in ClientHandler constructor should be loaded. ");
        }

        this.clientSocket = clientSocket;
        this.repository = repository;

        this.isLoggedIn = false;
    }

    private void sendOutput(PrintWriter toClient, String output) {
        String[] lines = output.split(System.lineSeparator());

        int linesToSend = lines.length;
        int index = 0;

        toClient.println(linesToSend);
        while (index != linesToSend) {
            toClient.println(lines[index]);

            index++;
        }
    }

    private void interactWithUser(PrintWriter toClient, BufferedReader fromClient)
        throws IOException, AccountException {
        if (toClient == null || fromClient == null || currUser == null || passwordToCurrUser == null) {
            throw new IllegalArgumentException("Null reference in ClientHandler::interactWithUser");
        }

        TaskCluster taskCluster = currUser.getTasks(passwordToCurrUser);

        String input;
        String output;
        while ((input = fromClient.readLine()) != null) {
            try {

                TaskCommand command = TaskCommandFactory.of(input);

                output = command.execute(taskCluster);

            } catch (CommandException commandException) {
                output = commandException.getMessage();
            }

            sendOutput(toClient, output);
        }
    }

    private void interactWithClient(PrintWriter toClient, BufferedReader fromClient) throws IOException {
        if (toClient == null || fromClient == null) {
            throw new IllegalArgumentException("Null reference in ClientHandler::interactWithClient");
        }

        String input;
        String output;
        while (true) {
            try {
                if (isLoggedIn) {
                    interactWithUser(toClient, fromClient);
                    break;
                }

                if ((input = fromClient.readLine()) == null) {
                    break;
                }

                AccountCommand accountCommand = AccountCommandFactory.of(input);
                AccountCommandResult result = accountCommand.execute(repository);

                if (result.isUserAndPasswordSet()) {
                    isLoggedIn = true;
                    currUser = result.getUser();
                    passwordToCurrUser = result.getPasswordOfUser();
                }

                output = result.getOutput();
            } catch (AccountException accountException) {
                output = accountException.getMessage();
            }

            sendOutput(toClient, output);
        }
    }

    @Override
    public void run() {
        try (PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            interactWithClient(toClient, fromClient);

        } catch (IOException ex) {
            throw new RuntimeException("Something went wrong with IO", ex);
        }
    }
}
