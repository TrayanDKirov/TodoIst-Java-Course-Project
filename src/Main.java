import bg.sofia.uni.fmi.mjt.project.todoist.server.TodoIstServer;
import bg.sofia.uni.fmi.mjt.project.todoist.server.repository.InMemoryTodoIstRepository;

import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        /*TodoIstServer server = new TodoIstServer(Path.of("repository.dat").toFile());
        Thread thread = new Thread() {
            @Override
            public void run() {
                server.start();
            }
        };

        thread.start();

        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();

        if (str.equals("q")) {
            server.stop();

            System.out.println("Server stopped. ");
        }*/

        InMemoryTodoIstRepository repository = new InMemoryTodoIstRepository();
        InMemoryTodoIstRepository.saveRepository(Path.of("repository.dat").toFile(), repository);
    }
}