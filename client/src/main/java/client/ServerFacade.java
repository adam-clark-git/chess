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
    public ServerFacade(String port) {
        serverUrl = "http://localhost:" + port;
    }

    public void register(String username, String password, String email)  throws Exception {
        RegisterRequest request = new RegisterRequest(username,password,email);
        try {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final HttpClient client = HttpClient.newHttpClient();

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

    private static HttpResponse<String> sendRequest(String url, String method, String body)
            throws InterruptedException, IOException {
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

    private static void receiveResponse(HttpResponse<String> response) {
        var statusCode = response.statusCode();

        var responseBody = new Gson().fromJson(response.body(), Map.class);
        System.out.printf("= Response =========\n[%d]\n\n%s\n\n", statusCode, responseBody);
    }
}
