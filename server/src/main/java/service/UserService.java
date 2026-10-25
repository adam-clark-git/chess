package service;

import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import model.UserData;
import server.AlreadyTakenException;
import server.BadRequestException;
import server.DataAccessException;
import model.UserData;
import model.AuthData;
import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    public RegisterResult registerUser(RegisterRequest request) throws DataAccessException {
        String username = request.username();
        String password = request.password();
        String email = request.email();
        if (username == null || password == null || email == null) {
            throw new BadRequestException("Error: Bad Request");
        }
        if (userDAO.getUserData(username) != null) {
            throw new AlreadyTakenException("Error: Username taken");
        }
        UserData user = new UserData(username,password, email);
        userDAO.createUser(user);
        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, username);
        return new RegisterResult(username, authToken);
    }
}
