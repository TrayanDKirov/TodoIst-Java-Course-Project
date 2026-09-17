package bg.sofia.uni.fmi.mjt.project.todoist.server;

import java.nio.file.Path;
import java.util.Scanner;

public class ServerRunner {
    private static final String DATA_FILE_PATH = "repository.dat";

    private static final String START_SERVER_COMMAND = "start";
    private static final String STOP_SERVER_COMMAND = "stop";
    private static final String QUIT_COMMAND = "quit";

    private static TodoIstServer server;

    private static void printTheRules() {
        System.out.println("Write start-to start the server. ");
        System.out.println("Write stop-to stop the server. ");
        System.out.println("Write quit-to quit the program. ");
    }

    private static void readConsoleCommands(Scanner scanner) throws InterruptedException {
        Thread serverThread = null;
        String command = scanner.nextLine();

        while (!command.equals(QUIT_COMMAND)) {
            switch (command) {
                case START_SERVER_COMMAND -> {
                    if (serverThread == null || !serverThread.isAlive()) {
                        serverThread = new Thread(server::start);
                        serverThread.start();
                    } else {
                        System.out.println("Server is already running.");
                    }
                }
                case STOP_SERVER_COMMAND -> {
                    server.stop();
                    if (serverThread != null) {
                        serverThread.join();
                        serverThread = null;
                    }
                }
                default -> System.out.println("Unknown command. ");
            }

            command = scanner.nextLine();
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in);) {
            server = new TodoIstServer(Path.of(DATA_FILE_PATH).toFile());

            printTheRules();

            readConsoleCommands(scanner);
        } catch (InterruptedException exception) {
            throw new RuntimeException("");
        }

        System.out.println("Runner is stopped. ");
    }
}
