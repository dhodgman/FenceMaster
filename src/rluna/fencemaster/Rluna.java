/* COMP30024 Artificial Intelligence
 * FenceMaster AI
 * Authors: Rosa Luna <rluna> and Ryan Hodgman <hodgmanr>
 */
package rluna.fencemaster;

import java.io.PrintStream;
import java.util.ArrayList;
import aiproj.fencemaster.*;

/** The player class containing our AI code. */
public class Rluna implements Player, Piece {
/* The class variables. */
    /** A constant representing the minimum realistic board size. */
    public static final int MIN_BOARD_SIZE = 2;
    
	/** The dimension of the board. **/
	private int dim;
	
	/** The number of tiles on the board. **/
	private int num_tiles;
	
	/** The current state of the game. **/
	private ArrayList<Tile> game_state;
	
	/** This player's colour. **/
	private int piece;
	
	/** The number of moves made so far in the game. **/
	private int move_number;
	
/* The getter and setter methods. */   
    /** Returns the board dimension. */
    public int getDim() {
        return dim;
    }
    
    /** Returns the number of tile on the board. */
    public int getNumTiles() {
        return num_tiles;
    }
    
    /** Returns an ArrayList representing the current state of the board. */
    public ArrayList<Tile> getGameState() {
        return game_state;
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
     * @param n The dimension of the board to be played on.
     * @param p The piece colour assigned to this player. 
     * @return Returns 0 if the initialization was successful and -1 if unsuccessful. */
	public int init(int n, int p) {
		// Guard to check that the assigned piece colour is valid.
		if(p != WHITE && p != BLACK) {
			return -1;
		}
		// Guard to check that the given board size is valid.
		if(n < MIN_BOARD_SIZE) {
			return -1;
		}
		piece = p;
		dim = n;
		move_number = 0;
		num_tiles = 3*dim*(dim - 1) + 1;
		// Creates a coordinate array to represent the board position of each tile.
        int[][] coord = new int[num_tiles][2];
    	int col = 0;
    	int row = 0;
    	int increment = 0;
        for (int i = 0; i < num_tiles; i++) {
        	if(row - col == dim && col < dim - 1){
        		col++;
        		row = 0;
        	} else if(row - col == dim && col == dim - 1) {
        		col++;
        		row = 1;
        		increment++;
        	} else if(row + increment - col == dim && col > dim - 1) {
    			col++;
    			increment++;
    			row = increment;
    		}
        	coord[i][0] = col;
        	coord[i][1] = row;
        	row++;
        }
        // Initialises the game state to represent an empty board.
        game_state = new ArrayList<Tile>(num_tiles);
        for (int i = 0; i < num_tiles; i++) {
        	Tile tile = new Tile(dim, EMPTY, coord[i][0], coord[i][1]);
        	game_state.add(i, tile);
        }
		return 0;
	}

/* The class methods */
	/** Called by the referee to determine if the current board state has resulted in a winner.
	 * @return Returns INVALID if there is no winner yet and BLACK or WHITE if the respective player has won. */
	// The code from part A will go in this function call.
	@SuppressWarnings("unused")
	public int getWinner() {
		// Check if white player has won.
		if(WHITE == 0) {
			return WHITE;
		} 
		// Check if black player has won.
		else if(BLACK == 1) {
			return BLACK;
		}
		// Game hasn't yet concluded.
		return INVALID;	
	}

	/** Called by the referee to ask this AI to calculate its next move.
	 * @return Returns the new Move object determined by the gameplay algorithm. */
	public Move makeMove() {
		// Test initialization. Will implement AI algorithms here.
		Move new_move = new Move(piece, false, 0, 0);
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
			return -1;
		}
		// Look up the move's tile ID. [THIS COULD BE MORE EFFICIENT!]
		int move_ID = 0;
		for(int i = 0; i < num_tiles; i++) {
			if(m.Row == game_state.get(i).getRow() && m.Col == game_state.get(i).getCol()) {
				move_ID = i;
				break;
			}
		}
		// Checks if the location is in the top half of the board.
		if(m.Row >= 0 && m.Row < dim) {
			// Guard to check if the location is outside of the upper column boundaries.
			if(m.Col < 0 || m.Col > (dim - 1 + m.Row)) {
				return -1;
			} else {
				// Checks that either the location is empty or the move is a legal swap move. ***
				if(game_state.get(move_ID).getPiece() == EMPTY || m.IsSwap) {
					// The move is valid, so update the game state to reflect the change.
					game_state.get(move_ID).setPiece(m.P);
					return 0;	
				}
				// There was already a piece in that location and it wasn't a legal swap move.
				return -1;
			}
		}
		// Else the location is in the bottom half of the board.
		else if(dim <= m.Row && m.Row <= (2*dim - 2)) {
			// Guard to check if the location is outside of the lower column boundaries.
			if(m.Col < (m.Row - dim + 1) || m.Col > (2*dim -2)) {
          		return -1;
			} else {
				// Executes the same algorithm as for the top half of the board. [Refer to ***]
				if(game_state.get(move_ID).getPiece() == EMPTY || m.IsSwap) {
					game_state.get(move_ID).setPiece(m.P);
					return 0;	
				}
				return -1;
			}
		}
		// If the location is not in the top or bottom halves of the board it is outside of the row boundaries.
		return -1;
	}

	/** Called by the referee to print the board configuration according to this player. 
	 * Prints the output as a simple string with no formatting.
	 * @param output The output pipe. */
	public void printBoard(PrintStream output) {
		char[] current_board = new char[num_tiles];
        for (int i = 0; i < num_tiles; i++) {
        	current_board[i] = (char) game_state.get(i).getPiece();
        }
        output.println(current_board);
	}
}