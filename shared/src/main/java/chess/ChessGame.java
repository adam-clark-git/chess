package chess;

import java.util.*;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard board;
    public ChessGame() {
        board = new ChessBoard();
        teamTurn = TeamColor.WHITE;
        board.resetBoard();
    }
    public ChessGame(ChessGame game) {
        board = new ChessBoard(game.getBoard());
        teamTurn = game.getTeamTurn();
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) return false;
        var game = (ChessGame) obj;
        //System.out.println("OTHER: " + game.getTeamTurn() + "\n" + game.getBoard().toString());
        //System.out.println("THIS: " + teamTurn + "\n" + board.toString());
        return (game.getTeamTurn() == teamTurn && board.equals(game.getBoard()));
    }
    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }
    @Override
    public String toString() {
        return teamTurn + "'s turn: \n" + board.toString();
    }
    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) return null;
        TeamColor color = piece.getTeamColor();
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        return checkMovesValid(moves, color);
    }
    private Collection<ChessMove> checkMovesValid(Collection<ChessMove> moves, TeamColor color) {
        Collection<ChessMove> validMoves = new ArrayList<ChessMove>();
        for (ChessMove move : moves) {
            ChessGame game = new ChessGame(this);
            game.sloppyMove(move);
            if (!game.isInCheck(color)) {
                validMoves.add(move);
                //System.out.println("FakeBoard \n" +game.getBoard().toString());
                //System.out.println(move);
            }
        }
        return validMoves;
    }
    private List<ChessMove> allMoves(TeamColor color) {
        List<ChessMove> moves = new ArrayList<ChessMove>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);
                if (piece != null) {
                    if (piece.getTeamColor() == color) {
                        moves.addAll(piece.pieceMoves(board, position));
                    }
                }
            }
        }
        return moves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (board.getPiece(move.startPosition) == null) throw new InvalidMoveException("Starting Position is empty");
        if (board.getPiece(move.startPosition).getTeamColor() != teamTurn) throw new InvalidMoveException("Not color's turn");
        if (!validMoves(move.startPosition).contains(move)) throw new InvalidMoveException("Not in List");
        if (isInCheck(teamTurn)) throw new InvalidMoveException("King is in check");

        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (move.getPromotionPiece() != null) {
            board.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
        }
        else {
            board.addPiece(move.getEndPosition(), piece);
        }
        board.addPiece(move.getStartPosition(), null);
        setTeamTurn(getOppositeColor(teamTurn));
        //System.out.println(toString());
    }
    // Move that doesn't check if it results in check
    private void sloppyMove(ChessMove move) {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (move.getPromotionPiece() != null) {
            board.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
        }
        else {
            board.addPiece(move.getEndPosition(), piece);
        }
        board.addPiece(move.getStartPosition(), null);
        setTeamTurn(getOppositeColor(teamTurn));

    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //System.out.println(teamColor);
        ChessPosition kingPosition = findKing(teamColor);
        //System.out.println( "KING HERE" + kingPosition);
        TeamColor oppositeTeam = getOppositeColor(teamColor);
        List<ChessMove> moves = allMoves(oppositeTeam);
        for (ChessMove move : moves) {
            //System.out.println( "Google en passant" + move.toString());
            if (move.getEndPosition().equals(kingPosition)) {
                System.out.println( "Check" + move.toString());
                return true;
            }
        }
        return false;
    }
    private ChessPosition findKing(TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(position);
                if (piece != null) {
                    if (piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING) {
                        return position;
                    }
                }
            }
        }
        return null;
    }
    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        Collection<ChessMove> moves = allMoves(teamColor);
        Collection<ChessMove> validMoves = checkMovesValid(moves, teamColor);
        /*if (moves.size() < 5) {
            System.out.println("Few possible moves:");
            for (ChessMove move : moves) {
                System.out.println(move.toString());
            }
        }*/
        if (validMoves.isEmpty()) {
            System.out.println("Checkmate");
            return true;
        }
        return false;
    }
    private TeamColor getOppositeColor(TeamColor teamColor) {
        if (teamColor == TeamColor.WHITE) return TeamColor.BLACK;
        return TeamColor.WHITE;
    }
    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        Collection<ChessMove> moves = allMoves(teamColor);
        Collection<ChessMove> validMoves = checkMovesValid(moves, teamColor);
        return (validMoves.isEmpty());
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
