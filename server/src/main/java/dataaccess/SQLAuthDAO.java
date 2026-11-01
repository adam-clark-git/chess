package dataaccess;

import model.AuthData;
import server.exceptions.DataAccessException;

import java.sql.SQLException;


public class SQLAuthDAO implements AuthDAO {
    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = """
                DELETE FROM auth;
                """;
            try (var stmt = conn.prepareStatement(statement)) {
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to clear", ex);
        }
    }
    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = """
                INSERT INTO user (authToken, username)
                VALUES (?, ?, ?)
                """;
            try (var stmt = conn.prepareStatement(statement)) {
                stmt.setString(1, auth.authToken());
                stmt.setString(2, auth.username());
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to clear", ex);
        }
    }
    @Override
    public AuthData checkAuth(String authToken) throws DataAccessException {

    }
    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }
}
