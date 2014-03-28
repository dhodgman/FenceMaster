/* COMP30024 Artificial Intelligence
 * FenceMaster AI
 * Author: Ryan Hodgman <hodgmanr>
 */

/** A representation of the board tiles. */
public class Tile{
/* The class variables */
    /** Record of the piece occupying this tile ("B", "W" or "-"). */
    private char piece;
    
    /** The tile's x-coordinate. */
    private int x_coord;
    
    /** The tile's y-coordinate. */
    private int y_coord;
    
    /** The board's dimension. */
    private int dim;
    
    /** An adjacency record that contains the coordinates of the adjacent tiles ([-1, -1] = board edge). */
    private int[][] adj_record;
    
    /** A constant representing the number of adjacent tiles. */
    public static final int NUM_ADJ = 6;

    /** A visited flag used to indicate if a search has visited this tile. */
    private boolean visited;
    
    /** A group identifier to indicate which group this tile is part of. */
    private int group;

/* The constructor(s) */
    /** Creates a new tile object.
     * @param dim The dimension of the hexagonal board.
     * @param piece The piece (if any) occupying this tile.
     * @param x The tile's x board location.
     * @param y The tile's y board location. */
    public Tile(int dim, char piece, int x, int y) {
    	this.dim = dim;
    	setPiece(piece);
    	setX(x);
    	setY(y);
    	int[][] adj_record = new int[NUM_ADJ][2];
    	adj_record[0][0] = 2;
//    	adj_record = calcAdj(adj_record);
    	setVisited(false);
    	setGroup(0);
    }

/* The getter and setter methods */
    /** Returns the piece (if any) occupying the tile. */
    public char getPiece() {
        return piece;
    }
    
    /** Sets the piece occupying the tile. */
    public void setPiece(char new_piece) {
        this.piece = new_piece;
    }
    
    /** Returns the x-coordinate of the tile. */
    public int getX() {
        return x_coord;
    }
    
    /** Sets the x-coordinate of the tile. */
    public void setX(int x) {
    	this.x_coord = x;
    }
    
    /** Returns the y-coordinate of the tile. */
    public int getY() {
        return y_coord;
    }
    
    /** Sets the y-coordinate of the tile. */
    public void setY(int y) {
    	this.y_coord = y;
    }
    
    /** Returns the adjacency record. */
    public int[][] getAdj() {
        return adj_record;
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

/* The class methods */
    /** Creates and fills the adjacency record.
     * @param num_adj A two-dimensional adjacency record for the tile (assume number of adjacent tile = 6). */
    public int[][] calcAdj(int[][] num_adj) {
    	// Iterates through the surrounding tiles.
    	for(int i = 0; i < NUM_ADJ; i++) {
        	// Checks to see if the tile is on the first half of the board.
        	if(0 <= getX() && getX() < dim - 1) {
        		// Checks to see if the tile is within the y-coordinate bounds of the board.
        		if(getY() < 0 || getY() > dim - 1 + getX()) {
            		num_adj[i][0] = -1;
            		num_adj[i][1] = -1;
        		}
        	} 
        	// Checks to see if the tile is on the second half of the board.
        	else if(dim - 1 <= getX() && getX() < 2*dim - 2) {
                // Checks to see if the tile is within the y-coordinate bounds of the board.
                if(getY() < getX() - dim + 1 || getY() > 2*dim -2) {
              		num_adj[i][0] = -1;
                    num_adj[i][1] = -1;
                }
        	} 
        	// Tile not in x-coordinate bounds of the board.
        	else {
        		num_adj[i][0] = -1;
        		num_adj[i][1] = -1;
        	}
        	// Assigns the coordinates of the adjacent tiles.
        	if(i == 0) {
        		num_adj[i][0] = getX() - 1;
        		num_adj[i][1] = getY() - 1;
        	}
        	if(i == 1) {
        		num_adj[i][0] = getX() - 1;
        		num_adj[i][1] = getY();
        	}
        	if(i == 2) {
        		num_adj[i][0] = getX();
        		num_adj[i][1] = getY() + 1;
        	}
        	if(i == 3) {
        		num_adj[i][0] = getX() + 1;
        		num_adj[i][1] = getY() + 1;
        	}
        	if(i == 4) {
        		num_adj[i][0] = getX() + 1;
        		num_adj[i][1] = getY();
        	}
        	if(i == 5) {
        		num_adj[i][0] = getX();
        		num_adj[i][1] = getY() - 1;
        	}
    	}
    	
    	return num_adj;
    }    
}