/* COMP30024 Artificial Intelligence
 * FenceMaster AI
 * Authors: Rosa Luna <rluna> and Ryan Hodgman <hodgmanr>
 */

package rluna.fencemaster;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/** Checks a board state to determine if either player has won the game. */
public class GameState{
/* The class variables */
	
    /** The ArrayList containing all of the board tiles. */
    private ArrayList<Tile> tile_list;
    
    /** The ArrayList containing all of the tile groups. */
    private ArrayList<TileGroup> group_list;
    
    /** The current group being fed into the group list. NOTE: Poor modular practice. */
    private TileGroup group;
    
    /** The dimension of the board being checked. */
    private int dim;
    
    /** A constant representing the minimum number of tiles needed to make a loop. */
    public static final int LOOP_MIN = 6;
    
	/**Flags if white has won via a loop*/
	private boolean white_loop;
	
	/**Flags if black has won via a loop*/
	private boolean black_loop;
	
	/**Flags if white has won via a tripod*/
	private boolean white_tripod;
	
	/**Flags if black has won via a tripod*/
	private boolean black_tripod;
    
/* The getter and setter methods */   
    /** Returns the dimension of the board. */
    public int getDimension() {
    	return dim;
    }
    
    /** Returns the tile list. */
    public ArrayList<Tile> getTileList() {
    	return tile_list;
    }
    
    /** Returns the tile list. */
    public ArrayList<TileGroup> getGroupList() {
    	return group_list;
    }
    
/* The constructor(s) */
    /** Creates a new WinChecker object. 
     * @throws FileNotFoundException */
    public GameState()
    throws FileNotFoundException {
    	// Creates the board state from an inputted .txt file.
    	initTiles();
    	// Assign the black/white priorities for each tile on the board.
    	for(int i = 0; i < tile_list.size(); i++) {
    		tile_list.get(i).calcPriorities(tile_list);
    	}
        // Hunts through the tile list and sections the pieces on the board into groups, where a 'group' refers to a 
        // collection of same-coloured pieces that are all connected to each other via other same-coloured pieces.
        group_list = new ArrayList<TileGroup>();
        assignGroups();
        // Prints out the groups and their members.
        if(GameSimulation.DEBUG) {
            for(int q = 0; q < group_list.size(); q++) {
                for(int i = 0; i < group_list.get(q).group_tiles.size(); i++) {
                    System.out.println("Group ID: " + group_list.get(q).getID() + ", Colour: " + group_list.get(q).getPlayer() + " , Tile_ID: " + group_list.get(q).group_tiles.get(i));
                }
            }
        }
        
     // Checks each group to see if it forms a loop.
        black_loop = false;
        white_loop = false;
        for(int i = 0; i < group_list.size(); i++) {
        	resetVistedFlags();
        	if(group_list.get(i).getPlayer()=='B'){
        		black_loop = loopChecker(group_list.get(i));
        	}
        	if(group_list.get(i).getPlayer()=='W'){
        		white_loop = loopChecker(group_list.get(i));
        	}
        	if(black_loop||white_loop){
        		break;
        	}
        }

        
        // Checks each group to see if it forms a tripod.
        black_tripod = false;
        white_tripod = false;
        for(int i = 0; i < group_list.size(); i++) {
        	if(group_list.get(i).getPlayer()=='B'){
        		black_tripod = tripodChecker(group_list.get(i));
        	}
        	if(group_list.get(i).getPlayer()=='W'){
        		white_tripod = tripodChecker(group_list.get(i));
        	}
        	if(black_tripod||white_tripod){
        		break;
        	}
        }
        
        printResult();
    }
    
    /** Initialises a tile list that represents the current board state. 
     * @throws FileNotFoundException */
    public void initTiles() throws FileNotFoundException{
    	// Scans the data file.
        Scanner file_scanner = new Scanner(System.in);
        String line = file_scanner.nextLine().replaceAll("\\s+", "");
        // Extracts the board dimension from the first line of the data file, then calculates the board properties.
        dim = Integer.parseInt(line);
        int num_lines = 2*dim - 1;
        int num_tiles = 3*dim*dim - 3*dim + 1;
        // Creates a temporary string to store the piece configuration of the board state.
        String tile_pieces = new String("");
        for (int i = 0; i < num_lines; i++) {
            line = file_scanner.nextLine();
            String temp = line.replaceAll("\\s+", "");
            tile_pieces = tile_pieces + temp;
        }
        file_scanner.close();
        // Input error catch - Not enough pieces supplied for the given board dimension.
        if(tile_pieces.length() != num_tiles) {
        	System.out.println("ERROR: Number of tiles supplied does not match the given dimension!");
        	System.exit(0);
        }
        // Prints out the full list of tile pieces in order from [0, 0] to  [dim - 2, dim - 2].
        if(GameSimulation.DEBUG) {
        	System.out.println(tile_pieces);
        }
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
        // Creates and fills a new ArrayList with the required tile objects.
        tile_list = new ArrayList<Tile>(num_tiles);
        for (int i = 0; i < num_tiles; i++) {
        	Tile tile = new Tile(dim, tile_pieces.charAt(i), coord[i][0], coord[i][1]);
        	tile_list.add(i, tile);
        }
    }
    
