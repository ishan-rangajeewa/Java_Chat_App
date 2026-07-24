package org.example.chatapp.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    public ListView<String> listMessages;
    public ListView<String> listUsers;
    public TextField txtMessage;
    public Label lblCurrentUser;

    private User currentUser;
    private String selectedUser = "";
    private ChatClientService chatClientService;
    private static String group = "group";
    private  Map<String, ObservableList<String>> conversations = new HashMap<>();

    public void initialize(URL url, ResourceBundle rb) {
        chatClientService = ChatClientService.getInstance();

        conversations.put(group, FXCollections.observableArrayList());
        listMessages.setItems(conversations.get(group));

        listUsers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           if (newValue != null) {
               openConversationWith(newValue);
           }
        });

    }

    private void openConversationWith(String UserName) {
        selectedUser = UserName;
        conversations.putIfAbsent(UserName, FXCollections.observableArrayList());
        listMessages.setItems(conversations.get(UserName));
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
        System.out.println("Selected User: " + selectedUser);
        System.out.println("Current User: " + currentUser.getUserName());
        if(!(selectedUser.isEmpty())){
            Message  message = new Message(
                    currentUser.getUserName(),selectedUser,content, Message.Type.PRIVATE, LocalDateTime.now());
            chatClientService.sendMessage(message);
            conversations.get(selectedUser).add("You: "+content);
        }
        else{
            Message message = new Message(
                 currentUser.getUserName(),"",content, Message.Type.CHAT, LocalDateTime.now()
            );
            chatClientService.sendMessage(message);
            conversations.get(group).add("You: "+content);
        }

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

    private void handleIncomingMessage(Message message) {
        Platform.runLater(() -> {
           switch (message.getType()){
               case CHAT:
                   String display = message.getSender().equals(currentUser.getUserName())
                           ? "You: " + message.getMassage() : message.getSender()+ " : "+message.getMassage();
                   conversations.get(group).add(display);
                   break;
               case PRIVATE:
                   String partner = message.getSender().equals(currentUser.getUserName())
                           ? message.getReceiver() : message.getSender();
                   conversations.putIfAbsent(partner, FXCollections.observableArrayList());
                   String privateDisplay = message.getSender().equals(currentUser.getUserName())
                           ? "You: " + message.getMassage() : message.getSender() + " : "+message.getMassage();
                   conversations.get(partner).add(privateDisplay);

                   break;
               case USER_LIST:
                   updateUserList(message.getMassage());
                   break;
           }
        });
    }

    private void updateUserList(String message) {
        System.out.println("user list called "+message);
        String previouslySelected = listUsers.getSelectionModel().getSelectedItem();
        listUsers.getItems().clear();
        if(!message.isEmpty()){
            String[] users = message.split(",");
            for (String user : users){
                if(currentUser != null && user.equals(currentUser.getUserName())){
                    continue;
                }
                listUsers.getItems().add(user);
            }
        }
        if(previouslySelected != null && listUsers.getItems().contains(previouslySelected)){
            listUsers.getSelectionModel().select(previouslySelected);
        }
    }

}
