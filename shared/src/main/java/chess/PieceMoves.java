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
    public enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST
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
    public List<ChessMove> getQueenMoves() {
        moves = new ArrayList<ChessMove>();
        BiFunction<Direction, Integer, ChessPosition> movementRook = (dir, magnitude) -> {
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
        BiFunction<Direction, Integer, ChessPosition> movementBishop = (dir, magnitude) -> {
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
        straightMoves(movementRook);
        straightMoves(movementBishop);
        return moves;
    }
    public List<ChessMove> getKnightMoves() {
        moves = new ArrayList<ChessMove>();
        BiFunction<Direction, Integer, ChessPosition> movement1 = (dir, magnitude) -> {
            ChessPosition end;
            if (magnitude > 1) {
                // Just trying to get it to return an error
                end =  new ChessPosition(20, 20);
            }
            else if (dir == Direction.NORTH) {
                end = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()-1);
            }
            else if (dir == Direction.SOUTH) {
                end = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()+1);
            }
            else if (dir == Direction.EAST) {
                end = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()+1);
            }
            else {
                end = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()-1);
            }
            return end;
        };
        BiFunction<Direction, Integer, ChessPosition> movement2 = (dir, magnitude) -> {
            ChessPosition end;
            if (magnitude > 1) {
                // Just trying to get it to return an error
                end =  new ChessPosition(20, 20);
            }
            else if (dir == Direction.NORTH) {
                end = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-2);
            }
            else if (dir == Direction.SOUTH) {
                end = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-2);
            }
            else if (dir == Direction.EAST) {
                end = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+2);
            }
            else {
                end = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+2);
            }
            return end;
        };
        straightMoves(movement1);
        straightMoves(movement2);
        return moves;
    }
    public List<ChessMove> getKingMoves() {
        moves = new ArrayList<ChessMove>();
        BiFunction<Direction, Integer, ChessPosition> movement1 = (dir, magnitude) -> {
            ChessPosition end;
            if (magnitude > 1) {
                // Just trying to get it to return an error
                end =  new ChessPosition(20, 20);
            }
            else if (dir == Direction.NORTH) {
                end = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
            }
            else if (dir == Direction.SOUTH) {
                end = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
            }
            else if (dir == Direction.EAST) {
                end = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1);
            }
            else {
                end = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1);
            }
            return end;
        };
        BiFunction<Direction, Integer, ChessPosition> movement2 = (dir, magnitude) -> {
            ChessPosition end;
            if (magnitude > 1) {
                // Just trying to get it to return an error
                end =  new ChessPosition(20, 20);
            }
            else if (dir == Direction.NORTH) {
                end = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
            }
            else if (dir == Direction.SOUTH) {
                end = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
            }
            else if (dir == Direction.EAST) {
                end = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);
            }
            else {
                end = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);
            }
            return end;
        };
        straightMoves(movement1);
        straightMoves(movement2);
        return moves;
    }


    public List<ChessMove> getPawnMoves() {
        int mult = 1;
        if (board.getColor(myPosition) == ChessGame.TeamColor.BLACK) {
            mult = -1;
        }
        int x = 1;
        ChessMove move;
        do {
            move = new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+(mult), myPosition.getColumn()+(x)));
            if (isMoveValid(board, move) && isMoveCapture(board,move)) {
                moveAndPromote(move);
            }
            x = x * -1;
        } while (x != 1);
        move = new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+(mult), myPosition.getColumn()));
        if (isMoveValid(board, move) && !isMoveCapture(board,move)) {
            moveAndPromote(move);
            move = new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+(2*mult), myPosition.getColumn()));
            if (isMoveValid(board, move) && !isMoveCapture(board,move) && !board.getPiece(move.getStartPosition()).checkMoved()) {
                if (move.getStartPosition().getRow() == 2|| move.getStartPosition().getRow() == 7) {
                    moves.add(move);
                }
            }
        }
        for (ChessMove movin : moves) {
            System.out.println(movin.getEndPosition().toString());
        }
        return moves;
        // Implement En Passant Later
        // Implement Promotion Later
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
        if (board.getPiece(end) != null && board.getColor(end) == board.getColor(move.getStartPosition())) {
            return false;
        }
        return true;
    }
    private boolean isMoveCapture(ChessBoard board, ChessMove move) {
        var end = move.getEndPosition();
        if (board.getPiece(end) != null && board.getColor(end) != board.getColor(move.getStartPosition())) {
            return true;
        }
        return false;
    }
    private void moveAndPromote(ChessMove move) {
        if (move.getEndPosition().getRow() == 8 || move.getEndPosition().getRow() == 1) {
            ChessMove newMove;
            for (var promotion:  ChessPiece.PieceType.values()) {
                if (promotion == ChessPiece.PieceType.PAWN || promotion == ChessPiece.PieceType.KING) {
                    continue;
                }
                newMove = new ChessMove(move.getStartPosition(), move.getEndPosition(), promotion);
                moves.add(newMove);
            }
        }
        else {
            moves.add(move);
        }
    }
}
