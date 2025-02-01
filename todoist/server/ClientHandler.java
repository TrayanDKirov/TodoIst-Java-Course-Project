package bg.sofia.uni.fmi.mjt.project.todoist.server;

import bg.sofia.uni.fmi.mjt.project.todoist.server.command.AccountCommand;
import bg.sofia.uni.fmi.mjt.project.todoist.server.command.AccountCommandResult;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.AccoutException;
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

    private void interactWithUser(PrintWriter toClient, BufferedReader fromClient) throws IOException {

    }

    private void interactWithClient(PrintWriter toClient, BufferedReader fromClient) throws IOException {
        if (toClient == null || fromClient == null) {
            throw new IllegalArgumentException("Null reference in ClientHandler::interactWithClient");
        }

        String input;
        String output;
        while ((input = fromClient.readLine()) != null) {
            try {
                AccountCommand accountCommand = AccountCommand.of(input);

                AccountCommandResult result = accountCommand.execute(repository);

                isLoggedIn = true;
                currUser = result.user();
                passwordToCurrUser = result.passwordOfUser();

                output = result.output();
            } catch (AccoutException ex) {
                output = ex.getMessage();
            }

            toClient.println(output);
            if (isLoggedIn) {
                interactWithUser(toClient, fromClient);
            }
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
