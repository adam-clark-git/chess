package service;

import chess.ChessGame;
import dataAccess.*;
import model.*;
import server.exceptions.*;
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
        if (request.gameName() == null) {
            throw new BadRequestException("Error: Missing Game name");
        }
        ChessGame game = new ChessGame();
        String username = auth.username();
        int gameID = UUID.randomUUID().hashCode() & Integer.MAX_VALUE;
        String black = null;
        String white = null;
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
            throw new GameNotFoundException("Error: Game Not Found");
        }
        GameData newGameData;
        if (request.playerColor() == ChessGame.TeamColor.BLACK) {
            if (gameData.blackUsername() != null) {
                throw new ColorTakenException("Error: Color is taken");
            }
            newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());

        }
        else if (request.playerColor() == ChessGame.TeamColor.WHITE) {
            if (gameData.whiteUsername() != null) {
                throw new ColorTakenException("Error: Color is taken");
            }
            newGameData = new GameData(gameData.gameID(), username, gameData.blackUsername(), gameData.gameName(), gameData.game());

        }
        else {
            throw new BadRequestException("Error: Color not permitted");
        }
        gameDAO.updateGame(newGameData);

    }


}