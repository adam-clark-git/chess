package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import server.exceptions.DataAccessException;

public class ClearService {
    private final UserDAO userDAO;
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;
    public ClearService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }
    public void clearAll() throws DataAccessException {
        clearUsers();
        clearGames();
        clearAuths();
    }
    public void clearUsers() throws DataAccessException {
        userDAO.clear();
    }
    public void clearGames() throws DataAccessException {
        gameDAO.clear();
    }
    public void clearAuths() throws DataAccessException {
        authDAO.clear();
    }

}
