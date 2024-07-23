package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private final HashMap<String, UserData> myUserData = new HashMap<>();

    public void createUser(UserData theUserData) {
        myUserData.put(theUserData.username(), theUserData);
    }

    public UserData getUser(String theUsername, String thePassword) throws DataAccessException {
        if(theUsername == null) {
            throw new DataAccessException("Username cannot be null");
        }

        UserData userToReturn = myUserData.get(theUsername);

        if(userToReturn == null) {
            throw new DataAccessException("User not found");
        }

        if(!userToReturn.password().equals(thePassword)) {
            throw new DataAccessException("Wrong password");
        }

        return userToReturn;
    }

    public void deleteAll(){
        myUserData.clear();
    }
}
