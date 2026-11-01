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

public class SQLUserTest {
    @BeforeAll
    static void createDB() throws DataAccessException {
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();
    }

    @BeforeEach
    void setUp() throws DataAccessException{
        var statement = """
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
}
