package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.UserData;
import model.AuthData;
import server.exceptions.*;
import service.endpoints.LoginRequest;
import service.endpoints.RegisterRequest;
import service.endpoints.RegisterResult;

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
        authDAO.createAuth(auth);
        return new RegisterResult(username, authToken);
    }
    public RegisterResult loginUser(LoginRequest request) throws DataAccessException {
        String username = request.username();
        String password = request.password();
        if (username == null || password == null) {
            throw new BadRequestException("Error: Bad Request");
        }
        UserData user = userDAO.getUserData(username);
        if (user == null) {
            throw new AuthException("Error: No user found");
        }
        if (!password.equals(user.password())) {
            throw new PasswordIncorrectException("Error: Password invalid");
        }
        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, username);
        authDAO.createAuth(auth);
        return new RegisterResult(username, authToken);
    }
    public void logoutUser(String authToken) throws DataAccessException {
        AuthData auth = authDAO.checkAuth(authToken);
        if (auth == null) {
            throw new AuthException("Error: Unauthorized");
        }
        authDAO.deleteAuth(authToken);
    }
}
