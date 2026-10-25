package service;

import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import model.UserData;
import server.*;
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
            throw new AlreadyTakenException("Error: No user found");
        }
        if (password != user.password()) {
            throw new PasswordIncorrectException("Error: Password invalid");
        }
        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, username);
        authDAO.createAuth(auth);
        return new RegisterResult(username, authToken);
    }
    public void logoutUser(AuthToken authToken) throws DataAccessException {
        String token = authToken.token();
        AuthData auth = authDAO.checkAuth(token);
        if (auth == null) {
            throw new AuthException("Error: Unauthorized");
        }
        authDAO.deleteAuth(token);
    }
}
