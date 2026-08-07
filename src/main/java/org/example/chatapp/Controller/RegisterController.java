package org.example.chatapp.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.chatapp.DataBase.DBConnection;
import org.example.chatapp.DataBase.UserData;
import org.example.chatapp.Model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterController {

    public PasswordField regRepswd;
    public PasswordField regPswd;
    public TextField regUserName;
    public TextField regName;
    public Label regError;

    public void registerOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
//        UserData userData = UserData.getInstance();
        String name = regName.getText();
        String userName = regUserName.getText();
        String password = regPswd.getText();
        String re_password = regRepswd.getText();

        String hashed_password = BCrypt.hashpw(password, BCrypt.gensalt());
        if(!password.isEmpty() && !userName.isEmpty() && !name.isEmpty() && !re_password.isEmpty()){
            if(password.equals(re_password)){
//                User user = new User(name,userName,password);
//                userData.saveUser(user);
                String sql = """
                        INSERT INTO users(name, userName, password_hash ) 
                        VALUES (?, ?, ?);
                        """;
                PreparedStatement ptmt = DBConnection.getConnection().prepareStatement(sql);
                ptmt.setString(2, userName);
                ptmt.setString(3, hashed_password);
                ptmt.setString(1, name);
                ptmt.executeUpdate();
                System.out.println("User created");
                regError.setStyle("-fx-border-color: green;");
                regError.setText("User created");

            }
            else{
                regError.setStyle("-fx-border-color: red;");
                regError.setText("Passwords are Not Match");
            }
        }
        else{
            regError.setStyle("-fx-border-color: red;");
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

    public void backtoLgin(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/chatapp/View/LoginForm.fxml"));
        Scene loginScene = null;
        try {
            loginScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.setScene(loginScene);
        currentStage.setTitle("Log in");
    }
}
