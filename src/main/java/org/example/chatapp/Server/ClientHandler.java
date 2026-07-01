package org.example.chatapp.Server;
import org.example.chatapp.Model.Massage;

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
                Massage massage = (Massage) input.readObject();
                handleMessage(massage);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            disconect(); 
        }
    }

    private void handleMessage(Massage massage) {
        switch (massage.getType()) {
            case LOGIN:
                handleLogin(massage);
                break;
            case LOGOUT:
                handleLogout(massage);
                break;
            case CHAT:
                handleChat(massage);
                break;
            case PRIVATE:
                handlePrivate(massage);
                break;
        }
    }

    private void handleLogout(Massage massage) {
        isruning = false;
        Massage leave = new Massage(
                "SERVER","",userName+" Left",Massage.Type.LOGOUT,LocalDateTime.now());
//        ChatServer.getInstance().broadcast(leave);
//        sendUserListForAll();
        sendUserList();
    }

    private void handlePrivate(Massage massage) {
        ChatServer.getInstance().sendtoUser(massage.getReceiver(),massage);
    }

    private void handleChat(Massage massage) {
        ChatServer.getInstance().broadcast(massage);
    }

    private void handleLogin(Massage massage) {
        this.userName = massage.getSender();
        System.out.println("Client " + userName + " logged in");
        Massage joinMassage = new Massage(
                "Server","Client",userName+" Joined Chat", Massage.Type.CHAT, LocalDateTime.now());
        ChatServer.getInstance().broadcast(joinMassage);
        sendUserList();
    }

    private void sendUserList() {
        String userList = String.join(",",ChatServer.getInstance().getOnlineUsers());
        Massage massage = new Massage("SERVER",userList,"", Massage.Type.USER_LIST,null);
        massage.setMassage(String.join(",",ChatServer.getInstance().getOnlineUsers()));
//        sendMssege(massage);
        ChatServer.getInstance().broadcast(massage);
    }

    public void sendMssege(Massage massage) {
        try {
            output.writeObject(massage);
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
            Massage leaveMsg = new Massage(
                    "Server","",userName+" Disconected",Massage.Type.CHAT,LocalDateTime.now());
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
