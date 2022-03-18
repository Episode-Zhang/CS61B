package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author Episode Zhang
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;
        // set board view perspective
        board.setViewingPerspective(side);
        // mark whether a tile comes from merging.
        final int size = size();
        Boolean[][] already_merged = initializeArray(size, false);
        // Considering upward keyboard hit
        // Traverse for columns at first, then check by row
        // The direction of row checking is from top to bottom(actually top is ignored, however).
        for (int col = 0; col < size; col++) {
            for (int row = size - 2; row >= 0; row--) {
                Tile current_tile = tile(col, row);
                int available_pos = farthestAvailableMove(current_tile, board, side, already_merged);
                // valid circumstance
                if (available_pos != -1) {
                    int available_col = available_pos / 10;
                    int available_row = available_pos % 10;
                    // check if is a merge
                    if (board.move(available_col, available_row, current_tile)) {
                        score += tile(available_col, available_row).value();
                        already_merged[available_row][available_col] = true;
                    }
                    changed = true;
                }
            }
        }
        checkGameOver();
        if (changed) {
            setChanged();
        }
        board.setViewingPerspective(Side.NORTH);
        return changed;
    }
    
    /** Initialization for array ALREADYMERGED in TILT method*/
    private Boolean[][] initializeArray(int size, Boolean initial_value) {
        Boolean[][] array = new Boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                array[i][j] = initial_value;
            }
        }
        return array;
    }

    /** Searching for the farthest available movement of a tile in upward
     *  direction. Return -1 iff there is no available movement
     */
    private int farthestAvailableMove(Tile t, Board b, Side s, Boolean[][] merged) {
        /* pos to store the coordinate of the farthest available move
           in the form of digits, first as COL and second as ROW*/
        int pos = -1;
        if (t == null) {
            return pos;
        }
        // non-null tile t
        // Get the real col, since method setViewingPerspective just serve
        // transformation of view but without any change to col or row of tile
        int converted_coordinate = convertCoordinate(t, s);
        int real_t_col = converted_coordinate / 10;
        int real_t_row = converted_coordinate % 10;
        // ! buggy cuz merge may leap its obstacle
        for (int i = real_t_row + 1; i < b.size(); i++) {
            Tile scanned_tile = b.tile(real_t_col, i);
            if (scanned_tile == null) {
                // move greedily
                pos = real_t_col * 10 + i;
            }
            else {
                // check if is able to merge
                // be aware of that a after-merged tile couldn't merge again
                if (!merged[i][real_t_col] && scanned_tile.value() == t.value()) {
                    pos = real_t_col * 10 + i;
                    // merge should not be greedy
                    break;
                }
            }
        }
        return pos;
    }

    /** Convert a coordinate from original side.NORTH view into 
     *  corresponding new VIEW
     */
    private int convertCoordinate(Tile t, Side s) {
        // first digit to store col, second digit to store row
        int coordinate = -1;
        final int upper = size() - 1;
        int col = 0, row = 0;
        
        switch (s) {
        case NORTH:
            col = t.col();
            row = t.row();
            break;
        case SOUTH:
            col = upper - t.col();
            row = upper - t.row();
            break;
        case EAST:
            col = upper - t.row();
            row = t.col();
            break;
        case WEST:
            col = t.row();
            row = upper - t.col();
            break;
        default:
            break;
        }
        
        coordinate = col * 10 + row;
        return coordinate;
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     */
    public static boolean emptySpaceExists(Board b) {
        Boolean has_empty_space = false;
        for (int i = 0; i < b.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                if (b.tile(i, j) == null) {
                    has_empty_space = true;
                    break;
                }
            }
        }
        return has_empty_space;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        Boolean has_max_tile = false;
        for (int i = 0; i < b.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                Tile current_tile = b.tile(i, j);
                if (current_tile != null && current_tile.value() == MAX_PIECE) {
                    has_max_tile = true;
                    break;
                }
            }
        }
        return has_max_tile;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        if (emptySpaceExists(b) || canMerge(b)) {
            return true;
        }
        return false;
    }

    /**
     * To judge whether there exists any two-tiles within the given board
     * can be merged into one.
     */
    private static Boolean canMerge(Board b) {
        Boolean can_merge = false;
        int[][] numerical_tile = copyNumericalBoardWithSentinel(b);
        /* Judgement
         * 
         * For a particular tile, just scan its right-side neighbor
         * and below-side neighbor to check if they can merge.
         * 
         * Because of the calculation of scan can be seen commutative
         * (or symmetric, whatever), aka, once a tile can merge its
         * right-side neighbor, it is equivalent to say the right one
         * can merge its left-side neighbor. So, check one side is enough
         * 
         * The existence of sentinels ensures our for-loop won't out of
         * range
         */
        for (int i = 0; i < b.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                int right_neighbor = numerical_tile[i][j + 1];
                int below_neighbor = numerical_tile[i + 1][j];
                if (numerical_tile[i][j] == right_neighbor || 
                numerical_tile[i][j] == below_neighbor) {
                    can_merge = true;
                    break;
                }
            }
        }
        return can_merge;
    }

    /**
     * Generate a copy of board, but only numerical parts of tiles with
     * the board with sentinels.
     * For example, the board
     * 
     *      |   2|   4|   2|   4|
     *      |  16|   2|   4|   2|
     *      |   2|   4|   2|   4|
     *      |   4|   2|   4|   2|    (4 * 4)
     * 
     *  will be copied and generated as 
     * 
     *    |   2|   4|   2|   4|  -1|
     *    |  16|   2|   4|   2|  -1|
     *    |   2|   4|   2|   4|  -1|
     *    |   4|   2|   4|   2|  -1|
     *    |  -1|  -1|  -1|  -1|  -1|   (5 * 5)
     * 
     *  where -1 acts as sentinel
     */
    private static int[][] copyNumericalBoardWithSentinel(Board b) {
        final int copy_board_size = b.size() + 1;
        int[][] copied_board = new int[copy_board_size][copy_board_size];
        
        for (int i = 0; i < copy_board_size; i++) {
            for (int j = 0; j < copy_board_size; j++) {
                // non-sentinel tile
                if (i < b.size() && j < b.size()) {
                    copied_board[i][j] = b.tile(i, j).value();
                }
                else {
                    copied_board[i][j] = -1;
                }
            }
        }

        return copied_board;
    }

    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
