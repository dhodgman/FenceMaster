/* COMP30024 Artificial Intelligence
 * FenceMaster AI
 * Authors: Rosa Luna <rluna> and Ryan Hodgman <hodgmanr>
 */

package rluna.fencemaster;

import java.util.ArrayList;
import aiproj.fencemaster.*;

/** A representation of the board tiles. Requires that you set the adjacency reocord after initialisation. */
public class Tile implements Piece {
/* The class variables */
    /** A constant representing the number of adjacent tiles. */
    public static final int NUM_ADJ = 6;
    
    /** Record of the piece occupying this tile (WHITE: 1, BLACK: 2, EMPTY: 0, INVALID: -1). */
    private int piece;
    
    /** The tile's row (x-coordinate). */
    private int row;
    
    /** The tile's column (y-coordinate). */
    private int col;

    /** An adjacency record that contains the tile IDs of the adjacent tiles (-1 = board edge). */
    private int[] adj_record;

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
    
    /** Returns the column of the tile. */
    public int getCol() {
        return col;
    }
    
    /** Returns the adjacency record. */
    public int[] getAdj() {
        return adj_record;
    }
    
    /** Sets the column of the tile. */
    public void setAdj(int[] adj_record) {
    	this.adj_record = adj_record;
    }
    
    /** Returns a specific ID in the tile's adjacency record. 
     * @param pos The position of the requested element relative to this tile. 
     * @return Returns the ID of the tile located at the specified position in the adj_record. */
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
    
    /** Returns the group number of this tile. */
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
     * @param piece The piece type (if any) occupying this tile.
     * @param row The tile's row.
     * @param col The tile's column. */
    public Tile(int piece, int row, int col) {
    	this.piece = piece;
    	this.row = row;
    	this.col = col;
    	visited = false;
    	group = 0;
    }

/* The class methods */    
    /** Takes an adjacency position (0 to 5) and returns the coordinates of that tile relevant to the current one.
     * @param pos The position of the desired tile relevant to the current tile. 
     * @return Returns the two-dimensional coordinates of the specified adjacent tile. */
    public int[] adjCoord(int pos) {
    	int[] coord = new int[2];
    	if(pos == 0) {
    		coord[0] = row - 1;
    		coord[1] = col - 1;
    	} else if(pos == 1) {
    		coord[0] = row - 1;
    		coord[1] = col;
    	} else if(pos == 2) {
    		coord[0] = row;
    		coord[1] = col + 1;
    	} else if(pos == 3) {
    		coord[0] = row + 1;
    		coord[1] = col + 1;
    	} else if(pos == 4) {
    		coord[0] = row + 1;
    		coord[1] = col;
    	} else if(pos == 5) {
    		coord[0] = row;
    		coord[1] = col - 1;
    	}
    	return coord;
    }
    
    /** Calculates the priorities for this tile, where a tile's priority is determined by the number of white/black pieces adjacent to it.
     * @param tile_list A list of tiles that represents the board state. */
    public void calcPriorities(ArrayList<Tile> tile_list) {
    	for (int i = 0; i < NUM_ADJ; i++) {
    		if(adj_record[i] != Rluna.INVALID){
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