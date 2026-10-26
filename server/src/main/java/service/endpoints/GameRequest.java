package service.endpoints;

public record GameRequest (AuthToken authToken, String gameName){}