	/** Iterates through the tile list and creates group objects that store all connected tiles of the same colour. */
	public void assignGroups(){
		// Initializes arrays that store the tile IDs of the black and white pieces in the game state.
		ArrayList<Integer> black = new ArrayList<Integer>();
		ArrayList<Integer> white = new ArrayList<Integer>();
		
		// Iterates through the tile list and assigns the tiles to the appropriate colour list.
		for(int i = 0; i < tile_list.size(); i++) {
			if(tile_list.get(i).getPiece() == 'B') {
				black.add(i);
			}
			if(tile_list.get(i).getPiece() == 'W') {
				white.add(i);
			}
		}
		
		// Calculates the groups present in the current game state.
		calcGroups(black, false);
		calcGroups(white, true);		
	}
	
	/** Hunts through the supplied list of same-coloured pieces to find all connected groups, then initializes group objects
	 * to represent them. 
	 * @param coll A collection of tiles of a particular colour in the current board state. 
	 * @param called True if the calcGroups() method has been called before, false otherwise.*/
	public void calcGroups(ArrayList<Integer> coll, Boolean called){
		// Initializes a queue designed to hold a list of tiles in the same group as the current tile.
		ArrayList<Integer> queue = new ArrayList<Integer>();
		char coll_colour = tile_list.get(coll.get(0)).getPiece();
		// If the method has never been called before, creates an initial group and adds it to the group list.
		if(!called) {
			group = new TileGroup(group_list.size() + 1, coll_colour);
			group_list.add(group);
		} else {
			// The method has been called before, so reset the colour of the last group in the group list.
			int last = group_list.size();
			group_list.get(last - 1).setPlayer(coll_colour);
		}
		// Identifies if a new group needs to be created.
		Boolean new_group = false;
		
		// Iterates through the collection of same-coloured tiles looking for groups.
		for(int i = 0; i < coll.size(); i++) {
			// Checks that the tile being examined is not yet in a group, hasn't yet been visited and that the queue is empty.
			if(tile_list.get(coll.get(i)).getGroup() == 0 && !tile_list.get(coll.get(i)).getVisited() && queue.isEmpty()) {
				// If this is the case, then a new tile not related to any previously created group needs to be explored.
				exploreTile(coll.get(i), group, queue);
				new_group = true;
				// Check to see if there are any tiles to be dealt with in the queue.
				while(!queue.isEmpty()){
					// A group is yet to be fully explored, so continue exploring the new tiles in the queue.
					exploreTile(queue.get(0), group, queue);
					// Finished exploring the current tile, so eject it from the queue.
					queue.remove(0);
				}
			}
			// The old group has been fully explored, so create a new group and add it to the group list.
			if(new_group) {
				group = new TileGroup(group_list.size() + 1, coll_colour);
				group_list.add(group);
				new_group = false;
			}
		}
	}
	
	/** Explores a tile so as to identify what group it should be part of and what same-colour tiles are adjacent to it.
	 * @param tile_ID The ID of the tile to be explored.
	 * @param current_group The group currently being expanded for this tile to be added to.
	 * @param queue The list of tiles associated with the current group that are yet to be explored. */
	public void exploreTile(int tile_ID, TileGroup current_group, ArrayList<Integer> queue){
		char coll_colour = tile_list.get(tile_ID).getPiece();
		// Adds the tile to the current group and alters the tile's attributes to reflect this.
		current_group.group_tiles.add(tile_ID);
		tile_list.get(tile_ID).setGroup(current_group.getID());
		tile_list.get(tile_ID).setVisited(true);
		// Adds the adjacent tiles of the same colour to the queue.
		for(int q = 0; q < Tile.NUM_ADJ; q++) {
			// Checks that the adjacent tile exists (is not off the board edge).
			if(tile_list.get(tile_ID).getAdjElement(q) != -1) {
				// Checks that the adjacent tile being examined is of the same colour, hasn't been visited yet and isn't already in the queue.
				if(tile_list.get(tile_list.get(tile_ID).getAdjElement(q)).getPiece() == coll_colour && !tile_list.get(tile_list.get(tile_ID).getAdjElement(q)).getVisited() && !queue.contains(tile_list.get(tile_ID).getAdjElement(q))) {
					queue.add(tile_list.get(tile_ID).getAdjElement(q));
				}
			}
		}
	}
	
