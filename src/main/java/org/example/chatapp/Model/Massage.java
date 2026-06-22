package org.example.chatapp.Model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Massage implements Serializable {
    private String sender;
    private String receiver;
    private String massage;
    private LocalDateTime timestamp;
    public enum type{CHAT,LOGIN,LOGOUT,PRIVATE}

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
