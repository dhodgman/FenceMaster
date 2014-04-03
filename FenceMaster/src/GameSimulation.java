/* COMP30024 Artificial Intelligence
 * FenceMaster AI
 * Authors: Rosa Luna <rluna> and Ryan Hodgman <hodgmanr>
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
    private static GameState state;
    
    /** Start-up method. Creates the game and runs it.
     * @param args Command-line arguments (ignored). */
    public static void main(String[] args) {
        try {
			state = new GameState();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Couldn't find the data file!");
			System.exit(0);
		}
        // Prints out the full list of tiles and their attributes (piece type, x-coord and y-coord).
        if(DEBUG) {
        	for(int t = 0; t < state.getTileList().size(); t++) {
                System.out.println(state.getTileList().get(t).getPiece() + ", [" + state.getTileList().get(t).getX() + ","+ state.getTileList().get(t).getY() + "]");
        	}
        }
    }
}
