package server;

import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.*;
import server.exceptions.*;
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
        javalin.exception(BadRequestException.class, (e, ctx) -> sendError(ctx, 400, e));
        javalin.exception(DataAccessException.class ,(e, ctx) -> sendError(ctx, 500, e)); // Should be changed, I'm unsure of the proper error code
        javalin.exception(AuthException.class ,(e, ctx) -> sendError(ctx, 401, e));
        javalin.exception(ColorTakenException.class ,(e, ctx) -> sendError(ctx, 403, e));
        javalin.exception(AlreadyTakenException.class ,(e, ctx) -> sendError(ctx, 403, e));
        javalin.exception(GameNotFoundException.class ,(e, ctx) -> sendError(ctx, 400, e));
        javalin.exception(PasswordIncorrectException.class ,(e, ctx) -> sendError(ctx, 401, e));


    }

    private void sendError(Context ctx, int status, Exception e) {
        ctx.status(status).result(gson.toJson(Map.of("message", e.getMessage())));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
