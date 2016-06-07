package server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;


/**
 *
 *
 * @author Jordan Hartwick
 * Jun 6, 2016
 */
public class MorseCodeTransmitterServer extends Thread {


    private ServerSocket serverSocket;


    public static void main(String[] args) {
        try {
            PrintStream out = new PrintStream("log.txt");
            System.setErr(out);
        } catch (IOException err) {
            
        }
        
        new MorseCodeTransmitterServer(7879).start();
    }


    public MorseCodeTransmitterServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException err) {
            err.printStackTrace();
        }
    }


    /*
 		Accept connections and display who has connected.
     */
    @Override
    public void run() {
        ClientHandler clientHandler = new ClientHandler();

        System.out.println("----------------------------------------------------------------");
        System.out.println("Server is active");
        System.out.println("----------------------------------------------------------------");
        System.out.println("Waiting for connections!!!");

        while(serverSocket.isBound()) {
            try {
                Socket clientConnection = serverSocket.accept();
                System.out.println("Client connected from: " + clientConnection.getRemoteSocketAddress());
                clientHandler.addAndHandleConnection(clientConnection);
            } catch (IOException err) {
                System.err.println("Error listening for clients to connect!");
            }
        }
    }
}