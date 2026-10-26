package service;

import chess.ChessGame;
import dataAccess.*;
import model.*;
import server.exceptions.AuthException;
import server.exceptions.ColorTakenException;
import server.exceptions.DataAccessException;
import server.exceptions.GameNotFoundException;
import service.endpoints.CreateGameResult;
import service.endpoints.GameRequest;
import service.endpoints.JoinGameRequest;
import service.endpoints.ListGamesResult;

import java.util.UUID;

public class GameService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ListGamesResult listGames(String authToken) throws DataAccessException {
        AuthData auth = authDAO.checkAuth(authToken);
        if (auth == null) {
            throw new AuthException("Error: Unauthorized");
        }

        return new ListGamesResult(gameDAO.listGames());
    }
    public CreateGameResult createGame(GameRequest request) throws DataAccessException {
        String token = request.authToken();
        AuthData auth = authDAO.checkAuth(token);
        if (auth == null) {
            throw new AuthException("Error: Unauthorized");
        }
        ChessGame game = new ChessGame();
        String username = auth.username();
        int gameID = UUID.randomUUID().hashCode() & Integer.MAX_VALUE;
        String black = "";
        String white = "";
        String name = request.gameName();
        gameDAO.createGame(new GameData(gameID,white, black, name, game));
        return new CreateGameResult(gameID);
    }
    public void joinGame(JoinGameRequest request) throws DataAccessException {
        String token = request.authToken();
        AuthData auth = authDAO.checkAuth(token);
        if (auth == null) {
            throw new AuthException("Error: Unauthorized");
        }
        String username = auth.username();
        GameData gameData =  gameDAO.getGame(request.gameID());
        if (gameData == null) {
            throw new GameNotFoundException("Game Not Found");
        }
        GameData newGameData;
        if (request.gameColor() == ChessGame.TeamColor.BLACK) {
            if (gameData.blackUsername() != "") {
                throw new ColorTakenException("Error: Color is taken");
            }
            newGameData = new GameData(gameData.gameID(), gameData.blackUsername(), username, gameData.gameName(), gameData.game());
        }
        else {
            if (gameData.whiteUsername() != "") {
                throw new ColorTakenException("Error: Color is taken");
            }
            newGameData = new GameData(gameData.gameID(), username, gameData.whiteUsername(), gameData.gameName(), gameData.game());
        }
        gameDAO.updateGame(newGameData);

    }


}