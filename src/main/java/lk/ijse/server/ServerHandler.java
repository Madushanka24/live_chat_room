package lk.ijse.server;

import lk.ijse.client.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerHandler {
    private ServerSocket serverSocket;
    private Socket socket;
    private static ServerHandler serverHandler;


    private List<ClientHandler> clients = new ArrayList<>();

    private ServerHandler() throws IOException {
        serverSocket = new ServerSocket(3002);
    }
    public static ServerHandler getInstance() throws IOException {
        return serverHandler !=null? serverHandler :(serverHandler =new ServerHandler());
    }
    public void createSocket(){
        while (!serverSocket.isClosed()){
            try{
                socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket,clients);
                clients.add(clientHandler);
                System.out.println("client socket accepted "+socket.toString());
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
