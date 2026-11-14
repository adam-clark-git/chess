package client;
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
        var result = sendRequest(url, "POST", body);
        return receiveResponse(result, RegisterResult.class);
    }

    private static HttpResponse<String> sendRequest(String url, String method, String body)
            throws ResponseException {
        try {
            var request = HttpRequest.newBuilder(URI.create(url))
                    .method(method, requestBodyPublisher(body))
                    .build();
            return client.send(request, BodyHandlers.ofString());
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
        var responseBody = gson.fromJson(response.body(), responseClass);
        return responseBody;
    }
}
