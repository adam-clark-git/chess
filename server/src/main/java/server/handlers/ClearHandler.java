package server.handlers;

import server.exceptions.DataAccessException;
import service.ClearService;
import io.javalin.http.Context;

public class ClearHandler {
    private final ClearService clearService;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }
    public void clear(Context ctx) throws DataAccessException {
        clearService.clearAll();
        ctx.status(200).result("{}");
    }
}
