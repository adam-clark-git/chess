package database;
import chess.ChessGame;
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
                SELECT Gamename, email, password FROM Game WHERE Gamename = "bob"
                """;
        try (var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            var result = stmt.executeQuery();
            assertTrue(result.next());
            assertEquals("bob", result.getString("Gamename"));
            assertNotNull(result.getString("password"));
            assertEquals("happy@mail.com", result.getString("email"));
        } catch (SQLException ex) {
            throw new DataAccessException("failed to add", ex);
        }
    }
    @Test
    void addFail() throws DataAccessException {
        GameDAO gameDAO = new SQLGameDAO();
        gameDAO.createGame(new GameData(5,"white","black", "kingsgambit", new ChessGame()));
        assertThrows(DataAccessException.class,
                () -> gameDAO.createGame(new GameData("bob","password2","sad@mail.com")));

    }
    @Test
    void clearSuccess() throws DataAccessException {
        GameDAO gameDAO = new SQLGameDAO();
        gameDAO.createGame(new GameData(5,"white","black", "kingsgambit", new ChessGame()));
        gameDAO.createGame(new GameData(5,"white","black", "kingsgambit", new ChessGame()));
        gameDAO.clear();
        var statement = """
                SELECT Gamename, email, password FROM Game WHERE Gamename = "bob"
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
        gameDAO.createGame(new GameData("bob","password","happy@mail.com"));
        GameData data = gameDAO.getGameData("bob");
        assertNotNull(data);
        assertEquals("bob", data.Gamename());
        assertEquals("happy@mail.com", data.email());
        assertNotEquals("password", data.password());
    }
    @Test
    void getFail() throws DataAccessException {
        GameDAO gameDAO = new SQLGameDAO();
        gameDAO.createGame(new GameData("bob","password","happy@mail.com"));
        assertNull(GameDAO.getGameData("bobothy"));

    }
    // DO THE PASSWORD ONES LATER
}
