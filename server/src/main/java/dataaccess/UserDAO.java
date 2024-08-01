package dataaccess;

import model.UserData;

import java.sql.SQLException;

public interface UserDAO extends DataAccess {
    public void createUser(UserData theUser) throws DataAccessException;

    public UserData getUser(String theUsername) throws DataAccessException;

    public int size() throws SQLException, DataAccessException;
}
