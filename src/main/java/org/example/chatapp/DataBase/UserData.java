package org.example.chatapp.DataBase;
import org.example.chatapp.Model.User;
import java.util.ArrayList;

public class UserData {
    private static UserData instance;
    private ArrayList<User> users;

    private UserData(){
        users = new ArrayList<>();
    }
     public static synchronized UserData getInstance(){
        if (instance == null){
            instance = new UserData();
        }
        return instance;
     }
    public boolean UserLoginValidate(String userName, String password){
        for(User user : users){
            if((user.getUserName().equals(userName))&& (user.getPassword().equals(password))){
                return true;
            }
        }
        return false;
    }
    public void saveUser(User user){
        users.add(user);
    }

}
