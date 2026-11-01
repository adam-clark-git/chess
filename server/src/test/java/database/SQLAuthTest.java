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
                TRUNCATE TABLE user;
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
                SELECT authname, email, password FROM auth WHERE authToken = "token"
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
                () -> authDAO.createAuth(new AuthData("bob","password2","sad@mail.com")));

    }
    @Test
    void clearSuccess() throws DataAccessException {
        AuthDAO authDAO = new SQLAuthDAO();
        authDAO.createAuth(new AuthData("bob","password","happy@mail.com"));
        authDAO.createAuth(new AuthData("bob2","password2","happy2@mail.com"));
        authDAO.clear();
        var statement = """
                SELECT authname, email, password FROM auth WHERE authname = "bob"
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
        authDAO.createAuth(new AuthData("bob","password","happy@mail.com"));
        AuthData data = authDAO.getAuthData("bob");
        assertNotNull(data);
        assertEquals("bob", data.authname());
        assertEquals("happy@mail.com", data.email());
        assertNotEquals("password", data.password());
    }
    @Test
    void getFail() throws DataAccessException {
        AuthDAO authDAO = new SQLAuthDAO();
        authDAO.createAuth(new AuthData("bob","password","happy@mail.com"));
        assertNull(authDAO.getAuthData("bobothy"));

    }
    // DO THE PASSWORD ONES LATER
}