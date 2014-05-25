/* COMP30024 Artificial Intelligence
 * FenceMaster AI
 * Authors: Rosa Luna <rluna> and Ryan Hodgman <hodgmanr>
 */

package rluna.fencemaster;

import java.util.ArrayList;

/** Checks a board state to determine if either player has won the game. */
public class GameState {
/* The class variables */
    /** A constant representing the minimum number of tiles needed to make a loop. */
    public static final int LOOP_MIN = 6;
    
    /** The ArrayList containing all of the board tiles. */
    private ArrayList<Tile> tile_list;
    
    /** The ArrayList containing all of the tile groups. */
    private ArrayList<TileGroup> group_list;
    
    /** The number of tiles on the board. */
    private int num_tiles;
    
    /** The dimension of the board. */
    private static int dim;
    
/* The getter and setter methods */       
    /** Returns the tile list. */
    public ArrayList<Tile> getTileList() {
    	return tile_list;
    }
    
    /** Returns the tile specified by the ID value. [added to be used by AI]*/
    public Tile getTile(int ID) {
    	return tile_list.get(ID);
    }
    
    /** Returns the group list. */
    public ArrayList<TileGroup> getGroupList() {
    	return group_list;
    }
    
    /** Returns the number of board tiles. */
    public int getNumTiles() {
    	return num_tiles;
    }
    
    /** Returns the dimension of the board. */
    public int getDim() {
        return dim;
    }
    
/* The constructor(s) */
    /** Creates a new GameState object. 
     * @param dim The board dimension. */
    public GameState(int dim) {
    	GameState.dim = dim;
    	// Initialises the tiles representing the board state.
    	initTiles();
    	for(int i = 0; i < tile_list.size(); i++) {
    		// Assign the adjacency record for each tile on the board.
    		tile_list.get(i).setAdj(calcAdj(i));
    		// Assign the black/white priorities for each tile on the board.
    		tile_list.get(i).calcPriorities(tile_list);
    	}
    	group_list = new ArrayList<TileGroup>();
    }
    
/* The class methods */
    /** Initialises a tile list that represents the current board state. */
    public void initTiles() {
        num_tiles = 3*dim*dim - 3*dim + 1;
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
        // Creates and fills a new ArrayList with the required tile objects.
        tile_list = new ArrayList<Tile>(num_tiles);
        for (int i = 0; i < num_tiles; i++) {
        	Tile tile = new Tile(Rluna.EMPTY, coord[i][0], coord[i][1]);
        	tile_list.add(i, tile);
        }
    }
	
	/** Takes as input a group of board pieces of a single colour and then checks whether those pieces form a tripod.
	 * @param group The group of pieces being checked for a tripod win condition. 
	 * @param dim The board dimension.
	 * @return Returns true if the supplied group forms a tripod. */
	public Boolean tripodChecker(TileGroup group, int dim) {
		// A group can only form a tripod if it has at least as many members as it takes to stretch across a side plus two.
		if(group.getTiles().size() < dim + 2) {
			return false;
		}
		// Creates an array to store the ID of all of the tiles in the group that are on the edge of the board, but are not corner pieces.
		ArrayList<Integer> side_tiles = new ArrayList<Integer>();
		int edge_count = 0;
		for(int i = 0; i < group.getTiles().size(); i++) {
			for(int q = 0; q < Tile.NUM_ADJ; q++) {
				if(tile_list.get(group.getTiles().get(i)).getAdjElement(q) == -1) {
					edge_count++;
				}
			}
			// If a tile is an edge piece but not a corner piece then two of its adjacency entries will equal -1.
			if(edge_count == 2) {
				side_tiles.add(group.getTiles().get(i));
			}
			edge_count = 0;
		}
		// A group can only form a tripod if it consists of at least three edge pieces.
		if(side_tiles.size() >= 3) {
			return true;
		} else {
			return false;
		}
	}
	
