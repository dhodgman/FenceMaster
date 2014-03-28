/* COMP30024 Artificial Intelligence
 * FenceMaster AI
 * Author: Ryan Hodgman <hodgmanr>
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/** Checks a board state to determine if either player has won the game. */
public class WinChecker {
/* The class variables */
	
    /** The ArrayList containing all of the board tiles. */
    private ArrayList<Tile> tile_list;
    
    /** The dimension of the board being checked. */
    private int dim;
    
/* The getter and setter methods */   
    /** Returns the dimension of the board. */
    public int getDimension() {
    	return dim;
    }
    
    /** Returns the tile list. */
    public ArrayList<Tile> getList() {
    	return tile_list;
    }
    
/* The constructor(s) */
    /** Creates a new WinChecker object. */
    public WinChecker()
    throws FileNotFoundException {
    	// Scans the data file.
        Scanner file_scanner = new Scanner(new File(GameSimulation.DATA_PATH + "/test_input.txt"));
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
}
  
