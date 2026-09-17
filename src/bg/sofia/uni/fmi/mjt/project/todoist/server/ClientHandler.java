package bg.sofia.uni.fmi.mjt.project.todoist.server;

import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.Command;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.CommandFactory;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.accountcommand.AccountCommandFactory;
import bg.sofia.uni.fmi.mjt.project.todoist.server.commands.accountcommand.AccountCommandResult;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.account.AccountException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.exception.CommandException;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class ClientHandler implements Runnable {
    private TodoIstRepository repository;
    private Socket clientSocket;

    private boolean isLoggedIn;
    private User currUser;
    private String passwordToCurrUser;

    private BufferedReader fromClient;
    private PrintWriter toClient;

    private boolean isRunning;

    public ClientHandler(Socket clientSocket, TodoIstRepository repository) {
        if (clientSocket == null || repository == null) {
            throw new IllegalArgumentException("Null reference in ClientHandler constructor. ");
        }

        this.clientSocket = clientSocket;
        this.repository = repository;

        this.isLoggedIn = false;
        this.isRunning = false;
    }

    private void sendOutput(String output) {
        String[] lines = output.split(System.lineSeparator());

        int linesToSend = lines.length;
        int index = 0;

        toClient.println(linesToSend);
        while (index != linesToSend) {
            toClient.println(lines[index]);

            index++;
        }
    }

    private void interactWithUser()
        throws IOException, AccountException {
        if (toClient == null || fromClient == null || currUser == null || passwordToCurrUser == null) {
            throw new IllegalArgumentException("Null reference in ClientHandler::interactWithUser");
        }

        CommandFactory commandFactory = new CommandFactory(currUser, passwordToCurrUser, repository);

        String input;
        String output;
        while ((input = fromClient.readLine()) != null) {
            try {

                Command command = commandFactory.of(input);

                output = command.execute();

            } catch (CommandException commandException) {
                output = commandException.getMessage();
            }

            sendOutput(output);
        }
    }

    private void interactWithClient() throws IOException {
        if (toClient == null || fromClient == null) {
            throw new IllegalArgumentException("Null reference in ClientHandler::interactWithClient");
        }

        String input;
        String output;
        while (true) {
            try {
                if (isLoggedIn) {
                    interactWithUser();
                    break;
                }
                if ((input = fromClient.readLine()) == null) {
                    break;
                }

                AccountCommandResult result = AccountCommandFactory.of(input).execute(repository);
                if (result.isUserAndPasswordSet()) {
                    isLoggedIn = true;
                    currUser = result.getUser();
                    passwordToCurrUser = result.getPasswordOfUser();
                }
                output = result.getOutput();
            } catch (AccountException accountException) {
                output = accountException.getMessage();
            }
            sendOutput(output);
        }
    }

    @Override
    public void run() {
        try (PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            isRunning = true;
            this.toClient = toClient;
            this.fromClient = fromClient;

            sendOutput("Write commands to the server. Use help command if you need help. ");
            interactWithClient();

        } catch (IOException ex) {
            if (currUser != null) {
                System.out.println("Client disconnected - " + currUser.getUsername() + ".");
            } else {
                System.out.println("Client disconnected.");
            }
        }
    }

    public void stop() {
        isRunning = false;

        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                sendOutput("Server is stopping. Goodbye. ");
                clientSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing client socket: " + e.getMessage());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(clientSocket);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof ClientHandler other) {
            if (this == other) {
                return true;
            }

            return this.clientSocket.equals(other.clientSocket);
        }

        return false;
    }
}
