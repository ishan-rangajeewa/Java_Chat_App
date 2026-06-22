package org.example.chatapp.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    public void registerOnAction(ActionEvent actionEvent) {
        setUi(actionEvent,"RegisterForm","Register Form");
    }

    public void logiFormOnAction(ActionEvent actionEvent) {
        setUi(actionEvent,"LoginForm","Login Form");
    }

    private void setUi(ActionEvent event, String path, String title){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/chatapp/View/"+path+".fxml"));
        Scene loginScene = null;
        try {
            loginScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(loginScene);
        currentStage.setTitle(title);
        currentStage.show();
    }
}
