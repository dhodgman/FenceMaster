/* COMP30024 Artificial Intelligence
 * FenceMaster AI
 * Authors: Rosa Luna <rluna> and Ryan Hodgman <hodgmanr>
 */
package rluna.fencemaster;

import java.io.PrintStream;
import java.util.ArrayList;
import aiproj.fencemaster.*;

public class Rluna implements Player, Piece {
/* The class variables */
	
	/** Stores the dimension of the board **/
	private int dim;
	
	/** Stores the number of tiles on the board **/
	private int num_tiles;
	
	/** Stores the current state of the game**/
	private ArrayList<Tile> game_board;
	
	private int piece;
	
	/* This function when called by referee should return the winner  
	 */
	public int getWinner() {
		return 0;
	}

	/* Function called by referee to initialize the player.
	 *  Return 0 for successful initialization and -1 for failed one.
	 */
	public int init(int n, int p) {
		if(p != WHITE && p != BLACK){
			// Invalid piece colour given to player
			return -1;
		}
		if(n<1){ //what is min board size?
			return -1;
		}
		piece = p;
		dim = n;
		num_tiles = 3*dim*(dim-1)+1;
		// Creates a coordinate array to store the board position of each tile.
        int[][] coord = new int[num_tiles][2];
    	int x = 0;
    	int y = 0;
    	int increment = 0;
        for (int i = 0; i < num_tiles; i++) {
        	if(y - x == dim && x < dim - 1){
        		x++;
        		y = 0;
        	} else if(y - x == dim && x == dim - 1) {
        		x++;
        		y = 1;
        		increment++;
        	} else if(y + increment - x == dim && x > dim - 1) {
    			x++;
    			increment++;
    			y = increment;
    		}
        	coord[i][0] = x;
        	coord[i][1] = y;
        	y++;
        }
        // Creates a new ArrayList for an empty board
        game_board = new ArrayList<Tile>(num_tiles);
        for (int i = 0; i < num_tiles; i++) {
        	Tile tile = new Tile(dim, '-', coord[i][0], coord[i][1]);
        	game_board.add(i, tile);
        }
		return 0;
	}

	/* Function called by referee to request a move by the player.
	 *  Return object of class Move
	 */
	public Move makeMove() {
		Move new_move = new Move(piece, false, 0, 0 );
		// just for testing
		return new_move;
	}

	/* Function called by referee to inform the player about the opponent's move
	 *  Return -1 if the move is illegal otherwise return 0
	 */
	public int opponentMove(Move m) {
		if(m.Row >= 0 && m.Row < dim){
			//In top half of the board
			if(m.Col < 0 || m.Col > (dim - 1 + m.Row)){
				//outside of y values for row
				return -1;
			} else {
				if(/*game_board.get(tile_id) == EMPTY ||*/ m.IsSwap){
					// board tile is empty so can place a piece on it
					// Or the player has swapped a piece - need to figure out how to determine it 
					// was 2nd player's 1st shot
					// update board with opponent's new move
					return 0;	
				}
				return -1;
			}
		}
		else if(dim <= m.Row && m.Row <= (2*dim - 2)){
			if(m.Col < (m.Row - dim + 1) || m.Col > (2*dim -2)) {
          		return -1;
            } else {
				if(m.P == EMPTY || m.IsSwap){
					// board tile is empty so can place a piece on it
					// update board with opponent's new move
					return 0;	
				}
				return -1;
            }
		}
		return -1;
	}

	/* Function called by referee to get the board configuration in String format
	 * from player 
	 */
	public void printBoard(PrintStream output) {
		char[] current_board = new char[num_tiles];
        for (int i = 0; i < num_tiles; i++) {
        	current_board[i] = game_board.get(i).getPiece();
        }
        output.println(current_board);
	//sends the board as a single string
	}

}
