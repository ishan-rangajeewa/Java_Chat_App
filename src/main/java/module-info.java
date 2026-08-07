module org.example.chatapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires jbcrypt;
    requires java.desktop;


    opens org.example.chatapp to javafx.fxml;
    exports org.example.chatapp;
    exports org.example.chatapp.Controller;
    opens org.example.chatapp.Controller to javafx.fxml;
}