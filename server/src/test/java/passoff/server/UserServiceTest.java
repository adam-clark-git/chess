package passoff.server;

import dataAccess.*;
import server.*;
import service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private UserService service;

    @BeforeEach
    void setUp() {
        service = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
    }
    @Test
    void registerSuccess() throws DataAccessException {
        var result = service.registerUser(new RegisterRequest("alice", "pw", "a@b.com"));
        assertEquals("alice", result.username());
        assertNotNull(result.authToken());
    }
    @Test
    void registerDuplicateUsername() throws DataAccessException {
        service.registerUser(new RegisterRequest("cal", "pw", "speed.com"));
        assertThrows(AlreadyTakenException.class,
                () -> service.registerUser(new RegisterRequest("cal", "difpw", "dif.com")));
    }
    @Test
    void registerFail() {
        assertThrows(BadRequestException.class,
                () -> service.registerUser(new RegisterRequest("bobby", null, "a@b.com")));
    }
    @Test
    void loginSuccess() throws DataAccessException{
        service.registerUser(new RegisterRequest("cal", "pw", "speed.com"));
        var result = service.loginUser(new LoginRequest("cal", "pw"));
        assertEquals("cal", result.username());
        assertNotNull(result.authToken());

    }
    @Test
    void loginNoAccount() throws DataAccessException {
        assertThrows(AuthException.class,
                () -> service.loginUser(new LoginRequest("bobby", "pw")));
    }
    @Test
    void loginPasswordWrong() throws DataAccessException {
        service.registerUser(new RegisterRequest("user", "pw1", "email"));
        assertThrows(AuthException.class,
                () -> service.loginUser(new LoginRequest("user", "pw2")));
    }
}
