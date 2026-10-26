package server.handlers;

import com.google.gson.Gson;
import io.javalin.http.Context;
import server.exceptions.DataAccessException;
import service.GameService;
import service.UserService;
import service.endpoints.*;

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
    public void createGame(Context ctx) throws DataAccessException {
        var request = ctx.bodyAsClass(GameRequest.class);
        var result = gameService.createGame(request);
        ctx.status(200).result(gson.toJson(result));
    }


}
