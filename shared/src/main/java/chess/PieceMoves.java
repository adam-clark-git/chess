package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

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
        NORTHEAST,
        EAST,
        SOUTHEAST,
        SOUTH,
        SOUTHWEST,
        WEST,
        NORTHWEST
    }

    private ChessMove rookStep(Direction dir, int num) {
        switch (dir) {
            case NORTH -> {return new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+num, myPosition.getColumn()), null);}
            case SOUTH -> {return new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-num, myPosition.getColumn()), null);}
            case EAST -> {return new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn()+num), null);}
            case WEST -> {return new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn()-num), null);}
        }
        return null;
    }

    private ChessMove bishopStep(Direction dir, int num) {
        switch (dir) {
            case NORTHEAST -> {return new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+num, myPosition.getColumn()+num), null);}
            case NORTHWEST -> {return new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+num, myPosition.getColumn()-num), null);}
            case SOUTHEAST -> {return new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-num, myPosition.getColumn()+num), null);}
            case SOUTHWEST -> {return new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-num, myPosition.getColumn()-num), null);}
        }
        return null;
    }

    public List<ChessMove> getRookMoves() {
        moves = new ArrayList<ChessMove>();
        BiFunction<Direction, Integer, ChessMove> movement = this::rookStep;
        straightMoves(movement);
        return moves;
    }

    public List<ChessMove> getBishopMoves() {
        moves = new ArrayList<ChessMove>();
        BiFunction<Direction, Integer, ChessMove> movement = this::bishopStep;
        straightMoves(movement);
        return moves;
    }

    public List<ChessMove> getQueenMoves() {
        moves = new ArrayList<ChessMove>();
        BiFunction<Direction, Integer, ChessMove> movement = (dir, num) -> {
            ChessMove move = rookStep(dir, num);
            return move != null ? move : bishopStep(dir, num);
        };
        straightMoves(movement);
        return moves;
    }

    public List<ChessMove> getKnightMoves() {
        moves = new ArrayList<ChessMove>();
        BiFunction<Direction, Integer, ChessMove> movement = (dir, num) -> {
            if (num > 1) return null;
            switch (dir) {
                case NORTH -> {return new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()+1), null);}
                case SOUTH -> {return new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()+1), null);}
                case EAST -> {return new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()-1), null);}
                case WEST -> {return new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()-1), null);}
                case NORTHEAST -> {return new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+2), null);}
                case NORTHWEST -> {return new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-2), null);}
                case SOUTHEAST -> {return new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+2), null);}
                case SOUTHWEST -> {return new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-2), null);}
            }
            return null;
        };
        straightMoves(movement);
        return moves;
    }

    public List<ChessMove> getKingMoves() {
        moves = new ArrayList<ChessMove>();
        BiFunction<Direction, Integer, ChessMove> movement = (dir, num) -> {
            if (num > 1) return null;
            ChessMove move = rookStep(dir, num);
            return move != null ? move : bishopStep(dir, num);
        };
        straightMoves(movement);
        return moves;
    }

    public List<ChessMove> getPawnMoves() {
        moves = new ArrayList<ChessMove>();
        int dir = (color() == ChessGame.TeamColor.BLACK) ? -1 : 1;
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        for (int side : new int[]{1, -1}) {
            ChessPosition endPos = new ChessPosition(row + dir, col + side);
            if (isPositionValid(endPos) && isPositionCapture(endPos)) {
                promotePiece(endPos);
            }
        }

        ChessPosition endPos = new ChessPosition(row + dir, col);
        if (isPositionValid(endPos) && !isPositionCapture(endPos)) {
            promotePiece(endPos);
            if ((row == 2 || row == 7) && !board.getPiece(myPosition).checkMoved()) {
                endPos = new ChessPosition(row + (2 * dir), col);
                if (isPositionValid(endPos) && !isPositionCapture(endPos)) {
                    promotePiece(endPos);
                }
            }
        }
        return moves;
    }

    private void straightMoves(BiFunction<Direction, Integer, ChessMove> movement) {
        for (Direction dir : Direction.values()) {
            for (int i = 1; i < 9; i++) {
                ChessMove move = movement.apply(dir, i);
                if (move == null) {
                    break;
                }
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

    private ChessGame.TeamColor color() {
        return board.getColor(myPosition);
    }

    private boolean inBounds(ChessPosition p) {
        return p.getRow() >= 1 && p.getRow() <= 8 && p.getColumn() >= 1 && p.getColumn() <= 8;
    }

    private boolean isMoveValid(ChessBoard board, ChessMove move) {
        return isPositionValid(move.getEndPosition());
    }

    private boolean isMoveCapture(ChessBoard board, ChessMove move) {
        return isPositionCapture(move.getEndPosition());
    }

    private boolean isPositionValid(ChessPosition p) {
        if (!inBounds(p)) {
            return false;
        }
        return board.getPiece(p) == null || board.getColor(p) != color();
    }

    private boolean isPositionCapture(ChessPosition p) {
        return board.getPiece(p) != null && board.getColor(p) != color();
    }

    private void promotePiece(ChessPosition endPos) {
        if (endPos.getRow() == 1 || endPos.getRow() == 8) {
            for (ChessPiece.PieceType type : ChessPiece.PieceType.values()) {
                if (type == ChessPiece.PieceType.PAWN || type == ChessPiece.PieceType.KING) {
                    continue;
                }
                moves.add(new ChessMove(myPosition, endPos, type));
            }
        } else {
            moves.add(new ChessMove(myPosition, endPos, null));
        }
    }
}