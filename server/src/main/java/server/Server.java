package server;

import com.google.gson.Gson;
import dataAccess.*;
import io.javalin.*;
import server.exceptions.BadRequestException;
import server.handlers.ClearHandler;
import server.handlers.GameHandler;
import server.handlers.UserHandler;
import service.ClearService;
import service.GameService;
import service.UserService;
import io.javalin.http.Context;

import java.util.Map;

public class Server {

    private final Javalin javalin;
    private final Gson gson = new Gson();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        UserDAO userDAO = new MemoryUserDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();

        UserHandler userHandler = new UserHandler(new UserService(userDAO, authDAO));
        GameHandler gameHandler = new GameHandler(new GameService(userDAO, authDAO, gameDAO));
        ClearHandler clearHandler = new ClearHandler(new ClearService(userDAO, authDAO, gameDAO));

        javalin.post("/user", userHandler::registerUser);
        javalin.post("/session", userHandler::loginUser);
        javalin.delete("/session", userHandler::logoutUser);

        javalin.get("/game", gameHandler::listGames);
        javalin.post("/game", gameHandler::createGame);
        javalin.put("/game", gameHandler::joinGame);

        javalin.delete("/db", clearHandler::clear);

        // Register your endpoints and exception handlers here.
        // Need to still do exception handlers
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