	/** Goes through an ArrayList of group tiles to return the number of board sides that the group contacts.
	 * @param edge_tiles ArrayList of all of the edge tiles in a group. 
	 * @return Returns the number of board sides that the group contacts. */
	public int numSides(ArrayList<Integer> edge_tiles) {
		int side0 = 0;
		int side1 = 0;
		int side2 = 0;
		int side3 = 0;
		int side4 = 0;
		int side5 = 0;
		
		// Identifies which side of the board each edge piece contacts.
		for(int j = 0; j < edge_tiles.size(); j++) {
			if(tile_list.get(edge_tiles.get(j)).getAdjElement(0) == -1 && tile_list.get(edge_tiles.get(j)).getAdjElement(5) == -1) {
				side0 = 1;
			} else if(tile_list.get(edge_tiles.get(j)).getAdjElement(0) == -1 && tile_list.get(edge_tiles.get(j)).getAdjElement(1) == -1) {
				side1 = 1;
			} else if(tile_list.get(edge_tiles.get(j)).getAdjElement(1) == -1 && tile_list.get(edge_tiles.get(j)).getAdjElement(2) == -1) {
				side2 = 1;
			} else if(tile_list.get(edge_tiles.get(j)).getAdjElement(2) == -1 && tile_list.get(edge_tiles.get(j)).getAdjElement(3) == -1) {
				side3 = 1;
			} else if(tile_list.get(edge_tiles.get(j)).getAdjElement(3) == -1 && tile_list.get(edge_tiles.get(j)).getAdjElement(4) == -1) {
				side4 = 1;
			} else if(tile_list.get(edge_tiles.get(j)).getAdjElement(4) == -1 && tile_list.get(edge_tiles.get(j)).getAdjElement(5) == -1) {
				side5 = 1;
			}
		}	
		// Counts the number of board edges contacted by the group.
		int num_sides = side0 + side1 + side2 + side3 + side4 + side5;
		
		return num_sides;
	}
	
	/** Takes as input a group of board pieces of a single colour and then checks whether those pieces form a loop.
	 * @param group The group of pieces being checked for a loop win condition. 
	 * @return Returns true if the supplied group forms a loop. */
	public boolean loopChecker(TileGroup group) {
		if(group.getTiles().size()<LOOP_MIN) {
			return false;
		}
		// Stores the immediately adjacent tiles that don't contain the same colour piece according to priority.
		ArrayList<Integer> queue_first = new ArrayList<Integer>();
		// Stores the next adjacent tiles that don't contain the same colour piece in the current path being taken according to priority.
		ArrayList<Integer> queue_track = new ArrayList<Integer>();
		queue_first = buildQueueFirst(group);
		
		Integer current_id;
		int edge_side;
	    
			while(!queue_first.isEmpty()) {
				current_id = queue_first.get(0);
				tile_list.get(current_id).setVisited(true);
				while(current_id != -1) {
					edge_side = 0;
					for(int i = 0; i < Tile.NUM_ADJ; i++) {
						if(tile_list.get(current_id).getAdjElement(i) != -1 && tile_list.get(tile_list.get(current_id).getAdjElement(i)).getPiece()!=group.getPlayer()) {
							if(!tile_list.get(tile_list.get(current_id).getAdjElement(i)).getVisited()) {
								// Add to queue track.
								if(queue_track.isEmpty()) {
									queue_track.add(tile_list.get(current_id).getAdjElement(i));
								} else {
									int k = 0;
									if(group.getPlayer() == Rluna.BLACK) {
										// Look at black priority.
										while(k < queue_track.size() && tile_list.get(current_id).getAdjElement(i) != queue_track.get(k) && tile_list.get(tile_list.get(current_id).getAdjElement(i)).getBlackPriority() <= tile_list.get(queue_track.get(k)).getBlackPriority()) {
											k++;
										}	
									} else {
										// Look at white priority.
										while(k < queue_track.size() && tile_list.get(current_id).getAdjElement(i) != queue_track.get(k) && tile_list.get(tile_list.get(current_id).getAdjElement(i)).getWhitePriority() <= tile_list.get(queue_track.get(k)).getWhitePriority()) {
											k++;
										}
									}
									if(k == queue_track.size()) {
										queue_track.add(tile_list.get(current_id).getAdjElement(i));
									} else if(tile_list.get(current_id).getAdjElement(i) != queue_track.get(k)) {
										queue_track.add(k,tile_list.get(current_id).getAdjElement(i)); // add to kth place
									}
								}							
							}
						} else if(tile_list.get(current_id).getAdjElement(i) == -1) {
							edge_side++;
						}
					}
					// Outside for loop.
					tile_list.get(current_id).setVisited(true);
					queue_track.remove(current_id);
					// Have hit an edge tile and hence not in a loop.
					if(edge_side >= 2) {
						queue_track.clear();
					} else if(queue_track.isEmpty()) {
						// Inside a loop.
						return true;
					} 
					queue_first.remove(current_id);
					if(queue_track.isEmpty()) {
						current_id = -1;
					} else {
						current_id = queue_track.get(0);
					}
				}
			}
		// All immediately adjacent tiles have been visited and they are not on the inside of the loop.
		return false;
	}
	
