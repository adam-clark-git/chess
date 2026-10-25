package dataAccess;

import model.UserData;
import server.AlreadyTakenException;
import server.DataAccessException;

public interface UserDAO {
    void clear() throws DataAccessException;
    void createUser(UserData user) throws DataAccessException;
    UserData getUserData(String username) throws DataAccessException;
}
