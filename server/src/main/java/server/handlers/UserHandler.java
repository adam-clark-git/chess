package server.handlers;

import server.exceptions.DataAccessException;
import service.UserService;
import io.javalin.http.Context;
import service.endpoints.*;
import com.google.gson.Gson;

public class UserHandler {
    private final UserService userService;
    private final Gson gson = new Gson();
    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public void registerUser(Context ctx) throws DataAccessException {
        RegisterRequest request = ctx.bodyAsClass(RegisterRequest.class);
        RegisterResult result = userService.registerUser(request);
        ctx.status(200).result(gson.toJson(result));
    }
    public void loginUser(Context ctx) throws DataAccessException {
        var request = ctx.bodyAsClass(LoginRequest.class);
        var result = userService.loginUser(request);
        ctx.status(200).result(gson.toJson(result));
    }
    public void logoutUser(Context ctx) throws DataAccessException {
        var request = ctx.bodyAsClass(AuthToken.class);
        userService.logoutUser(request);
        ctx.status(200).result("{}");
    }
}
