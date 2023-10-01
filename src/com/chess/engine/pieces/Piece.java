package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {

    protected final PieceType pieceType;
    protected final int piecePosition;
    private final Alliance pieceAlliance;
    private final boolean isFirstMove;
    private final int cachedHashCode;

    Piece(PieceType pieceType, final int piecePosition, final Alliance pieceAlliance) {
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        isFirstMove = false;
        cachedHashCode = computeHashCode();
    }

    private int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + ((isFirstMove) ? 1 : 0);
        return result;
    }

    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public abstract Piece movePiece(final Move move);

    public boolean isFirstMove() {
        return isFirstMove;
    }

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;

        if (!(other instanceof Piece)) return false;

        final Piece otherPiece = (Piece) other;

        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() && pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove();
    }

    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    public enum PieceType {
        PAWN("P"), KNIGHT("N"), BISHOP("B"), ROOK("R"), QUEEN("Q"), KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }
        };

        private final String pieceName;

        PieceType(final String pieceName) {
            this.pieceName = pieceName;
        }

        public boolean isKing() {
            return false;
        }

        public boolean isRook() {
            return false;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }
    }

}
