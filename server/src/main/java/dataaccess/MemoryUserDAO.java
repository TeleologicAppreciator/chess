package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private final HashMap<String, UserData> myUserData = new HashMap<>();

    public void createUser(UserData theUserData) {
        myUserData.put(theUserData.username(), theUserData);
    }

    public UserData getUser(String theUsername) {
        return myUserData.get(theUsername);
    }

    public void deleteAll(){
        myUserData.clear();
    }
}
