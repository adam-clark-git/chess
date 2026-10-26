package service.endpoints;

import chess.ChessGame;

public record JoinGameRequest(String authToken, ChessGame.TeamColor gameColor, int gameID){}
