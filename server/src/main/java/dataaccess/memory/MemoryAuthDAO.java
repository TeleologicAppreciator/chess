package dataaccess.memory;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<String, AuthData> myAuthData = new HashMap<>();

    public void createAuth(AuthData theUserAuthData) {
        myAuthData.put(theUserAuthData.authToken(), theUserAuthData);
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        AuthData returnDataFromToken = myAuthData.get(authToken);

        if (returnDataFromToken == null) {
            throw new DataAccessException("Auth token not found");
        }

        return myAuthData.get(authToken);
    }

    public void deleteAuth(AuthData theUserAuthData) {
        myAuthData.remove(theUserAuthData.authToken());
    }

    public void deleteAll() {
        myAuthData.clear();
    }

    public int size() {
        return myAuthData.size();
    }
}
