package client;
import java.util.Scanner;
import chess.*;

import static ui.EscapeSequences.*;

public class ClientMain {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        Scanner scanner = new Scanner(System.in);
        System.out.println("♕ 240 Chess Client: " + piece);
        boolean loggedIn = false;
        ChessGame game = new ChessGame();
        BoardRenderer.printBoard(game.getBoard(), ChessGame.TeamColor.WHITE);
        while (true) {
            System.out.print("[LOGGED_OUT] >>> ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String line = scanner.nextLine().toLowerCase();
            if (line.equals("help")) {
                help();
            }
            else {
                break;
            }
        }
    }
    private static void help() {
        System.out.println(SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL>" + RESET_TEXT_COLOR + " - Creates a new account\n" +
                SET_TEXT_COLOR_BLUE + "login <USERNAME> <PASSWORD>" + RESET_TEXT_COLOR + " - Logins into existing account\n" +
                SET_TEXT_COLOR_BLUE + "quit" + RESET_TEXT_COLOR + " - Stop running program\n" +
                SET_TEXT_COLOR_BLUE + "help" + RESET_TEXT_COLOR + " - Displays possible commands\n");
    }
}
