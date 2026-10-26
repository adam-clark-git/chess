package dataaccess;

import model.UserData;
import server.exceptions.DataAccessException;

public interface UserDAO {
    void clear() throws DataAccessException;
    void createUser(UserData user) throws DataAccessException;
    UserData getUserData(String username) throws DataAccessException;
}
