package dataaccess;

import model.UserData;
import server.exceptions.DataAccessException;

import java.sql.SQLException;


public class SQLUserDAO implements UserDAO {
    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = """
                DELETE FROM user;
                """;
            try (var stmt = conn.prepareStatement(statement)) {
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to clear", ex);
        }
    }
    @Override
    public void createUser(UserData user) throws DataAccessException {
        // NEED TO STILL ENCRYPT PASSWORDS
        try (var conn = DatabaseManager.getConnection()) {
            var statement = """
                INSERT INTO user (username, password, email)
                VALUES (?, ?, ?)
                """;
            try (var stmt = conn.prepareStatement(statement)) {
                stmt.setString(1, user.username());
                stmt.setString(2, user.password());
                stmt.setString(3, user.email());
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create user", ex);
        }
    }
    @Override
    public UserData getUserData(String username) throws DataAccessException {

    }
}
