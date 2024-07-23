package dataaccess;

import model.AuthData;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<String, AuthData> myAuthData = new HashMap<>();

    public void createAuth(AuthData theUserAuthData) {
        myAuthData.put(theUserAuthData.authToken(), theUserAuthData);
    }

    public AuthData getAuth(String authToken) {
        return myAuthData.get(authToken);
    }

    public void deleteAuth(AuthData theUserAuthData) {
        myAuthData.remove(theUserAuthData.authToken());
    }

    public void deleteAll() {
        myAuthData.clear();
    }
}