	/** Takes as input a group of board pieces of a single colour and then checks whether those pieces form a tripod.
	 * @param group The group of pieces being checked for a tripod win condition. 
	 * @return Returns true if the supplied group forms a tripod. */
	public Boolean tripodChecker(TileGroup group){
		// A group can only form a tripod if it has at least as many members as it takes to stretch across a side plus two.
		if(group.group_tiles.size() < dim + 2) {
			return false;
		}
		// Creates an array to store the ID of all of the tiles in the group that are on the edge of the board, but are not corner pieces.
		ArrayList<Integer> side_tiles = new ArrayList<Integer>();
		int edge_count = 0;
		for(int i = 0; i < group.group_tiles.size(); i++) {
			for(int q = 0; q < Tile.NUM_ADJ; q++) {
				if(tile_list.get(group.group_tiles.get(i)).getAdjElement(q) == -1) {
					edge_count++;
				}
			}
			// If a tile is an edge piece but not a corner piece then two of its adjacency entries will equal -1.
			if(edge_count == 2) {
				side_tiles.add(group.group_tiles.get(i));
			}
			edge_count = 0;
		}
		// A group can only form a tripod if it consists of at least three edge pieces.
		if(side_tiles.size() < 3) {
			return false;
		}
		// If a group contacts three or more sides, then it has formed a tripod.
		if(numSides(side_tiles) >= 3) {
			return true;
		} else {
			return false;
		}
	}
	
	/** Goes through an ArrayList of edge tiles to return the number of board sides that the group contacts.
	 * @param edge_tiles ArrayList of all of the edge tiles in a group. 
	 * @return Returns the number of board sides that the group contacts. */
	public int numSides(ArrayList<Integer> edge_tiles){
		int side0 = 0;
		int side1 = 0;
		int side2 = 0;
		int side3 = 0;
		int side4 = 0;
		int side5 = 0;
		
		// Identifies which side of the board each edge piece contacts.
		for(int j = 0; j < edge_tiles.size(); j++){
			if(tile_list.get(edge_tiles.get(j)).getAdjElement(0) == -1 && tile_list.get(edge_tiles.get(j)).getAdjElement(5) == -1){
				side0 = 1;
			} else if(tile_list.get(edge_tiles.get(j)).getAdjElement(0) == -1 && tile_list.get(edge_tiles.get(j)).getAdjElement(1) == -1){
				side1 = 1;
			} else if(tile_list.get(edge_tiles.get(j)).getAdjElement(1) == -1 && tile_list.get(edge_tiles.get(j)).getAdjElement(2) == -1){
				side2 = 1;
			} else if(tile_list.get(edge_tiles.get(j)).getAdjElement(2) == -1 && tile_list.get(edge_tiles.get(j)).getAdjElement(3) == -1){
				side3 = 1;
			} else if(tile_list.get(edge_tiles.get(j)).getAdjElement(3) == -1 && tile_list.get(edge_tiles.get(j)).getAdjElement(4) == -1){
				side4 = 1;
			} else if(tile_list.get(edge_tiles.get(j)).getAdjElement(4) == -1 && tile_list.get(edge_tiles.get(j)).getAdjElement(5) == -1){
				side5 = 1;
			}
		}
		
		// Counts the number of board edges contacted by the group.
		int num_sides = side0 + side1 + side2 + side3 + side4 + side5;
		
		return num_sides;
	}

	/** Checks whether the board is full of pieces and thus whether the game is a draw.
	 * @return Returns true if the game is a draw. */
	public boolean drawState(){
		int i = 0;
		while(i < tile_list.size()){
			if(tile_list.get(i).getPiece() == '-'){
				return false;
			}
			i++;
		}
		return true;
	}
	
