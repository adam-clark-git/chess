package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this){
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) return false;
        var piece = (ChessPiece) obj;
        if (piece.getTeamColor() == getTeamColor() && piece.getPieceType() == getPieceType()) {
            return true;
        }
        return false;
    }
    @Override
    public String toString() {
        String sPiece = "";
        if (type == PieceType.PAWN) {
            sPiece = "p";
        }
        if (type == PieceType.BISHOP) {
            sPiece = "b";
        }
        if (type == PieceType.ROOK) {
            sPiece = "r";
        }
        if (type == PieceType.QUEEN) {
            sPiece = "q";
        }
        if (type == PieceType.KING) {
            sPiece = "k";
        }
        if (type == PieceType.KNIGHT) {
            sPiece = "n";
        }
        if (getTeamColor() == ChessGame.TeamColor.BLACK) {
            sPiece = sPiece.toUpperCase();
        }
        return sPiece;
    }
    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }
}
