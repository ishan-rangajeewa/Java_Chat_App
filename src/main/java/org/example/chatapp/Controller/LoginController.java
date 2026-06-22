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

import java.io.IOException;
import java.util.ArrayList;

public class LoginController {

    public PasswordField logpswd;
    public TextField logUname;

    public void loginOnAction(ActionEvent actionEvent) {
        String password = logpswd.getText();
        String userName = logUname.getText();
        UserData userData = UserData.getInstance();
        boolean isvalidate = userData.UserLoginValidate(userName,password);
        if(isvalidate){
            //goto chat interface
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


}
