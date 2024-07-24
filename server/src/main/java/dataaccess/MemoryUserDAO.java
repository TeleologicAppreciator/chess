package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private final HashMap<String, UserData> myUserData = new HashMap<>();

    public void createUser(UserData theUserData) throws DataAccessException {
        UserData isTheUserAlreadyCreated = null;

        try {
            isTheUserAlreadyCreated = getUser(theUserData.username(), theUserData.password());
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

    public UserData getUser(String theUsername, String thePassword) throws DataAccessException {
        if(theUsername == null || thePassword == null) {
            throw new DataAccessException("Username and password are required");
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

    public void deleteAll() {
        myUserData.clear();
    }

    public int size() {
        return myUserData.size();
    }
}
