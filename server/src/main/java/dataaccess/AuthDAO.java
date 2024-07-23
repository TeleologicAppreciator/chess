package dataaccess;

import model.AuthData;

public interface AuthDAO extends DataAccess {
    public void createAuth(AuthData theUserData);

    public AuthData getAuth(String theAuthToken) throws DataAccessException;

    public void deleteAuth(AuthData theUserData);
}
