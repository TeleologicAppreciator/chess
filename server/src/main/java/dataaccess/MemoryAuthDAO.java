package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<String, AuthData> myAuthData = new HashMap<>();

    public void createAuth(AuthData theUserData) {
        myAuthData.put(theUserData.authToken(), theUserData);
    }

    public AuthData getAuth(String authToken) {
        return myAuthData.get(authToken);
    }

    public void deleteAuth(AuthData theUserData) throws DataAccessException {
        myAuthData.remove(theUserData.authToken());
    }

    public void deleteAll() {
        myAuthData.clear();
    }
}