	/** Builds the ArrayList that stores the immediately adjacent tiles that don't contain the player's piece 
	 * with priority determined by how many of the player's pieces surround them.
	 * @param group The group of tiles that are being checked.
	 * @return Returns a filled high priority ArrayList. */
	public ArrayList<Integer> buildQueueFirst(TileGroup group) {
		ArrayList<Integer> queue_first = new ArrayList<Integer>();
		int current_id;
		int k;
		// Set queue_first.
		for(int i = 0; i < group.getTiles().size(); i++) {
			for(int j = 0; j < Tile.NUM_ADJ; j++) {
				// Look at tiles that are != group.getPlayer().
				current_id = tile_list.get(group.getTiles().get(i)).getAdjElement(j);
				if(current_id != -1 && tile_list.get(current_id).getPiece() != group.getPlayer()) {
					if(queue_first.isEmpty()) {
						queue_first.add(current_id);
					} else {
						k = 0;
						if(group.getPlayer() == Rluna.BLACK) {
							// Look at black priority.
							while(k < queue_first.size() && current_id != queue_first.get(k) && tile_list.get(current_id).getBlackPriority() <= tile_list.get(queue_first.get(k)).getBlackPriority()){
								k++;
							}
						} else {
							// Look at white priority.
							while(k < queue_first.size() && current_id != queue_first.get(k) && tile_list.get(current_id).getWhitePriority() <= tile_list.get(queue_first.get(k)).getWhitePriority()){
								k++;
							}	
						}
						if(k == queue_first.size()) {
							queue_first.add(current_id);
						} else if(current_id != queue_first.get(k)) {
							queue_first.add(k, current_id); // add to kth place in queue_first
						}
					}
				}
			}
		}
		return queue_first;
	}
	
	/** Ensures that Visited flag of all the board's tiles is set to not visited (false). */
	public void resetVistedFlags() {
		for(int i = 0; i < tile_list.size(); i++) {
			if(tile_list.get(i).getVisited()) {
				tile_list.get(i).setVisited(false);
			}
		}
	}
	
	/** Determines if the current board state has a winner.
	 * @return Returns the colour of the winner, or INVALID if there is none. */
	public int calcResult() { 
        // Checks each group to see if that group forms a winning configuration and for which player.
        for(int i = 0; i < group_list.size(); i++) {
        	// Resets the visited flags to make sure that the loopChecker() method functions correctly.
        	resetVistedFlags();
        	if(group_list.get(i).getPlayer() == Rluna.BLACK) {
        		if(loopChecker(group_list.get(i)) || tripodChecker(group_list.get(i), dim)) {
        			return Rluna.BLACK;
        		}
        	} else if(group_list.get(i).getPlayer() == Rluna.WHITE) {
        		if(loopChecker(group_list.get(i)) || tripodChecker(group_list.get(i), dim)) {
        			return Rluna.WHITE;
        		}
        	}
        }
        // Checked each group and didn't find a winning configuration.
        return Rluna.INVALID;
	}
	
	/** Calculates the tile ID given a set of row/col coordinates.
	 * @param row The x-coordinate.
	 * @param col The y-coordinate.
	 * @return -1 if the coordinates are invalid and the tile ID if the coordinates are valid. */
	public static int calcTileID(int row, int col) {
		// Check to see whether the tile is in the top half of the board.
		if(0 <= row && row < dim) {
			// Guard to check if the location is outside of the upper column boundaries.
			if(col < 0 || col > (dim - 1 + row)) {
				return Rluna.INVALID;
			} else {
				// Calculates and returns the tile ID for the top half.
				return (int)(0.5*(row*row + (2*dim - 1)*row) + col);
			}
		}
		// Else the location is in the bottom half of the board.
		else if(dim <= row && row <= (2*dim - 2)) {
			// Guard to check if the location is outside of the lower column boundaries.
			if(col < (row - dim + 1) || col > (2*dim - 2)) {
	      		return Rluna.INVALID;
			} else {
				// Calculates and returns the tile ID for the bottom half.
				return (int)(0.5*((6*row + 4)*dim - 2*dim*dim - row*row - 5*row - 2) + col);
			}
		}
		// If the location is not in the top or bottom halves of the board it is outside of the row boundaries.
		return Rluna.INVALID;
	}
	
