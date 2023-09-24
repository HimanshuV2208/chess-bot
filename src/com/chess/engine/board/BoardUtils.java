package com.chess.engine.board;

public class BoardUtils {

    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;
    public static final int ZERO = 0;

    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHT_COLUMN = initColumn(7);

    private BoardUtils() {
        throw new RuntimeException("Not Instantiable");
    }

    public static boolean isValidTileCoordinate(final int coordinate) {
        return coordinate >= ZERO && coordinate < NUM_TILES;
    }

    private static boolean[] initColumn(int columnNumber) {
        final boolean[] board = new boolean[NUM_TILES];
        while (columnNumber < NUM_TILES) {
            board[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        }
        return board;
    }

}