	/** Takes as input a group of board pieces of a single colour and then checks whether those pieces form a loop.
	 * @param group The group of pieces being checked for a loop win condition. 
	 * @return Returns true if the supplied group forms a loop. */
	public boolean loopChecker(TileGroup group){
		if(group.group_tiles.size()<LOOP_MIN) {
			return false;
		}
		ArrayList<Integer> queue_first = new ArrayList<Integer>();	//Stores the immediately adjacent tiles that don't contain the same colour piece according to priority
		ArrayList<Integer> queue_track = new ArrayList<Integer>();	//Stores the next adjacent tiles that don't contain the same colour piece in the current path being taken according to priority
		queue_first = buildQueueFirst(group);
		
		Integer current_id;
		int edge_side;
	    
			while(!queue_first.isEmpty()){
				current_id = queue_first.get(0);
				tile_list.get(current_id).setVisited(true);
				
				while(current_id != -1){
					edge_side = 0;
					for(int i = 0; i < Tile.NUM_ADJ; i++){
						if(tile_list.get(current_id).getAdjElement(i) != -1 && tile_list.get(tile_list.get(current_id).getAdjElement(i)).getPiece()!=group.getPlayer()){
							if(!tile_list.get(tile_list.get(current_id).getAdjElement(i)).getVisited()){
								// Add to queue track.
								
								if(queue_track.isEmpty()){
									queue_track.add(tile_list.get(current_id).getAdjElement(i));
								}else{
									int k=0;
									if(group.getPlayer()=='B'){
										// Look at black priority.
										while(k < queue_track.size() && tile_list.get(current_id).getAdjElement(i) != queue_track.get(k) && tile_list.get(tile_list.get(current_id).getAdjElement(i)).getBlackPriority() <= tile_list.get(queue_track.get(k)).getBlackPriority()){
											k++;
										}	
									} else {
										// Look at white priority.
										while(k < queue_track.size() && tile_list.get(current_id).getAdjElement(i) != queue_track.get(k) && tile_list.get(tile_list.get(current_id).getAdjElement(i)).getWhitePriority() <= tile_list.get(queue_track.get(k)).getWhitePriority()){
											k++;
										}
									}
									
									
									if(k == queue_track.size()){
										queue_track.add(tile_list.get(current_id).getAdjElement(i));
									}else if(tile_list.get(current_id).getAdjElement(i) != queue_track.get(k)){
										queue_track.add(k,tile_list.get(current_id).getAdjElement(i)); // add to kth place
									}
								}
																
							}
						} else if(tile_list.get(current_id).getAdjElement(i)==-1){
							edge_side++;
						}
					}
					// Outside for loop.
					tile_list.get(current_id).setVisited(true);
					queue_track.remove(current_id);
					
					if(edge_side >= 2){
						// Have hit an edge tile and hence not in a loop.
						queue_track.clear();
					} else if(queue_track.isEmpty()){
						// Inside a loop.
						return true;
					} 
					
					queue_first.remove(current_id);
					
					if(queue_track.isEmpty()){
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
	public ArrayList<Integer> buildQueueFirst(TileGroup group){
		ArrayList<Integer> queue_first = new ArrayList<Integer>();
		int current_id;
		int k;
		
		//set queue_first
		for(int i=0; i<group.group_tiles.size();i++){
			for(int j = 0; j < Tile.NUM_ADJ; j++){
					//look at tiles that are != group.getPlayer()
					current_id = tile_list.get(group.group_tiles.get(i)).getAdjElement(j);
					if(current_id != -1 && tile_list.get(current_id).getPiece() != group.getPlayer()){
						
						if(queue_first.isEmpty()){
							queue_first.add(current_id);
						}else{
							k=0;
							if(group.getPlayer()=='B'){
								// look at black priority
								while(k < queue_first.size() && current_id != queue_first.get(k) && tile_list.get(current_id).getBlackPriority() <= tile_list.get(queue_first.get(k)).getBlackPriority()){
									k++;
								}
							} else {
								// look at white priority
								while(k < queue_first.size() && current_id != queue_first.get(k) && tile_list.get(current_id).getWhitePriority() <= tile_list.get(queue_first.get(k)).getWhitePriority()){
									k++;
								}	
							}
							
							if(k == queue_first.size()){
								queue_first.add(current_id);
							}else if(current_id != queue_first.get(k)){
								queue_first.add(k,current_id); // add to kth place in queue_first
							}
						}
					}
			}

		}
		return queue_first;
	}
	
	/** Ensures that Visited flag of all the board's tiles is set to not visited (false) */
	public void resetVistedFlags(){
		for(int i=0; i<tile_list.size();i++){
			if(tile_list.get(i).getVisited()){
				tile_list.get(i).setVisited(false);
			}
		}
	}
	
	/** Determines the result of the input game board and prints it out with format: [Wining Player or None or Draw] <\n> [How they won or Nil] */
	public void printResult(){
		//uses the flags white_loop, white_tripod, black_loop and black_tripos to determine the result
		
        if(white_loop && white_tripod){
        	System.out.println("White");
        	System.out.println("Both");
        } else if(black_loop && black_tripod){
        	System.out.println("Black");
        	System.out.println("Both");
        } else if(white_loop){
        	System.out.println("White");
        	System.out.println("Loop");
        } else if(black_loop){
        	System.out.println("Black");
        	System.out.println("Loop");
        } else if(white_tripod){
        	System.out.println("White");
        	System.out.println("Tripod");
        } else if(black_tripod){
        	System.out.println("Black");
        	System.out.println("Tripod");
        } else if(drawState()) {
        	// After checking for both tripod and loop win conditions,determines whether the game is a draw.
        	System.out.println("Draw");
        	System.out.println("Nil");
        } else {
        	System.out.println("None");
        	System.out.println("Nil");
        }
	}
	
}


  
