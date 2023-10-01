package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorMove;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {

    private static final int[] CANDIDATE_MOVE_COORDINATE = {7, 8, 9, 16};

    public Pawn(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.PAWN, piecePosition, pieceAlliance);
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset, final Alliance pieceAlliance) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && ((pieceAlliance.isBlack() && candidateOffset == 7) || (pieceAlliance.isWhite()) && candidateOffset == 9);
    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset, final Alliance pieceAlliance) {
        return BoardUtils.EIGHT_COLUMN[currentPosition] && ((pieceAlliance.isWhite() && candidateOffset == 7) || (pieceAlliance.isBlack()) && candidateOffset == 9);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {

            int candidateDestinationCoordinate = this.piecePosition + this.getPieceAlliance().getDirection() * currentCandidateOffset;

            if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }

            if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
            } else if (currentCandidateOffset == 16 && this.isFirstMove() && (BoardUtils.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isWhite()) || (BoardUtils.SEVENTH_ROW[this.piecePosition] && this.getPieceAlliance().isBlack())) {
                final int behindCandidateDestinationCoordinate = this.piecePosition + this.getPieceAlliance().getDirection() * 8;
                if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                }
            } else if (!isEightColumnExclusion(this.piecePosition, currentCandidateOffset, this.getPieceAlliance()) && !isFirstColumnExclusion(this.piecePosition, currentCandidateOffset, this.getPieceAlliance())) {
                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (pieceOnCandidate.getPieceAlliance() != this.getPieceAlliance()) {
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);

    }

    @Override
    public Pawn movePiece(final Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }
}
