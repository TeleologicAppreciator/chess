package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private HashMap<String, UserData> myUserData;

    public void createUser(UserData theUserData) {
        myUserData.put(theUserData.myUsername(), theUserData);
    }

    public UserData getUser(String theUsername) {
        return myUserData.get(theUsername);
    }

    public void deleteAll(){
        myUserData.clear();
    }
}
