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

    public static void readInputFromServer(BufferedReader fromServer) throws IOException {
        String input = fromServer.readLine();
        int linesToRead = Integer.parseInt(input);

        while (linesToRead != 0) {
            input = fromServer.readLine();
            System.out.println(input);

            linesToRead--;
        }
    }

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected successfully. ");
            System.out.print("> ");

            while (true) {
                String output = scanner.nextLine();

                toServer.println(output);

                readInputFromServer(fromServer);
                System.out.print("> ");
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
