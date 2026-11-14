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
        List<String> letters = new ArrayList<>(List.of("a","b","c","d","e","f","g","h"));
        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j <= 9; j++) {
                printPosition(i,j,board,color,letters);
            }
            System.out.println();
        }
        System.out.print(RESET_BG_COLOR);
    }
    static void printPosition(int i, int j, ChessBoard board, ChessGame.TeamColor color, List<String> letters) {
        if (i > 0 && i < 9 && j > 0 && j < 9) {
            if ((i + j) % 2 == 0) {
                System.out.print(SET_BG_COLOR_WHITE);
            } else {
                System.out.print(SET_BG_COLOR_BLACK);
            }
            if (color.equals(ChessGame.TeamColor.BLACK)) {
                System.out.print(translatePiece(board.getPiece(new ChessPosition(i, 9-j))));
            } else {
                System.out.print(translatePiece(board.getPiece(new ChessPosition(9-i,j))));
            }
            System.out.print(RESET_TEXT_COLOR);
            return;
        }
        System.out.print(SET_BG_COLOR_LIGHT_GREY);
        if ((i == 0 || i == 9) && (j == 0 || j == 9)) {
            System.out.print(EMPTY);
        } else if (i == 0 || i == 9) {
            if (color.equals(ChessGame.TeamColor.BLACK)) {
                System.out.print(" "+letters.get(8-(j))+"\u2003");
            } else {
                System.out.print(" " + letters.get(j - 1) + "\u2003");
            }
        } else {
            if (color.equals(ChessGame.TeamColor.BLACK)) {
                System.out.print(" " + i+"\u2003");
            }else {
                System.out.print(" " + (9-i) + "\u2003");
            }
        }

    }
    static String translatePiece(ChessPiece piece) {
        if (piece == null) {
            return EMPTY;
        }
        if (piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
            String color = SET_TEXT_COLOR_RED;
            switch (piece.getPieceType()) {
                case KING -> {return color+WHITE_KING;}
                case QUEEN -> {return color+WHITE_QUEEN;}
                case PAWN -> {return color+WHITE_PAWN;}
                case BISHOP -> {return color+WHITE_BISHOP;}
                case ROOK -> {return color+WHITE_ROOK;}
                case KNIGHT -> {return color+WHITE_KNIGHT;}
                case null -> {return EMPTY;}
            }
        } else {
            String color = SET_TEXT_COLOR_BLUE;
            switch (piece.getPieceType()) {
                case KING -> {return color+BLACK_KING;}
                case QUEEN -> {return color+BLACK_QUEEN;}
                case PAWN -> {return color+BLACK_PAWN;}
                case BISHOP -> {return color+BLACK_BISHOP;}
                case ROOK -> {return color+BLACK_ROOK;}
                case KNIGHT -> {return color+BLACK_KNIGHT;}
                case null -> {return EMPTY;}
            }
        }
    }
}
