package org.example.chatapp.Server;

import org.example.chatapp.DataBase.DBConnection;
import org.example.chatapp.Model.Message;
import org.sqlite.core.DB;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {
    private boolean isRun = false;
    private ServerSocket serverSocket;
    private final int port = 5010;
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
            System.out.println("1   Server started");
        } catch (IOException e ) {
            throw new RuntimeException(e);
        }
        isRun = true;
        System.out.println("Server Started");
        DBConfig();

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
    private void DBConfig(){
        final  String URL = "jdbc:sqlite:chat.db";

        String createUser = """
                CREATE TABLE IF NOT EXISTS users(
                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                name TEXT  NOT NULL,
                                username TEXT UNIQUE,
                                password_hash TEXT
                            );
                """;
        String createMessage = """
                CREATE TABLE IF NOT EXISTS  message(
                    id  INTEGER PRIMARY KEY AUTOINCREMENT,
                    sender TEXT ,
                    receiver TEXT ,
                    message TEXT,
                    timestamp INTEGER DEFAULT (strftime('%s', 'now'))
                );
                """;

        try(Connection con =  DriverManager.getConnection(URL);
            Statement statement = con.createStatement()){
            statement.execute(createUser);
            statement.execute(createMessage);
            System.out.println("DB created");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        ChatServer.getInstance().startServer();
    }
}
