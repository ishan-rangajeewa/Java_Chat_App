package org.example.chatapp.Server;

import org.example.chatapp.Model.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {
    private boolean isRun = false;
    private ServerSocket serverSocket;
    private final int port = 5000;
    private final List<ClientHandler> clients;

    private static ChatServer instance;

    private ChatServer(){
        clients = new CopyOnWriteArrayList<>();
    }
    public static synchronized ChatServer getInstance(){
        if(instance == null){
            instance = new ChatServer();
        }
        return instance;
    }
    public void startServer(){
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        isRun = true;
        System.out.println("Server Started");

        while(isRun){
            Socket clientScoket = null;
            try {
                clientScoket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("New Client Connected");

            ClientHandler handler = CLientHandlerFactory.createHandler(clientScoket);
            clients.add(handler);
            new Thread(handler).start();
        }

        }
    public void broadcast(Message message){
        for(ClientHandler client : clients){
            client.sendMssege(message);
        }
    }
    public void sendtoUser(String username, Message message){
        for(ClientHandler client : clients){
            if(client.getUserName() != null && client.getUserName().equals(username)){
                client.sendMssege(message);
                break;
            }
        }
    }
    public void removeClient(ClientHandler client){
        clients.remove(client);
        System.out.println("Removed Client");
    }
    public List<String> getOnlineUsers() {
        List<String> users = new ArrayList<>();
        for (ClientHandler client : clients) {
            if (client.getUserName() != null) {
                users.add(client.getUserName());
            }
        }
        System.out.println("Online Users " +  users);
        return users;
    }


        public void stopServer(){
        isRun = false;
        if(serverSocket != null){
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void main(String[] args) {
        ChatServer.getInstance().startServer();
    }
}
