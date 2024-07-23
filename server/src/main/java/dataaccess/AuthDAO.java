package dataaccess;

import model.AuthData;

public interface AuthDAO extends DataAccess {
    public void createAuth(AuthData theUserData);

    public AuthData getAuth(String theAuthToken);

    public void deleteAuth(AuthData theUserData);
}
