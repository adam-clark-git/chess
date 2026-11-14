package client;
import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.endpoints.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.*;
import java.net.http.HttpRequest.*;
import chess.ChessGame;
import java.util.Map;

public class ServerFacade {
    private final String serverUrl;
    private final Gson gson = new Gson();
    private static final HttpClient client = HttpClient.newHttpClient();
    public ServerFacade(String port) {
        serverUrl = "http://localhost:" + port;
    }

    public RegisterResult register(String username, String password, String email)  throws ResponseException {
        String url = serverUrl + "/user";
        String body = gson.toJson(new RegisterRequest(username, password, email));
        var result = sendRequest(url, "POST", body, null);
        return receiveResponse(result, RegisterResult.class);
    }
    public RegisterResult login(String username, String password) throws ResponseException {
        String url = serverUrl + "/session";
        String body = gson.toJson(new LoginRequest(username, password));
        var result = sendRequest(url, "POST", body, null);
        return receiveResponse(result, RegisterResult.class);
    }
    public void logout(String authToken) throws ResponseException {
        String url = serverUrl + "/session";
        var result = sendRequest(url, "DELETE", null, authToken);
        receiveResponse(result, null);
    }
    public ListGamesResult listGames(String authToken) throws ResponseException {
        String url = serverUrl + "/game";
        var result = sendRequest(url, "GET", null, authToken);
        return receiveResponse(result, ListGamesResult.class);
    }
    public CreateGameResult createGame(String authToken, String gameName) throws ResponseException {
        String url = serverUrl + "/game";
        String body = gson.toJson(new GameRequest(gameName));
        var result = sendRequest(url, "POST", body, authToken);
        return receiveResponse(result, CreateGameResult.class);
    }
    public void joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID) throws ResponseException {
        String url = serverUrl + "/game";
        String body = gson.toJson(new JoinGameRequest(playerColor, gameID));
        var result = sendRequest(url, "PUT", body, authToken);
        receiveResponse(result, null);
    }

    private static HttpResponse<String> sendRequest(String url, String method, String body, String authToken)
            throws ResponseException {
        try {
            var builder = HttpRequest.newBuilder(URI.create(url))
                    .method(method, requestBodyPublisher(body));
            if (authToken != null) {
                builder.header("authorization", authToken);
            }
            return client.send(builder.build(), BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new ResponseException("Could not reach the server.");
        }
    }

    private static BodyPublisher requestBodyPublisher(String body) throws IOException {
        if (body != null) {
            return BodyPublishers.ofString(body);
        } else {
            return BodyPublishers.noBody();
        }
    }

    private <T> T receiveResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        int statusCode = response.statusCode();
        if (statusCode < 200 || statusCode >= 300) {
            var error = gson.fromJson(response.body(), Map.class);
            if (error == null) {
                throw new ResponseException("Request failed.");
            }
            throw new ResponseException((String) error.get("message"));
        }
        if (responseClass == null) {
            return null;
        }
        return gson.fromJson(response.body(), responseClass);
    }
}
