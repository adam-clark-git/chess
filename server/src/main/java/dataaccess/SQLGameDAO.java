package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import server.exceptions.DataAccessException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SQLGameDAO implements GameDAO {

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = """
                DELETE FROM game;
                """;
            try (var stmt = conn.prepareStatement(statement)) {
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to clear", ex);
        }
    }
    @Override
    public void createGame(GameData game) throws DataAccessException {
        String serializedGame = new Gson().toJson(game.game());
        try (var conn = DatabaseManager.getConnection()) {
            var statement = """
                INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, game)
                VALUES (?, ?, ?, ?, ?)
                """;
            try (var stmt = conn.prepareStatement(statement)) {
                stmt.setInt(1, game.gameID());
                stmt.setString(2, game.whiteUsername());
                stmt.setString(3, game.blackUsername());
                stmt.setString(4, game.gameName());
                stmt.setString(5, serializedGame);
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create game", ex);
        }
    }
    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = """
                SELECT  whiteUsername, blackUsername, gameName, game FROM game WHERE gameID = ?
                """;
            try (var stmt = conn.prepareStatement(statement)) {
                stmt.setInt(1, gameID);
                try (var result = stmt.executeQuery()) {
                    if (result.next()) {
                        ChessGame game = new Gson().fromJson(result.getString("game"), ChessGame.class);
                        return new GameData(gameID,
                                result.getString("whiteUsername"), result.getString("blackUsername"),
                                result.getString("gameName"), game);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get game", ex);
        }
        return null;
    }
    @Override
    public List<GameData> listGames() throws DataAccessException {
        var list = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = """
                SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game
                """;
            try (var stmt = conn.prepareStatement(statement)) {
                try (var result = stmt.executeQuery()) {
                    while (result.next()) {
                        ChessGame game = new Gson().fromJson(result.getString("game"), ChessGame.class);
                        list.add(new GameData(result.getInt("gameID"),
                                result.getString("whiteUsername"), result.getString("blackUsername"),
                                result.getString("gameName"), game));
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to list games", ex);
        }
        return list;
    }
    @Override
    public void updateGame(GameData game) throws DataAccessException {
        String serializedGame = new Gson().toJson(game.game());
        try (var conn = DatabaseManager.getConnection()) {
            var statement = """
                UPDATE user SET whiteUsername=?, blackUsername=?, gameName=?, game=? WHERE gameID=?
                """;
            try (var stmt = conn.prepareStatement(statement)) {
                stmt.setInt(5, game.gameID());
                stmt.setString(1, game.whiteUsername());
                stmt.setString(2, game.blackUsername());
                stmt.setString(3, game.gameName());
                stmt.setString(4, serializedGame);
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to update", ex);
        }
    }
}
