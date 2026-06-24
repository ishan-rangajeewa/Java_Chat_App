package org.example.chatapp.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.chatapp.DataBase.UserData;
import org.example.chatapp.Model.User;
import org.example.chatapp.Service.ChatClientService;

import java.io.IOException;

public class LoginController {

    public PasswordField logpswd;
    public TextField logUname;
    private ChatClientService chatClientService;

    public void initialize(){
        chatClientService = ChatClientService.getInstance();
    }

    public void loginOnAction(ActionEvent actionEvent) {
        String password = logpswd.getText();
        String userName = logUname.getText();
        UserData userData = UserData.getInstance();
        boolean isvalidate = userData.UserLoginValidate(userName,password);
        if(isvalidate){
            //goto chat interface
             navigateToChat(actionEvent,new User(userName,userName,password));

        }
        else{
            //send error massage to label
        }
    }

    public void goHomeOnAction(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/chatapp/View/hello-view.fxml"));
        Scene loginScene = null;
        try {
            loginScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.setScene(loginScene);
        currentStage.setTitle("welcome");
        currentStage.show();
    }

    private void navigateToChat(ActionEvent event, User user){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/chatapp/View/chatView.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            ChatController chatController = fxmlLoader.getController();
            chatController.setCurrentUser(user);

            Stage stage = (Stage) logUname.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("chat - "+user.getUserName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


}
