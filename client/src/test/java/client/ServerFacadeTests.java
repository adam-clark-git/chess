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
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
