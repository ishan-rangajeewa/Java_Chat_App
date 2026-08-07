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
import org.example.chatapp.Model.Message;
import org.example.chatapp.Model.User;
import org.example.chatapp.Service.ChatClientService;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class LoginController {

    public PasswordField logpswd;
    public TextField logUname;
    public Label lblError;
    private ChatClientService chatClientService;
    private String storedPassword = "";

    public void initialize() throws SQLException, ClassNotFoundException {
        chatClientService = ChatClientService.getInstance();

    }

    public void loginOnAction(ActionEvent actionEvent) {
        String password = logpswd.getText();
        String userName = logUname.getText();
//        UserData userData = UserData.getInstance();
        String sql = "SELECT * FROM users WHERE username = ?";
        try(Connection conn = DBConnection.getConnection(); ){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                storedPassword = rs.getString("password_hash");
                System.out.println("DB "+storedPassword);
            }

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
          String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("Password Hash "+passwordHash);
          boolean isvalidate = BCrypt.checkpw(password, storedPassword);
        if(isvalidate){
            //goto chat interface
            boolean connected = chatClientService.connect();
            if(connected){
//                Massage massage = new Massage(userName,"","", Massage.Type.LOGIN, LocalDateTime.now());
//                chatClientService.sendMessage(massage);
                navigateToChat(actionEvent,new User(userName,userName,password));
            }
            else{
                lblError.setStyle("-fx-text-fill: red");
                lblError.setText("Server Error");
            }


        }
        else{
            lblError.setStyle("-fx-text-fill: red");
            lblError.setText("Invalid Username or Password");
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

            Message loginMsg = new Message(
                    logUname.getText(),"","",Message.Type.LOGIN,LocalDateTime.now());
            chatClientService.sendMessage(loginMsg);

            Stage stage = (Stage) logUname.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("chat - "+user.getUserName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void gotoRgster(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/chatapp/View/RegisterForm.fxml"));
        Scene loginScene = null;
        try {
            loginScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.setScene(loginScene);
        currentStage.setTitle("New User Register");
        currentStage.show();
    }
}
