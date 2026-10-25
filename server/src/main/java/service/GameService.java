package service;

import chess.ChessGame;
import dataAccess.*;
import server.*;
import model.*;
import service.AuthToken;

import java.util.List;
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

    public ListGamesResult listGames(AuthToken authToken) throws DataAccessException {
        String token = authToken.token();
        AuthData auth = authDAO.checkAuth(token);
        if (auth == null) {
            throw new AuthException("Error: Unauthorized");
        }

        return new ListGamesResult(gameDAO.listGames());
    }
    public int createGame(AuthToken authToken) throws DataAccessException {
        String token = authToken.token();
        AuthData auth = authDAO.checkAuth(token);
        if (auth == null) {
            throw new AuthException("Error: Unauthorized");
        }
        ChessGame game = new ChessGame();
        String username = auth.username();
        int gameID = UUID.randomUUID().hashCode() & Integer.MAX_VALUE;
        if (gameID % 2 == 1) {
            String black = username;
            String white = "";
        }
        else {
            String black = "";
            String white = username;
        }
        return gameID;
    }


}