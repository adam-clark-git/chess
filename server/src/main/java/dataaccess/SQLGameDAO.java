package dataaccess;

import com.google.gson.Gson;
import model.GameData;
import server.exceptions.DataAccessException;

import java.sql.SQLException;
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
                INSERT INTO user (gameID, whiteUsername, blackUsername, gameName, game)
                VALUES (?, ?, ?)
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
            throw new DataAccessException("failed to create auth", ex);
        }
    }
    @Override
    public GameData getGame(int gameID) throws DataAccessException {

    }
    @Override
    public List<GameData> listGames() throws DataAccessException {

    }
    @Override
    public void updateGame(GameData game) throws DataAccessException {

    }
}
