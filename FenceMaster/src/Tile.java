/* COMP30024 Artificial Intelligence
 * FenceMaster AI
 * Authors: Rosa Luna <rluna> and Ryan Hodgman <hodgmanr>
 */

package rluna.fencemaster;

import java.util.ArrayList;
import aiproj.fencemaster.*;

/** A representation of the board tiles. */
public class Tile implements Piece{
/* The class variables */
    /** Record of the piece occupying this tile (WHITE: 1, BLACK: 2, EMPTY: 0, INVALID: -1). */
    private int piece;
    
    /** The tile's row (x-coordinate). */
    private int row;
    
    /** The tile's column (y-coordinate). */
    private int col;
    
    /** The board's dimension. */
    private int dim;
    
    /** An adjacency record that contains the tile IDs of the adjacent tiles (-1 = board edge). */
    private int[] adj_record;
    
    /** A constant representing the number of adjacent tiles. */
    public static final int NUM_ADJ = 6;

    /** A visited flag used to indicate if a search has visited this tile. */
    private boolean visited;
    
    /** A group identifier to indicate which group this tile is part of. */
    private int group;
    
    /** A priority value in respect to the black tile groups. */
    private int black_priority;
    
    /** A priority value in respect to the white tile groups. */
    private int white_priority;

/* The getter and setter methods */
    /** Returns the piece (if any) occupying the tile. */
    public int getPiece() {
        return piece;
    }
    
    /** Sets the piece occupying the tile. */
    public void setPiece(int new_piece) {
        this.piece = new_piece;
    }
    
    /** Returns the row of the tile. */
    public int getRow() {
        return row;
    }
    
    /** Sets the row of the tile. */
    public void setRow(int row) {
    	this.row = row;
    }
    
    /** Returns the column of the tile. */
    public int getCol() {
        return col;
    }
    
    /** Sets the column of the tile. */
    public void setCol(int col) {
    	this.col = col;
    }
    
    /** Returns the adjacency record. */
    public int[] getAdj() {
        return adj_record;
    }
    
    /** Returns a specific element in the adjacency record. 
     * @param pos The position of the requested element relative to this tile. */
    public int getAdjElement(int pos) {
        return adj_record[pos];
    }
    
    /** Returns the visited flag. */
    public boolean getVisited() {
        return visited;
    }
    
    /** Sets the visited flag. */
    public void setVisited(boolean flag) {
    	this.visited = flag;
    }
    
    /** Returns the visited flag. */
    public int getGroup() {
        return group;
    }
    
    /** Sets the visited flag. */
    public void setGroup(int num) {
    	this.group = num;
    }
    
    /** Returns the priority relative to all black tiles. */
    public int getBlackPriority() {
        return black_priority;
    }
    
    /** Returns the priority relative to all white tiles. */
    public int getWhitePriority() {
        return white_priority;
    }

/* The constructor(s) */
    /** Creates a new tile object.
     * @param dim The dimension of the hexagonal board.
     * @param piece The piece (if any) occupying this tile.
     * @param row The tile's row.
     * @param col The tile's column. */
    public Tile(int dim, int piece, int row, int col) {
    	this.dim = dim;
    	setPiece(piece);
    	setRow(row);
    	setCol(col);
    	adj_record = new int[NUM_ADJ];
    	adj_record = calcAdj(adj_record);
    	setVisited(false);
    	setGroup(0);
    }

/* The class methods */
    /** Creates and fills the adjacency record.
     * @param num_adj A two-dimensional adjacency record for the tile (assume number of adjacent tile = 6). */
    public int[] calcAdj(int[] num_adj) {
    	// Iterates through the surrounding tiles.
    	for(int i = 0; i < NUM_ADJ; i++) {
    		int[] coord = new int[2];
    		coord = adjCoord(i);
    		int row = coord[0];
    		int col = coord[1];
        	// Checks to see if the tile is on the first half of the board.
        	if(0 <= row && row < dim) {
        		// Checks to see if the tile is within the y-coordinate bounds of the board.
        		if(col < 0 || col > dim - 1 + row) {
            		num_adj[i] = -1;
        		} else {
        			// Assigns the adjacent tile ID.
        			num_adj[i] = (int)(0.5*(row*row + (2*dim - 1)*row) + col);
        		}
        	} 
        	// Checks to see if the tile is on the second half of the board.
        	else if(dim <= row && row <= 2*dim - 2) {
                // Checks to see if the tile is within the y-coordinate bounds of the board.
                if(col < row - dim + 1 || col > 2*dim - 2) {
              		num_adj[i] = -1;
                } else {
                	//Assigns the adjacent tile ID.
                	num_adj[i] = (int)(0.5*((6*row + 4)*dim - 2*dim*dim - row*row - 5*row - 2) + col);
                }
        	} 
        	// Tile not in x-coordinate bounds of the board.
        	else {
        		num_adj[i] = -1;
        	}
    	}
    	// Prints out the adjacency array for each tile on the board.
    	if(Rluna.DEBUG) {
    		for(int q = 0; q < NUM_ADJ; q++) {
    			System.out.println(num_adj[q]);
    		}
    		System.out.println("-----");
    	}
    	return num_adj;
    } 
    
    /** Takes an adjacency position (0 to 5) and returns the coordinates of that tile relevant to the current one.
     * @param pos The position of the desired tile relevant to the current tile. */
    public int[] adjCoord(int pos) {
    	int[] coord = new int[2];
    	if(pos == 0) {
    		coord[0] = getRow() - 1;
    		coord[1] = getCol() - 1;
    	}
    	if(pos == 1) {
    		coord[0] = getRow() - 1;
    		coord[1] = getCol();
    	}
    	if(pos == 2) {
    		coord[0] = getRow();
    		coord[1] = getCol() + 1;
    	}
    	if(pos == 3) {
    		coord[0] = getRow() + 1;
    		coord[1] = getCol() + 1;
    	}
    	if(pos == 4) {
    		coord[0] = getRow() + 1;
    		coord[1] = getCol();
    	}
    	if(pos == 5) {
    		coord[0] = getRow();
    		coord[1] = getCol() - 1;
    	}
    	return coord;
    }
    
    /** Calculates the priorities for this tile, where a tile's priority is determined by the number of white/black pieces adjacent to it.
     * @param tile_list A list of tiles that represents the board state. */
    public void calcPriorities(ArrayList<Tile> tile_list) {
    	for (int i = 0; i < NUM_ADJ; i++) {
    		if(adj_record[i] != -1){
    			// Increments the priorities if the loop finds a black/white piece adjacent to this tile.
        		if(tile_list.get(adj_record[i]).getPiece() == BLACK) {
        			black_priority++;
        		}
        		if(tile_list.get(adj_record[i]).getPiece() == WHITE) {
        			white_priority++;
        		}
    		}
    	}
    }
}