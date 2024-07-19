package dataaccess;

import model.AuthData;
import model.UserData;

public interface UserDAO extends DataAccess {
    public void createUser(UserData theUser);

    public UserData getUser(String theUsername) throws DataAccessException;
}
