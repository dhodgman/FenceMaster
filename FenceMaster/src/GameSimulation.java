/* COMP30024 Artificial Intelligence
 * FenceMaster AI
 * Author: Ryan Hodgman <hodgmanr>
 */

import java.io.FileNotFoundException;

/** Main class for the Role-Playing Game engine.
 * Handles initialization, input and rendering. */
public class GameSimulation{
	/** A variable used to activate or deactivate debugging mode. */
    public static boolean DEBUG = false;
    
    /** Location of the "data" directory. */
    public static final String DATA_PATH = "data";
    
    /** The checker. */
    private static GameState check;
    
    /** Start-up method. Creates the game and runs it.
     * @param args Command-line arguments (ignored). */
    public static void main(String[] args) {
        try {
			check = new GameState();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Couldn't find the data file!");
			System.exit(0);
		}
        if(DEBUG) {
        	for(int t = 0; t < check.getTileList().size(); t++) {
                System.out.println(check.getTileList().get(t).getPiece() + ", [" + check.getTileList().get(t).getX() + ","+ check.getTileList().get(t).getY() + "]");
        	}
        	//System.out.println(check.getList().get(0).getAdjElement(3));
        }
    }
}
