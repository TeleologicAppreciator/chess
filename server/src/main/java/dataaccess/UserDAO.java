package dataaccess;

import model.AuthData;
import model.UserData;

public interface UserDAO extends DataAccess {
    public void createUser(UserData theUser) throws DataAccessException;

    public UserData getUser(String theUsername, String thePassword) throws DataAccessException;
}
