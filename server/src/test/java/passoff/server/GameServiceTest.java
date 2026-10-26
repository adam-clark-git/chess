package passoff.server;

import chess.ChessGame;
import dataaccess.*;
import model.GameData;
import server.exceptions.AuthException;
import server.exceptions.ColorTakenException;
import server.exceptions.DataAccessException;
import server.exceptions.GameNotFoundException;
import service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import service.endpoints.CreateGameResult;
import service.endpoints.GameRequest;
import service.endpoints.JoinGameRequest;
import service.endpoints.RegisterRequest;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    private GameService service;
    private UserService userService;
    private String authToken;
    @BeforeEach
    void setUp() {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
        service = new GameService(userDAO, authDAO, gameDAO);
        try{
            var registerResult = userService.registerUser(new RegisterRequest("ethan", "pw", "a@b.com"));
            authToken = registerResult.authToken();
        } catch (DataAccessException e) {
            // This shouldn't happen
            System.out.println("ERROR ERROR SOMETHING HAS GONE TERRIBLY WRONG");
        }
    }
    @Test
    void createGameSuccess() throws DataAccessException {
        var result = service.createGame(new GameRequest(authToken, "Game"));
        assertNotNull(result);
    }
    @Test
    void noAuthCreateGame() throws DataAccessException {
        assertThrows(AuthException.class,
                () -> service.createGame((new GameRequest("5", "Game"))));
    }
    @Test
    void listGamesSuccess() throws DataAccessException {
        service.createGame(new GameRequest(authToken, "Game"));
        service.createGame(new GameRequest(authToken, "Game2"));
        var result = service.listGames(authToken);
        List<GameData> games = result.games();
        games.sort(Comparator.comparingInt(GameData::gameID));
        GameData game1 = games.get(0);
        GameData game2 = games.get(1);
        assertEquals("Game", game1.gameName());
        assertEquals("Game2", game2.gameName());
        assertThrows(IndexOutOfBoundsException.class,
                () -> result.games().get(2));
        assertNotNull(result);
    }
    @Test
    void listGamesWithNoGames() throws DataAccessException {
        var result = service.listGames(authToken);
        assertThrows(IndexOutOfBoundsException.class,
                () -> result.games().get(0));
        assertNotNull(result);
    }
    @Test
    void joinGameSuccess() throws DataAccessException {
        CreateGameResult gameID = service.createGame(new GameRequest(authToken, "Game"));
        service.joinGame(new JoinGameRequest(authToken, ChessGame.TeamColor.WHITE, gameID.gameID()));
    }
    @Test
    void joinNoExistingGame() throws DataAccessException {
        assertThrows(GameNotFoundException.class,
                () -> service.joinGame(new JoinGameRequest(authToken, ChessGame.TeamColor.WHITE, 5)));
    }
    @Test
    void joinGameWithColorTaken() throws DataAccessException {
        CreateGameResult gameID = service.createGame(new GameRequest(authToken, "Game"));
        service.joinGame(new JoinGameRequest(authToken, ChessGame.TeamColor.WHITE, gameID.gameID()));
        assertThrows(ColorTakenException.class,
                () -> service.joinGame(new JoinGameRequest(authToken, ChessGame.TeamColor.WHITE, gameID.gameID())));
    }


}
