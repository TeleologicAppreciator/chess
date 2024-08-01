package dataaccess.memory;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private final HashMap<String, UserData> myUserData = new HashMap<>();

    public void createUser(UserData theUserData) throws DataAccessException {
        UserData isTheUserAlreadyCreated = null;

        try {
            isTheUserAlreadyCreated = getUser(theUserData.username());
        } catch (DataAccessException e) {
            if(e.getMessage().equals("Username and password are required")) {
                throw e;
            } else if (e.getMessage().equals("User not found")) {
                myUserData.put(theUserData.username(), theUserData);
            }
        }

        if(isTheUserAlreadyCreated != null) {
            throw new DataAccessException("User already exists");
        }
    }

    public UserData getUser(String theUsername) throws DataAccessException {
        if(theUsername == null) {
            throw new DataAccessException("Username is required");
        }

        UserData userToReturn = myUserData.get(theUsername);

        if(userToReturn == null) {
            throw new DataAccessException("User not found");
        }

        return userToReturn;
    }

    public void deleteAll() {
        myUserData.clear();
    }

    public int size() {
        return myUserData.size();
    }
}
