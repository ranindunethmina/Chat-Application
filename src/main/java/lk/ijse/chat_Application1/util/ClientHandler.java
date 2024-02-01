package lk.ijse.chat_Application1.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    private Socket socket;
    private ArrayList<ClientHandler> clients;
    private BufferedReader reader;
    private PrintWriter writer;

    public ClientHandler(Socket socket, ArrayList<ClientHandler> clients) {
        try {
            this.socket = socket;
            this.clients = clients;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                if (message.equalsIgnoreCase("bye")) {
                    break;
                }
                for (ClientHandler clientHandler : clients) {
                    clientHandler.writer.println(message);
                }
            }
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
