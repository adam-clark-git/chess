package client;

import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {
    private static ServerFacade facade;
    private static Server server;
    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }
    @BeforeEach
    public void setup() throws ResponseException {
        facade.clear();
    }
    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    void registerPositive() throws ResponseException {
        var result = facade.register("player1", "password", "p1@email.com");

        Assertions.assertEquals("player1", result.username());
        Assertions.assertNotNull(result.authToken());
        Assertions.assertTrue(result.authToken().length() > 10);
    }
    @Test
    void registerNegative() throws ResponseException {
        facade.register("player1", "password", "p1@email.com");
        Assertions.assertThrows(ResponseException.class, () -> facade.register("player1", null, "e1@email.com"));
    }
    @Test
    void loginPositive() throws ResponseException {
        facade.register("player1", "password", "p1@email.com");
        var result = facade.login("player1", "password");

        Assertions.assertEquals("player1", result.username());
        Assertions.assertNotNull(result.authToken());
        Assertions.assertTrue(result.authToken().length() > 10);
    }
    @Test
    void loginNegative() throws ResponseException {
        facade.register("player1", "password", "p1@email.com");
        Assertions.assertThrows(ResponseException.class, () -> facade.login("player1", null));
    }
    @Test
    void logoutPositive() throws ResponseException {
        var item = facade.register("player1", "password", "p1@email.com");
        String authToken = item.authToken();
        Assertions.assertDoesNotThrow(()->facade.logout(authToken));
    }
    @Test
    void logoutNegative() throws ResponseException {
        Assertions.assertThrows(ResponseException.class, () -> facade.logout(null));
    }
    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
