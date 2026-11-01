package database;
import model.UserData;
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
        UserDAO userDAO = new SQLUserDAO();
        userDAO.createUser(new UserData("bob","password","happy@mail.com"));
        var statement = """
                SELECT username, email, password FROM user WHERE username = "bob"
                """;
        try (var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            var result = stmt.executeQuery();
            assertTrue(result.next());
            assertEquals("bob", result.getString("username"));
            assertNotNull(result.getString("password"));
            assertEquals("happy@mail.com", result.getString("email"));
        } catch (SQLException ex) {
            throw new DataAccessException("failed to add", ex);
        }
    }
    @Test
    void addFail() throws DataAccessException {
        UserDAO userDAO = new SQLUserDAO();
        userDAO.createUser(new UserData("bob","password","happy@mail.com"));
        assertThrows(DataAccessException.class,
                () -> userDAO.createUser(new UserData("bob","password2","sad@mail.com")));

    }
    @Test
    void clearSuccess() throws DataAccessException {
        UserDAO userDAO = new SQLUserDAO();
        userDAO.createUser(new UserData("bob","password","happy@mail.com"));
        userDAO.createUser(new UserData("bob2","password2","happy2@mail.com"));
        userDAO.clear();
        var statement = """
                SELECT username, email, password FROM user WHERE username = "bob"
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
        UserDAO userDAO = new SQLUserDAO();
        userDAO.createUser(new UserData("bob","password","happy@mail.com"));
        UserData data = userDAO.getUserData("bob");
        assertNotNull(data);
        assertEquals("bob", data.username());
        assertEquals("happy@mail.com", data.email());
        assertNotEquals("password", data.password());
    }
    @Test
    void getFail() throws DataAccessException {
        UserDAO userDAO = new SQLUserDAO();
        userDAO.createUser(new UserData("bob","password","happy@mail.com"));
        assertNull(userDAO.getUserData("bobothy"));

    }
    // DO THE PASSWORD ONES LATER
}