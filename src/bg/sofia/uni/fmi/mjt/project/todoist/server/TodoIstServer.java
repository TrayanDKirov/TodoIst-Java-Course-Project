package bg.sofia.uni.fmi.mjt.project.todoist.server;

import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.InMemoryTodoIstRepository;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TodoIstServer {
    private static final int PORT = 7777;

    private InMemoryTodoIstRepository repository;
    private boolean isServerWorking;
    private File repositoryDataFile;

    private ServerSocket serverSocket;
    private Set<ClientHandler> clients;

    private ExecutorService executor;

    public TodoIstServer(File repositoryDataFile) {
        this.repository = InMemoryTodoIstRepository.loadRepository(repositoryDataFile);

        this.repositoryDataFile = repositoryDataFile;
        this.clients = new HashSet<>();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT);
             ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            this.serverSocket = serverSocket;

            isServerWorking = true;
            System.out.println("Server is working. ");

            Socket currClientSocket;

            while (isServerWorking) {
                currClientSocket = serverSocket.accept();
                System.out.println("Connected to client. ");

                ClientHandler currClientHandler = new ClientHandler(currClientSocket, repository);
                clients.add(currClientHandler);

                executor.execute(currClientHandler);
            }

        } catch (IOException ex) {
            if (isServerWorking) {
                throw new RuntimeException("Server had a io error and stopped. ");
            }
        }
    }

    public void stop() {
        isServerWorking = false;

        for (ClientHandler currClient : clients) {
            currClient.stop();
        }

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException exception) {
            throw new RuntimeException("Error with closing the server. ", exception);
        }

        InMemoryTodoIstRepository.saveRepository(repositoryDataFile, repository);
        System.out.println("Server is stopped. ");
    }
}
