package client;

import chess.ChessBoard;
import chess.ChessGame;

import java.util.ArrayList;
import java.util.List;

import static ui.EscapeSequences.*;

public class BoardRenderer {
    static void printBoard(ChessBoard board, ChessGame.TeamColor color) {
        int movement = 1;
        List<String> letters = new ArrayList<>(List.of("a","b","c","d","e","f","g"));
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
                } else {
                    System.out.print(SET_BG_COLOR_LIGHT_GREY);
                    if (i == 0 || i == 9 && j == 0 || j == 9) {
                        // skip
                    }
                    else if (j == 0 || j == 9) {
                        System.out.print(letters.get(i));
                    }
                    else if (i == 0 || i == 9) {
                        System.out.print(j);
                    }
                }

            }
        }
    }
}
