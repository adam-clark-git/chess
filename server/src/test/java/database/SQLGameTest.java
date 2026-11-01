package database;
import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.GameData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import dataaccess.*;
import server.exceptions.DataAccessException;
import service.ClearService;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGameTest {
    @BeforeAll
    static void createDB() throws DataAccessException {
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();
    }

    @BeforeEach
    void setUp() throws DataAccessException{
        var statement = """
                TRUNCATE TABLE game;
                """;
        try (var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to setup clear", ex);
        }
    }
    @Test
    void addSuccess() throws DataAccessException {
        GameDAO gameDAO = new SQLGameDAO();
        gameDAO.createGame(new GameData(5,"white","black", "kingsgambit", new ChessGame()));
        var statement = """
                SELECT GameID, whiteUsername, blackUsername, gameName, game FROM game WHERE GameID = 5
                """;
        try (var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            var result = stmt.executeQuery();
            assertTrue(result.next());
            assertEquals(5, result.getInt("gameID"));
            assertEquals("white", result.getString("whiteUsername"));
            assertEquals("black", result.getString("blackUsername"));
            assertEquals("kingsgambit", result.getString("gameName"));
            assertEquals(new ChessGame(), new Gson().fromJson(result.getString("game"), ChessGame.class));
        } catch (SQLException ex) {
            throw new DataAccessException("failed to add", ex);
        }
    }

    @Test
    void addFail() throws DataAccessException {
        GameDAO gameDAO = new SQLGameDAO();
        gameDAO.createGame(new GameData(5,"white","black", "kingsgambit", new ChessGame()));
        assertThrows(DataAccessException.class,
                () -> gameDAO.createGame(new GameData(5,"white","black", "kingsgambit", new ChessGame())));

    }
    @Test
    void clearSuccess() throws DataAccessException {
        GameDAO gameDAO = new SQLGameDAO();
        gameDAO.createGame(new GameData(5,"white","black", "kingsgambit", new ChessGame()));
        gameDAO.createGame(new GameData(6,"white","black", "kingsgambit", new ChessGame()));
        gameDAO.clear();
        var statement = """
                SELECT gameName FROM Game WHERE gameID = 5
                """;
        try (var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            var result = stmt.executeQuery();
            assertFalse(result.next());
        } catch (SQLException ex) {
            throw new DataAccessException("failed to add", ex);
        }
    }
    @Test
    void getSuccess() throws DataAccessException {
        GameDAO gameDAO = new SQLGameDAO();
        gameDAO.createGame(new GameData(5,"white","black", "kingsgambit", new ChessGame()));
        GameData data = gameDAO.getGame(5);
        assertNotNull(data);
        assertEquals("white", data.whiteUsername());
        assertEquals(new ChessGame(), data.game());
    }
    @Test
    void getFail() throws DataAccessException {
        GameDAO gameDAO = new SQLGameDAO();
        gameDAO.createGame(new GameData(5,"white","black", "kingsgambit", new ChessGame()));
        assertNull(gameDAO.getGame(7));
    }
    // DO THE PASSWORD ONES LATER
}
