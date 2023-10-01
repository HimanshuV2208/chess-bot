package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import static com.chess.engine.board.Board.Builder;

public abstract class Move {
    public static final Move NULL_MOVE = new NullMove();
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;

    private Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Move)) return false;
        final Move otherMove = (Move) obj;
        return getCurrentCoordinate() == otherMove.getCurrentCoordinate() && getDestinationCoordinate() == otherMove.getDestinationCoordinate() && getMovedPiece().equals(otherMove.getMovedPiece());
    }

    public int getCurrentCoordinate() {
        return this.getMovedPiece().getPiecePosition();
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    public int getDestinationCoordinate() {
        return destinationCoordinate;
    }

    public boolean isAttack() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }

    public Board execute() {

        final Builder builder = new Builder();

        for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }

        for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }

        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(board.currentPlayer().getOpponent().getAlliance());

        return builder.build();
    }

    public static final class MajorMove extends Move {
        public MajorMove(final Board board, final Piece movedPiece, final int destinationCoordinates) {
            super(board, movedPiece, destinationCoordinates);
        }

    }

    public static class AttackMove extends Move {
        final Piece attackedPiece;

        public AttackMove(final Board board, final Piece movedPiece, final int destinationCoordinates, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinates);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public int hashCode() {
            return super.hashCode() + this.attackedPiece.hashCode();
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof AttackMove)) return false;
            final AttackMove otherAttackMove = (AttackMove) obj;
            return super.equals(otherAttackMove) && this.getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return super.getAttackedPiece();
        }

        @Override
        public Board execute() {
            return super.execute();
        }
    }

    public static final class PawnMove extends Move {
        public PawnMove(final Board board, final Piece movedPiece, final int destinationCoordinates) {
            super(board, movedPiece, destinationCoordinates);
        }
    }

    public static class PawnAttackMove extends AttackMove {
        public PawnAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinates, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinates, attackedPiece);
        }

    }

    public static final class PawnEnPassantAttackMove extends PawnAttackMove {
        public PawnEnPassantAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinates, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinates, attackedPiece);
        }

    }

    public static final class PawnJump extends Move {
        public PawnJump(final Board board, final Piece movedPiece, final int destinationCoordinates) {
            super(board, movedPiece, destinationCoordinates);
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }

            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    static abstract class CastleMove extends Move {
        protected final Rook castleRook;

        protected final int castleRookStart;
        protected final int castleRookDestination;

        public CastleMove(final Board board, final Piece movedPiece, final int destinationCoordinates, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinates);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook() {
            return castleRook;
        }

        @Override
        public boolean isCastlingMove() {
            return true;
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRookDestination, this.movedPiece.getPieceAlliance()));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }

    }

    public static final class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinates, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinates, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public String toString() {
            return "O-O";
        }

    }

    public static final class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinates, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinates, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public String toString() {
            return "O-O-O";
        }
    }

    public static final class NullMove extends Move {
        public NullMove() {
            super(null, null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("Cannot execute Null Move!");
        }
    }

    public static class MoveFactory {

        private MoveFactory() {
            throw new RuntimeException("Not Instantiable!");
        }

        public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate) {
            for (final Move move : board.getAllPossibleMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }

    }

}
