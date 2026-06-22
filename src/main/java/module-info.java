module org.example.chatapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.chatapp to javafx.fxml;
    exports org.example.chatapp;
    exports org.example.chatapp.Controller;
    opens org.example.chatapp.Controller to javafx.fxml;
}