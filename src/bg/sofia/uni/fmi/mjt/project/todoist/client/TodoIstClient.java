package bg.sofia.uni.fmi.mjt.project.todoist.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TodoIstClient {
    private static final int PORT = 7777;
    private static final String HOST = "localhost";

    private static final String CONNECT_TO_SERVER_SERVER_COMMAND = "connect";
    private static final String DISCONNECT_FROM_SERVER_COMMAND = "disconnected";
    private static final String QUIT_COMMAND = "quit";

    public static void readInputFromServer(BufferedReader fromServer) throws IOException {
        String input = fromServer.readLine();
        int linesToRead = Integer.parseInt(input);

        while (linesToRead != 0) {
            input = fromServer.readLine();
            System.out.println(input);

            linesToRead--;
        }
    }

    private static void connect(Scanner scanner) {
        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Connected successfully. ");

            while (true) {
                readInputFromServer(fromServer);
                System.out.print(">: ");

                String output = scanner.nextLine();
                if (output.equals("disconnect")) {
                    break;
                }

                toServer.println(output);
            }

        } catch (IOException ex) {
            System.out.println("Server is stopped. ");
        }
    }

    public static void main(String[] args) {
        System.out.println("Write connect-to connect to server.");
        System.out.println("Write disconnect-to disconnect from server.");
        System.out.println("Write quit-to terminate the program.");

        try (Scanner scanner = new Scanner(System.in)) {
            String input = scanner.nextLine();
            while (!input.equals("quit")) {
                if (input.equals("connect")) {
                    connect(scanner);
                }

                System.out.println("Possible commands are connect and quit. ");
                input = scanner.nextLine();
            }
        }

        System.out.print("Client terminated. ");
    }
}
