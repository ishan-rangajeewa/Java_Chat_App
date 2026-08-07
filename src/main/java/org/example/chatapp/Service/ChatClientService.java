package org.example.chatapp.Service;

import javafx.application.Platform;
import org.example.chatapp.Model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class ChatClientService {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private final int serverPort = 5010;
    private boolean isConnected = false;

    private Consumer<Message> messageListener;

    private static ChatClientService instance;

    private ChatClientService() {}



    public static synchronized ChatClientService getInstance() {
        if (instance == null) {
            instance = new ChatClientService();
        }
        return instance;
    }
    public boolean connect() {
        try {
            socket = new Socket("127.0.0.1",serverPort);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            isConnected = true;

            new Thread(this::listenForMessages).start();
            return true;
        } catch (IOException e ) {
            throw new RuntimeException(e);

        }
    }

    private void listenForMessages() {
        while (isConnected) {
            try {
                Message message =(Message) input.readObject();

                if(messageListener != null) {
                    Platform.runLater(() -> messageListener.accept(message));
                }
            } catch (IOException | ClassNotFoundException  e) {
                isConnected = false;
                break;
            }
        }
    }


    public void sendMessage(Message message) {
        if(isConnected && output!=null) {
            try {
                output.writeObject(message);
                output.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void setMessageListener(Consumer<Message> listener) {
        this.messageListener = listener;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void disconnect() {
        isConnected = false;
        try {
            if (socket != null) socket.close();
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
    }
    
}
