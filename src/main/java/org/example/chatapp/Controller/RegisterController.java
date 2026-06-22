package org.example.chatapp.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.chatapp.DataBase.UserData;
import org.example.chatapp.Model.User;

import java.io.IOException;

public class RegisterController {

    public PasswordField regRepswd;
    public PasswordField regPswd;
    public TextField regUserName;
    public TextField regName;
    public Label regError;

    public void registerOnAction(ActionEvent actionEvent) {
        UserData userData = UserData.getInstance();
        String name = regName.getText();
        String userName = regUserName.getText();
        String password = regPswd.getText();
        String re_password = regRepswd.getText();
        if(!password.isEmpty() && !userName.isEmpty() && !name.isEmpty() && !re_password.isEmpty()){
            if(password.equals(re_password)){
                User user = new User(name,userName,password);
                userData.saveUser(user);
                System.out.println("User created");
            }
            else{
                regError.setText("Passwords are Not Match");
            }
        }
        else{
            regError.setText("Fill the all fields");
        }
    }

    public void goHomeAction(ActionEvent actionEvent) {
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
