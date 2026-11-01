package database;
import model.AuthData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import dataaccess.*;
import server.exceptions.DataAccessException;
import service.ClearService;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SQLAuthTest {
    @BeforeAll
    static void createDB() throws DataAccessException {
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();
    }

    @BeforeEach
    void setUp() throws DataAccessException{
        var statement = """
                TRUNCATE TABLE auth;
                """;
        try (var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to setup clear", ex);
        }
    }
    @Test
    void addSuccess() throws DataAccessException {
        AuthDAO authDAO = new SQLAuthDAO();
        authDAO.createAuth(new AuthData("token","bob"));
        var statement = """
                SELECT authToken, username FROM auth WHERE authToken = "token"
                """;
        try (var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            var result = stmt.executeQuery();
            assertTrue(result.next());
            assertEquals("token", result.getString("authToken"));
            assertEquals("bob", result.getString("username"));
        } catch (SQLException ex) {
            throw new DataAccessException("failed to add", ex);
        }
    }
    @Test
    void addFail() throws DataAccessException {
        AuthDAO authDAO = new SQLAuthDAO();
        authDAO.createAuth(new AuthData("bob","happy@mail.com"));
        assertThrows(DataAccessException.class,
                () -> authDAO.createAuth(new AuthData("bob","sad@mail.com")));

    }
    @Test
    void clearSuccess() throws DataAccessException {
        AuthDAO authDAO = new SQLAuthDAO();
        authDAO.createAuth(new AuthData("bob","happy@mail.com"));
        authDAO.createAuth(new AuthData("bob2","happy2@mail.com"));
        authDAO.clear();
        var statement = """
                SELECT authToken, username FROM auth WHERE authToken = "bob"
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
        AuthDAO authDAO = new SQLAuthDAO();
        authDAO.createAuth(new AuthData("bob","password"));
        AuthData data = authDAO.checkAuth("bob");
        assertNotNull(data);
        assertEquals("bob", data.authToken());
        assertEquals("password", data.username());

    }
    @Test
    void getFail() throws DataAccessException {
        AuthDAO authDAO = new SQLAuthDAO();
        authDAO.createAuth(new AuthData("bob","password"));
        assertNull(authDAO.checkAuth("bobothy"));

    }
    // DO THE DELETE ONES SOON
    @Test
    void deleteSuccess() throws DataAccessException {
        AuthDAO authDAO = new SQLAuthDAO();
        authDAO.createAuth(new AuthData("bob","password"));
        authDAO.deleteAuth("bob");
        AuthData data = authDAO.checkAuth("bob");
        assertNull(data);

    }
    @Test
    void deleteFail() throws DataAccessException {
        AuthDAO authDAO = new SQLAuthDAO();
        authDAO.createAuth(new AuthData("bob","password"));
        authDAO.deleteAuth("bobby");
        AuthData data = authDAO.checkAuth("bob");
        assertNotNull(data);
        assertEquals("bob", data.authToken());
        assertEquals("password", data.username());

    }
}