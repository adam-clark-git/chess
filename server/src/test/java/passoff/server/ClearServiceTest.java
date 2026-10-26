package passoff.server;

import dataaccess.*;
import server.exceptions.DataAccessException;
import service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import service.endpoints.GameRequest;
import service.endpoints.RegisterRequest;

public class ClearServiceTest {
    private GameService gameService;
    private UserService userService;
    private ClearService clearService;
    private String authToken;
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
            authToken = registerResult.authToken();
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