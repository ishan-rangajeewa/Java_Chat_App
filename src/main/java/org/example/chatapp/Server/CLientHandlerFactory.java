package org.example.chatapp.Server;

import java.net.Socket;

public class CLientHandlerFactory {
    public static ClientHandler createHandler(Socket socket){
        return new ClientHandler(socket);
    }
}
