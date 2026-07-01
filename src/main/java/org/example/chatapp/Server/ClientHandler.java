package org.example.chatapp.Server;
import org.example.chatapp.Model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String userName;
    private boolean isruning = true;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    @Override
    public void run() {

        try {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(clientSocket.getInputStream());

            while (isruning) {
                Message message = (Message) input.readObject();
                handleMessage(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            disconect(); 
        }
    }

    private void handleMessage(Message message) {
        switch (message.getType()) {
            case LOGIN:
                handleLogin(message);
                break;
            case LOGOUT:
                handleLogout(message);
                break;
            case CHAT:
                handleChat(message);
                break;
            case PRIVATE:
                handlePrivate(message);
                break;
        }
    }

    private void handleLogout(Message massage) {
        isruning = false;
        Message leave = new Message(
                "SERVER","",userName+" Left",Message.Type.LOGOUT,LocalDateTime.now());
//        ChatServer.getInstance().broadcast(leave);
//        sendUserListForAll();
        sendUserList();
    }

    private void handlePrivate(Message message) {
        ChatServer.getInstance().sendtoUser(message.getReceiver(),message);
    }

    private void handleChat(Message message) {
        ChatServer.getInstance().broadcast(message);
    }

    private void handleLogin(Message message) {
        this.userName = message.getSender();
        System.out.println("Client " + userName + " logged in");
        Message joinMassage = new Message(
                "Server","Client",userName+" Joined Chat", Message.Type.CHAT, LocalDateTime.now());
        ChatServer.getInstance().broadcast(joinMassage);
        sendUserList();
    }

    private void sendUserList() {
        String userList = String.join(",",ChatServer.getInstance().getOnlineUsers());
        Message message = new Message("SERVER",userList,"", Message.Type.USER_LIST,null);
        message.setMassage(String.join(",",ChatServer.getInstance().getOnlineUsers()));
//        sendMssege(massage);
        ChatServer.getInstance().broadcast(message);
    }

    public void sendMssege(Message message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getUserName(){
        return userName;
    }


    private void disconect() {
        ChatServer.getInstance().removeClient(this);
        if (userName != null) {
            Message leaveMsg = new Message(
                    "Server","",userName+" Disconected",Message.Type.CHAT,LocalDateTime.now());
            ChatServer.getInstance().broadcast(leaveMsg);
        }
        if (clientSocket != null) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
