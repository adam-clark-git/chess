package model.endpoints;

import chess.ChessGame;

public record JoinGameRequest(ChessGame.TeamColor playerColor, int gameID){}
