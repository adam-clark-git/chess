package dataaccess;

import model.UserData;
import server.exceptions.DataAccessException;
import org.mindrot.jbcrypt.BCrypt;

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
            throw new DataAccessException("ERROR: failed to clear", ex);
        }
    }
    @Override
    public void createUser(UserData user) throws DataAccessException {
        // NEED TO STILL ENCRYPT PASSWORDS
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        try (var conn = DatabaseManager.getConnection()) {
            var statement = """
                INSERT INTO user (username, password, email)
                VALUES (?, ?, ?)
                """;
            try (var stmt = conn.prepareStatement(statement)) {
                stmt.setString(1, user.username());
                stmt.setString(2, hashedPassword);
                stmt.setString(3, user.email());
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("ERROR: failed to create user", ex);
        }
    }
    @Override
    public UserData getUserData(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = """
                SELECT username, email, password FROM user WHERE username = ?
                """;
            try (var stmt = conn.prepareStatement(statement)) {
                stmt.setString(1, username);
                try (var result = stmt.executeQuery()) {
                    if (result.next()) {
                        return new UserData(result.getString("username"),
                                result.getString("password"), result.getString("email"));
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("ERROR: failed to get user data", ex);
        }
        return null;
    }
    @Override
    public boolean checkPassword(UserData user) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = """
                SELECT username, password FROM user WHERE username = ?
                """;
            try (var stmt = conn.prepareStatement(statement)) {
                stmt.setString(1, user.username());
                try (var result = stmt.executeQuery()) {
                    if (result.next()) {
                        return BCrypt.checkpw(user.password(), result.getString("password"));
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("ERROR: failed to get user data", ex);
        }
        return false;
    }
}
