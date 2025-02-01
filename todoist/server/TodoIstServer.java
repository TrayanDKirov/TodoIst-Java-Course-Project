package bg.sofia.uni.fmi.mjt.project.todoist.server;

import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.InMemoryTodoIstRepository;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.TodoIstRepository;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TodoIstServer {
    private static final int PORT = 7777;

    private TodoIstRepository repository;
    private boolean isServerWorking;

    public TodoIstServer(Path repositoryFilePath) {
        this.repository = new InMemoryTodoIstRepository(repositoryFilePath);
    }

    void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT);
             ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            isServerWorking = true;
            repository.load();

            Socket currClientSocket;

            while (isServerWorking) {
                currClientSocket = serverSocket.accept();

                ClientHandler currClientHandler = new ClientHandler(currClientSocket, repository);

                executor.execute(currClientHandler);
            }

        } catch (IOException ex) {
            throw new RuntimeException("Error occurred", ex);
        }
    }

    void stop() {
        isServerWorking = false;

        repository.save();
    }
}
