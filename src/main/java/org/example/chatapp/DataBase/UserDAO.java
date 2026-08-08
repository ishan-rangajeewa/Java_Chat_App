package org.example.chatapp.DataBase;

import org.example.chatapp.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public static void save(User user){
        String sql = """
                        INSERT INTO users(name, userName, password_hash ) 
                        VALUES (?, ?, ?);
                        """;

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstm = conn.prepareStatement(sql)){

            pstm.setString(1, user.getName());
            pstm.setString(2, user.getUserName());
            pstm.setString(3,user.getPassword());

            pstm.executeUpdate();

        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public static String findByUsername(String username){
        String sql = "SELECT * FROM users WHERE username = ?";
         String result = null;

        try(Connection conn = DBConnection.getConnection();
        PreparedStatement pstm = conn.prepareStatement(sql)){
            pstm.setString(1, username);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()){
                result = rs.getString("password_hash");
            }

        }catch(SQLException | ClassNotFoundException  e){
            e.printStackTrace();
        }
        return result;
    }
}


