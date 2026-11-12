package client;
import java.util.Scanner;
import chess.*;

public class ClientMain {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        Scanner scanner = new Scanner(System.in);
        System.out.println("♕ 240 Chess Client: " + piece);
        while (true) {
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
        System.out.println();
    }
}
