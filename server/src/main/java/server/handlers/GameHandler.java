package server.handlers;

import com.google.gson.Gson;
import io.javalin.http.Context;
import server.exceptions.DataAccessException;
import service.GameService;
import service.UserService;
import service.endpoints.AuthToken;
import service.endpoints.LoginRequest;
import service.endpoints.RegisterRequest;
import service.endpoints.RegisterResult;

public class GameHandler {
    private final GameService gameService;
    private final Gson gson = new Gson();
    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public void listGames(Context ctx) throws DataAccessException {
        var request = ctx.bodyAsClass(AuthToken.class);
        var result = gameService.listGames(request);
        ctx.status(200).result(gson.toJson(result));
    }

}
