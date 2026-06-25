package org.example.chatapp.Service;

import javafx.application.Platform;
import org.example.chatapp.Model.Massage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class ChatClientService {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private final int serverPort = 5000;
    private boolean isConnected = false;

    private Consumer<Massage> massageListener;

    private static ChatClientService instance;

    private ChatClientService() {}

    public void Initialize() {

    }

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
                Massage massage =(Massage) input.readObject();

                if(massageListener != null) {
                    Platform.runLater(() -> massageListener.accept(massage));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void sendMessage(Massage massage) {
        if(isConnected && output!=null) {
            try {
                output.writeObject(massage);
                output.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void setMessageListener(Consumer<Massage> listener) {
        this.massageListener = listener;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void disconnect() {
        isConnected = false;
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
