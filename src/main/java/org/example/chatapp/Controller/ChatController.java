package org.example.chatapp.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.example.chatapp.Model.Message;
import org.example.chatapp.Model.User;
import org.example.chatapp.Service.ChatClientService;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    public ListView<String> listMessages;
    public ListView<String> listUsers;
    public TextField txtMessage;
    public Label lblCurrentUser;

    private User currentUser;
    private ChatClientService chatClientService;

    public void initialize(URL url, ResourceBundle rb) {
        chatClientService = ChatClientService.getInstance();
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        if (chatClientService == null) {
            System.out.println("chatClientService is null");
            return;
        }
        lblCurrentUser.setText(currentUser.getName());

        chatClientService.setMessageListener(this::handleIncomingMessage);
    }
    @FXML
    protected void onSendClick(){
        String content = txtMessage.getText().trim();
        if (content.isEmpty()){
            return;
        }
        Message  message = new Message(
                currentUser.getUserName(),"",content, Message.Type.CHAT, LocalDateTime.now());
        chatClientService.sendMessage(message);


        //listMessages.getItems().add(content);
        txtMessage.clear();
    }
    @FXML
    protected void onLogoutClick(){
        Message message = new Message(
                currentUser.getUserName(),"","", Message.Type.LOGOUT,LocalDateTime.now());
        chatClientService.sendMessage(message);
        chatClientService.disconnect();

        //navigate to login ui

    }

    private void handleIncomingMessage(Message massage) {
        Platform.runLater(() -> {
           switch (massage.getType()){
               case CHAT:
                   //listMessages.getItems().add(massage.getSender()+" : " +massage.getMassage());
                   String disply = massage.getSender().equals(currentUser.getUserName())
                           ? "You: "+massage.getMassage() : massage.getSender()+" : "+massage.getMassage();
                   listMessages.getItems().add(disply);
                   break;
               case USER_LIST:
                   updateUserList(massage.getMassage());
                   break;
           }
        });
    }

    private void updateUserList(String massage) {
        System.out.println("user list called "+massage);
        listUsers.getItems().clear();
        if (!massage.isEmpty()){
            String[] users = massage.split(",");
            for (String user : users){
                System.out.println("user "+user);
                listUsers.getItems().add(user);
            }
        }
    }

}
