package org.example.chatapp.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.example.chatapp.Model.Massage;
import org.example.chatapp.Model.User;
import org.example.chatapp.Service.ChatClientService;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ChatController {
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
        Massage  massage = new Massage(
                currentUser.getUserName(),"",content, Massage.Type.CHAT, LocalDateTime.now());
        chatClientService.sendMessage(massage);


        listMessages.getItems().add(content);
        txtMessage.clear();
    }
    protected void onLogoutClick(){
        Massage massage = new Massage(
                currentUser.getUserName(),"","", Massage.Type.LOGOUT,LocalDateTime.now());
        chatClientService.sendMessage(massage);
        chatClientService.disconnect();

        //navigate to login ui

    }

    private void handleIncomingMessage(Massage massage) {
        Platform.runLater(() -> {
           switch (massage.getType()){
               case CHAT:
                   listMessages.getItems().add(massage.getSender()+" : " +massage.getMassage());
                   break;
               case USER_LIST:
                   updateUserList(massage.getMassage());
                   break;
           }
        });
    }

    private void updateUserList(String massage) {
        listUsers.getItems().clear();
        if (!massage.isEmpty()){
            String[] users = massage.split(",");
            for (String user : users){
                listUsers.getItems().add(user);
            }
        }
    }
}
