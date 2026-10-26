package passoff.server;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;
import server.*;
import service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import javax.xml.crypto.Data;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
    private GameService gameService;
    private UserService userService;
    private ClearService clearService;
    private AuthToken authToken;
    @BeforeEach
    void setUp() {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(userDAO, authDAO, gameDAO);
        clearService = new ClearService(userDAO, authDAO, gameDAO);
        try{
            var registerResult = userService.registerUser(new RegisterRequest("ethan", "pw", "a@b.com"));
            authToken = new AuthToken(registerResult.authToken());
            gameService.createGame(new GameRequest(authToken, "Game"));
        } catch (DataAccessException e) {
            // This shouldn't happen
            System.out.println("ERROR ERROR SOMETHING HAS GONE TERRIBLY WRONG");
        }
    }
    @Test
    void clearSuccess() throws DataAccessException {
        clearService.clearAll();
    }



}