	/** Creates and fills an adjacency record for a specific tile. Assumes that the number of adjacent tiles = 6. 
     * @param tile_ID The ID of the tile being evaluated. 
     * @return Returns the calculated adjacency record. */
    public int[] calcAdj(int tile_ID) {
    	int[] adj_record = new int[Tile.NUM_ADJ];
    	// Iterates through the surrounding tiles.
    	for(int i = 0; i < Tile.NUM_ADJ; i++) {
    		int[] coord = new int[2];
    		// Calculates the coordinates of the tile at adjacency position 'i'. 
    		coord = tile_list.get(tile_ID).adjCoord(i);
    		int row = coord[0];
    		int col = coord[1];
    		// Sets the record entry for adjacency position 'i' to the ID of the tile in that position, or INVALID if out of bounds.
    		adj_record[i] = calcTileID(row, col);
    	}
    	// Prints out the adjacency array for each tile on the board.
    	if(Rluna.DEBUG) {
    		for(int q = 0; q < Tile.NUM_ADJ; q++) {
    			System.out.println(adj_record[q]);
    		}
    		System.out.println("-----");
    	}
    	return adj_record;
    } 
    
	/** Upates the game state given a tile ID that represents a new move. 
	 * @param tile_ID The ID of the new piece being placed on the board. 
	 * @param player The owner of the piece being placed. */
	public void updateState(int tile_ID, int player) {
		// Updates the colour of pice occupying the specified tile.
		tile_list.get(tile_ID).setPiece(player);
		// Update the group list.
		if(group_list.isEmpty()) {
			// This is the first piece to be placed, so create a new group.
			TileGroup group = new TileGroup(group_list.size(), player);
			tile_list.get(tile_ID).setGroup(group.getID());
			group.getTiles().add(tile_ID);
			group_list.add(group);
		} else {
			// Iterate through adjacent tiles and determine if any contain pieces of the same colour.
			ArrayList<Integer> adj_groups = new ArrayList<Integer>();
			for(int i = 0; i < Tile.NUM_ADJ; i++) {
				// Guard to check that the adjacent tile exists.
				if(tile_list.get(tile_ID).getAdjElement(i) != Rluna.INVALID) {
					Tile temp = tile_list.get(tile_list.get(tile_ID).getAdjElement(i));
					// Adds the group ID of the adjacent tile to the adj_groups list.
					if(temp.getPiece() == player) {
						adj_groups.add(temp.getGroup());
					}
				}
			}
			// Determine how many adjacent groups of the same colour were found.
			if(adj_groups.size() == 0) {
				// Zero groups found, so create a new group.
				TileGroup group = new TileGroup(group_list.size(), player);
				tile_list.get(tile_ID).setGroup(group.getID());
				group.getTiles().add(tile_ID);
				group_list.add(group);
			} else if(adj_groups.size() == 1) {
				// One group found, so add this tile to that group.
				tile_list.get(tile_ID).setGroup(adj_groups.get(0));
				group_list.get(adj_groups.get(0)).getTiles().add(tile_ID);
			} else {
				// Two or more groups found, so merge the groups.
				ArrayList<Integer> temp_tiles = new ArrayList<Integer>();
				// Add the new move to the temporary tile ID list.
				temp_tiles.add(tile_ID);
				for(int i = 1; i < adj_groups.size(); i++) {
					for(int q = 0; q < group_list.get(adj_groups.get(i)).getTiles().size(); q++) {
						// Add all of the newly connected tiles from the 2nd (and possibly third) group(s) to a temporary ID list.
						temp_tiles.add(group_list.get(adj_groups.get(i)).getTiles().get(q));
					}
					// Remove the later groups from the group_list, as they are about to be merged.
					group_list.remove((adj_groups.get(i)));
				}
				for(int i = 0; i < temp_tiles.size(); i++) {
					// Add all of the newly connected tiles to the first discoverd adjacent group.
					tile_list.get(temp_tiles.get(i)).setGroup(adj_groups.get(0));
					group_list.get(adj_groups.get(0)).getTiles().add(temp_tiles.get(i));
				}
			}
		}
	}
}
