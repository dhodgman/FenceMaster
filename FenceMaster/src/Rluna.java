/* COMP30024 Artificial Intelligence
 * FenceMaster AI
 * Authors: Rosa Luna <rluna> and Ryan Hodgman <hodgmanr>
 */
package rluna.fencemaster;

import java.io.PrintStream;
import aiproj.fencemaster.*;

/** The player class containing our AI code. */
public class Rluna implements Player, Piece {
/* The class variables. */
    /** A constant representing the minimum realistic board size. */
    public static final int MIN_BOARD_SIZE = 2;
    
	/** A variable used to activate or deactivate debugging mode. */
    public static boolean DEBUG = true;
    
	/** The dimension of the board. **/
	private int dim;
	
    /** Representation of the game state. */
    private static GameState state;
	
	/** This player's colour. **/
	private int piece;
	
	/** The number of moves made so far in the game. **/
	private int move_number;
	
/* The getter and setter methods. */   
    /** Returns the board dimension. */
    public int getDim() {
        return dim;
    }
    
    /** Returns the colour of this player. */
    public int getPiece() {
        return piece;
    }
    
    /** Returns the number of moves that have been made so far. */
    public int getMoveNumber() {
        return move_number;
    }
    
/* The constructor(s) */
    /** Called by the referee to create a new player object.
     * @param dim The dimension of the board to be played on.
     * @param colour The piece colour assigned to this player. 
     * @return Returns 0 if the initialization was successful and -1 if unsuccessful. */
	public int init(int dim, int colour) {
		// Guard to check that the assigned piece colour is valid.
		if(colour != WHITE && colour != BLACK) {
			return -1;
		}
		// Guard to check that the given board size is valid.
		if(dim < MIN_BOARD_SIZE) {
			return -1;
		}
		piece = colour;
		this.dim = dim;
		move_number = 0;
		state = new GameState(dim);
        // Prints out the full list of tiles and their attributes (piece type, x-coord and y-coord).
        if(DEBUG) {
        	for(int t = 0; t < state.getTileList().size(); t++) {
                System.out.println(state.getTileList().get(t).getPiece() + ", [" + state.getTileList().get(t).getRow() + ","+ state.getTileList().get(t).getCol() + "]");
        	}
        }
		return 0;
	}

/* The class methods */
	/** Called by the referee to determine if the current board state has resulted in a winner.
	 * @return Returns INVALID if there is no winner yet and BLACK or WHITE if the respective player has won. */
	public int getWinner() {
		return state.calcResult();
	}

	/** Called by the referee to ask this AI to calculate its next move.
	 * @return Returns the new Move object determined by the gameplay algorithm. */
	public Move makeMove() {
		// Test initialization. Will implement AI algorithms here.
		int row = 0;
		int col = 0;
		Move new_move = new Move(piece, false, row, col);
		// Updates the board state.
		int tile_ID = calcTileID(row, col);
		state.getTileList().get(tile_ID).setPiece(new_move.P);
		// Increment the move count.
		move_number++;
		return new_move;
	}

	/** Called by the referee to inform this player about the opponent's move.
	 * @param m The Move object representing the opponent's last move.
	 * @return Returns 0 if the move is considered legal. Otherwise returns -1. */
	public int opponentMove(Move m) {
		// Increment the move count.
		move_number++;
		// Guard to check that a swap move is legal.
		if(m.IsSwap && move_number != 2) {
			return INVALID;
		}
		// Guard to check that the tile exists within the bounds of the board.
		int tile_ID = calcTileID(m.Row, m.Col);
		if(tile_ID == INVALID) {
			return INVALID;
		}	
		// Checks that either the location is empty or the move is a legal swap move.
		if(state.getTileList().get(tile_ID).getPiece() == EMPTY || m.IsSwap) {
			// The move is valid, so update the game state to reflect the change.
			state.getTileList().get(tile_ID).setPiece(m.P);
			return 0;	
		}
		// Tile location is full and the move is not a swap move.
		return INVALID;
	}

	/** Called by the referee to print the board configuration according to this player. 
	 * Prints the output as a simple string with no formatting.
	 * @param output The output pipe. */
	public void printBoard(PrintStream output) {
		char[] current_board = new char[state.getNumTiles()];
        for (int i = 0; i < state.getNumTiles(); i++) {
        	current_board[i] = (char) state.getTileList().get(i).getPiece();
        }
        output.println(current_board);
	}
	
	/** Calculates the tile ID given a set of row/col coordinates.
	 * @param row The x-coordinate.
	 * @param col The y-coordinate.
	 * @return -1 if the coordinates are invalid and the tile ID if the coordinates are valid. */
	public int calcTileID(int row, int col) {
		// Check to see whether the tile is in the top half of the board.
		if(row >= 0 && row < dim) {
			// Guard to check if the location is outside of the upper column boundaries.
			if(col < 0 || col > (dim - 1 + row)) {
				return INVALID;
			} else {
				// Calculates and returns the tile ID for the top half.
				return (int)(0.5*(row*row + (2*dim - 1)*row) + col);
			}
		}
		// Else the location is in the bottom half of the board.
		else if(dim <= row && row <= (2*dim - 2)) {
			// Guard to check if the location is outside of the lower column boundaries.
			if(col < (row - dim + 1) || col > (2*dim - 2)) {
	      		return INVALID;
			} else {
				// Calculates and returns the tile ID for the bottom half.
				return (int)(0.5*((6*row + 4)*dim - 2*dim*dim - row*row - 5*row - 2) + col);
			}
		}
		// If the location is not in the top or bottom halves of the board it is outside of the row boundaries.
		return INVALID;
	}
}