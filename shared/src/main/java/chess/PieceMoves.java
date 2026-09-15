package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PieceMoves {
    List<ChessMove> moves = new ArrayList<ChessMove>();
    ChessBoard board;
    ChessPosition myPosition;
    public PieceMoves(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;

    }
    public List<ChessMove> getBishopMoves() {
        moves =  new ArrayList<ChessMove>();
        BiFunction<Direction, Integer, ChessPosition> movement = (dir, magnitude) -> {
            ChessPosition end;
            if (dir == Direction.NORTH) {
                end = new ChessPosition(myPosition.getRow()+magnitude, myPosition.getColumn()+magnitude);
            }
            else if (dir == Direction.SOUTH) {
                end = new ChessPosition(myPosition.getRow()-magnitude, myPosition.getColumn()-magnitude);
            }
            else if (dir == Direction.EAST) {
                end = new ChessPosition(myPosition.getRow()-magnitude, myPosition.getColumn()+magnitude);
            }
            else {
                end = new ChessPosition(myPosition.getRow()+magnitude, myPosition.getColumn()-magnitude);
            }
            return end;
        };
        straightMoves(movement);
        return moves;
    }
    public List<ChessMove> getRookMoves() {
        moves = new ArrayList<ChessMove>();
        BiFunction<Direction, Integer, ChessPosition> movement = (dir, magnitude) -> {
            ChessPosition end;
            if (dir == Direction.NORTH) {
                end = new ChessPosition(myPosition.getRow()+magnitude, myPosition.getColumn());
            }
            else if (dir == Direction.SOUTH) {
                end = new ChessPosition(myPosition.getRow()-magnitude, myPosition.getColumn());
            }
            else if (dir == Direction.EAST) {
                end = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+magnitude);
            }
            else {
                end = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-magnitude);
            }
            return end;
        };
        straightMoves(movement);
        return moves;
    }
    public enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }
    private void straightMoves(BiFunction<Direction,Integer,ChessPosition> movement) {
        ChessMove move;
        for (Direction dir : Direction.values()){
            for (int mag = 1; mag < 10; mag++) {
                move = new ChessMove(myPosition, movement.apply(dir,mag));
                if (isMoveValid(board, move)) {
                    moves.add(move);
                    if (isMoveCapture(board, move)) {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
    }
    private boolean isMoveValid(ChessBoard board, ChessMove move) {
        var end = move.getEndPosition();
        if (end.getColumn() > 8 || end.getColumn() < 1 || end.getRow() > 8 || end.getRow() < 1) {
            return false;
        }
        if (board.getPiece(end) != null & board.getPiece(end).getTeamColor() == board.getPiece(move.getStartPosition()).getTeamColor()) {
            return false;
        }
        return true;
    }
    private boolean isMoveCapture(ChessBoard board, ChessMove move) {
        var end = move.getEndPosition();
        if (board.getPiece(end) != null & board.getPiece(end).getTeamColor() != board.getPiece(move.getStartPosition()).getTeamColor()) {
            return true;
        }
        return false;
    }
}
