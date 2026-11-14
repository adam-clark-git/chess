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

    public void register(String username, String password, String email)  throws Exception {
        RegisterRequest request = new RegisterRequest(username,password,email);
        String url = serverUrl + "/user";
        String body = gson.toJson(new RegisterRequest(username, password, email));
        var result = sendRequest(url, "POST", body);
        var interpreted_result = receiveResponse(result, RegisterResult.class);
    }
    /*
    public static void main(String[] args) throws Exception {
        if (args.length >= 2) {
            var method = args[0];
            var url = args[1];
            var body = args.length == 3 ? args[2] : null;

            HttpResponse<String> response = sendRequest(url, method, body);
            System.out.printf("= Request =========\n[%s] %s\n\n%s\n\n", method, url, body);
            receiveResponse(response);
        } else {
            System.out.println("ClientCurlExample <method> <url> [<body>]");
        }
    }
    */
    private static HttpResponse<String> sendRequest(String url, String method, String body)
            throws InterruptedException, IOException {
        // Need to create a new URL based on the path and what the function needs
        // Need to convert into a JSON object before passing it into string
        var request = HttpRequest.newBuilder(URI.create(url))
                .method(method, requestBodyPublisher(body))
                .build();
        return client.send(request, BodyHandlers.ofString());
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
            throw new ResponseException((String) error.get("message"));
        }
        if (responseClass == null) {
            return null;
        }
        var responseBody = new Gson().fromJson(response.body(), responseClass);
        return responseBody;
    }
}
