package service.endpoints;

import chess.ChessGame;

public record JoinGameRequest(AuthToken authToken, ChessGame.TeamColor gameColor, int gameID){}
