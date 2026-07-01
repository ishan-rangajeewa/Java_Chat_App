package org.example.chatapp.Model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private String sender;
    private String receiver;
    private String massage;
    private Type type;
    private LocalDateTime timestamp;

    public enum Type{CHAT,LOGIN,LOGOUT,PRIVATE,USER_LIST}

    public Message() {
    }

    public Message(String sender, String receiver, String massage, Type type, LocalDateTime timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.massage = massage;
        this.type = type;
        this.timestamp = timestamp;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
