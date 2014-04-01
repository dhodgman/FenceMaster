/* COMP30024 Artificial Intelligence
 * FenceMaster AI
 * Author: Rosa Luna <rluna>
 */

import java.util.ArrayList;

/** Used to go through board to check if a loop has been formed */
public class LoopChecker{
	/* The class variables */
	
//	/** The ArrayList containing all of the board tiles. */
//	private ArrayList<Tile> copy_tile_list;
	
	/** This array contains the index values of the tiles' pieces in tile_list that are the same colour */
	private ArrayList<Integer> samepiece_list;
	
	/** Record of the colour of the player being checked for a loop win **/
	private char player;
	
    /** The board's dimension. */
    private int dim;
    
    /** Record of the number of tiles on the board */
    private int num_tiles;
	
	/* The constructor(s) */
    /** Creates a new LoopChecker object.
     * @param tile_list The ArrayList containing all of the board tiles.
     * @param player The player that is being checked for having a loop. */
    public LoopChecker(char player) {
    	setPlayer(player);
    	buildSameList(tile_list, same_list);
    }

//	public ArrayList<Tile> getCopy_tile_list() {
//		return copy_tile_list;
//	}

//	public void setCopyTileList(ArrayList<Tile> tile_list) {
//		this.copy_tile_list = tile_list;
//	}
	
//	public void setSameList(ArrayList<Tile> tile_list) {
		//fill this in still
//	}
	
    /** Returns the array that has the locations of all the same colour pieces */
	public ArrayList<Integer> getSameList() {
		return samepiece_list;
	}

	/** Returns the colour of the player being checked */
	public char getPlayer() {
		return player;
	}

	/** Sets the colour of the player being checked */
	public void setPlayer(char player) {
		this.player = player;
	}
	
	/** Returns the number of tiles on the game board */
	public int getNumTiles() {
		return num_tiles;
	}

	/** Sets the number of tiles on the game board */
	public void setNumTiles(int num_tiles) {
		this.num_tiles = num_tiles;
	}
	
	/** Returns the dimension of the board */
	public int getDim() {
		return dim;
	}

	/** Sets the dimension of the board */
	public void setDim(int dim) {
		this.dim = dim;
	}
	
	/* The class methods */
	/** Returns a flag that indicates if the tile's piece colour matches the player's piece colour
	 * @param piece The colour of a tile's piece
	 */
	public boolean containsSamePiece(char piece) {
		if(piece == getPlayer()){
			return true;
		} 
		return false;
	}
	
	/** Builds an array that stores the location of the tiles containing the same colour piece as the
	 * player's piece by storing the index value of tile_list.
	 * @param tile_list The ArrayList that contains the information of all the tiles on the board.
	 * @param same_list The int array that is used to store the location of tiles with matching colour pieces. */
	public void buildSameList(ArrayList<Tile> tile_list, ArrayList<Integer> same_list){
		for(int i=0; i < getNumTiles();i++) {
			if(containsSamePiece(tile_list.get(i).getPiece())) {
				same_list.add(i);
			}
		}
	}
	

//still needs work on	
	public int assignGroup(ArrayList<Tile> tile_list, ArrayList<Integer> same_list) {
		int num_groups = 1;
		int current_tile_id;
		int next_tile_id;
		for(int i=0;i<same_list.size();i++) {
			current_tile_id = same_list.get(i);
			
			for(int j =0; j < Tile.NUM_ADJ; j++) {
				//wrong! need to go through adj matrix
				next_tile_id = tile_list.get(current_tile_id).getAdjElement(j);
				if(containsSamePiece(tile_list.get(next_tile_id).getPiece())) {
					// add it to the end of the queue 
				}
			}
			
		}
		return num_groups;
	}
}