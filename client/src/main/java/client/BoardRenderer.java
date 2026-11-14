package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.List;

import static ui.EscapeSequences.*;

public class BoardRenderer {
    static void printBoard(ChessBoard board, ChessGame.TeamColor color) {
        int movement = 1;
        List<String> letters = new ArrayList<>(List.of("a","b","c","d","e","f","g","h"));
        if (color.equals(ChessGame.TeamColor.BLACK)) {
            // Just assume we're doing it in white for now.
        }
        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j <= 9; j++) {
                if (i > 0 && i < 9 && j > 0 && j < 9) {
                    if ((i + j) % 2 == 0) {
                        System.out.print(SET_BG_COLOR_WHITE);
                    } else {
                        System.out.print(SET_BG_COLOR_BLACK);
                    }
                    System.out.print(translatePiece(board.getPiece(new ChessPosition(i,j))));
                } else {
                    System.out.print(SET_BG_COLOR_LIGHT_GREY);
                    if ((i == 0 || i == 9) && (j == 0 || j == 9)) {
                        System.out.print(EMPTY);
                    }
                    else if (j == 0 || j == 9) {
                        System.out.print(" "+letters.get(i-1)+" ");
                    }
                    else if (i == 0 || i == 9) {
                        System.out.print(" " +j+" ");
                    }
                }

            }
            System.out.println();
        }
    }
    static String translatePiece(ChessPiece piece) {
        if (piece == null) {
            return EMPTY;
        }
        if (piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
            switch (piece.getPieceType()) {
                case KING -> {return WHITE_KING;}
                case QUEEN -> {return WHITE_QUEEN;}
                case PAWN -> {return WHITE_PAWN;}
                case BISHOP -> {return WHITE_BISHOP;}
                case ROOK -> {return WHITE_ROOK;}
                case KNIGHT -> {return WHITE_KNIGHT;}
                case null -> {return EMPTY;}
            }
        } else {
            switch (piece.getPieceType()) {
                case KING -> {return BLACK_KING;}
                case QUEEN -> {return BLACK_QUEEN;}
                case PAWN -> {return BLACK_PAWN;}
                case BISHOP -> {return BLACK_BISHOP;}
                case ROOK -> {return BLACK_ROOK;}
                case KNIGHT -> {return BLACK_KNIGHT;}
                case null -> {return EMPTY;}
            }
        }
    }
}
