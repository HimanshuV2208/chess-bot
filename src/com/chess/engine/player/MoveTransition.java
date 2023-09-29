package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public class MoveTransition {

    private final Board transitionedBoard;
    private final Move move;
    private final MoveStatus moveStatus;

    public MoveTransition(final Board transitionedBoard, final Move move, final MoveStatus moveStatus) {
        this.transitionedBoard = transitionedBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }
}
