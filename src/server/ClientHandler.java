package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 *
 *
 * @author Jordan Hartwick
 * Jun 6, 2016
 */
public class ClientHandler {


    private List<Socket> clients = new ArrayList<>();


    public ClientHandler() {}


    public void addAndHandleConnection(Socket clientConnection) {
        clients.add(clientConnection);
        startClientHandlerThread(clientConnection);
    }


    private void startClientHandlerThread(Socket connection) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    DataInputStream in = new DataInputStream(connection.getInputStream());

                    // Listen for whatever the user sends.
                    while (!connection.isClosed()) {
                        String command = in.readUTF();
                        broadcastToUsers(command, connection);
                    }
                } catch (IOException err) {
                    System.out.println("Client " + connection.getRemoteSocketAddress() + " disconnected");
                    clients.remove(connection);
                }
            }
        }).start();
    }


    /**
     * Send the message to all users except for the one that sent the message.
     */
    private void broadcastToUsers(String command, Socket connection) throws IOException {
        for (Socket s : clients) {
            if (!s.getRemoteSocketAddress().equals(connection.getRemoteSocketAddress())) {
                DataOutputStream out = new DataOutputStream(s.getOutputStream());
                out.writeUTF(command);
                out.flush();
            }
        }
    }
}