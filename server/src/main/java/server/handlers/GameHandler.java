package server.handlers;

import com.google.gson.Gson;
import io.javalin.http.Context;
import model.endpoints.GameRequest;
import model.endpoints.JoinGameRequest;
import server.exceptions.DataAccessException;
import service.GameService;

public class GameHandler {
    private final GameService gameService;
    private final Gson gson = new Gson();
    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public void listGames(Context ctx) throws DataAccessException {
        String request = ctx.header("authorization");
        var result = gameService.listGames(request);
        ctx.status(200).result(gson.toJson(result));
    }
    public void createGame(Context ctx) throws DataAccessException {
        String token = ctx.header("authorization");
        var body = gson.fromJson(ctx.body(), GameRequest.class);
        GameRequest request = new GameRequest(body.gameName());
        var result = gameService.createGame(token, request);
        ctx.status(200).result(gson.toJson(result));
    }
    public void joinGame(Context ctx) throws DataAccessException {
        String token = ctx.header("authorization");
        var body = gson.fromJson(ctx.body(), JoinGameRequest.class);
        JoinGameRequest request = new JoinGameRequest(body.playerColor(), body.gameID());
        gameService.joinGame(token, request);
        ctx.status(200).result("{}");
    }

}
