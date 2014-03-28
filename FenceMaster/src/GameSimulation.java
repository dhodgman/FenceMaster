/* COMP30024 Artificial Intelligence
 * FenceMaster AI
 * Author: Ryan Hodgman <hodgmanr>
 */

import java.io.FileNotFoundException;

/** Main class for the Role-Playing Game engine.
 * Handles initialization, input and rendering.
 */
public class GameSimulation{
	/** A variable used to activate or deactivate debugging mode. */
    public static boolean DEBUG = true;
    
    /** Location of the "data" directory. */
    public static final String DATA_PATH = "data";
    
    /** The checker. */
    private static WinChecker check;
    
    /** Start-up method. Creates the game and runs it.
     * @param args Command-line arguments (ignored).
     */
    public static void main(String[] args) {
        try {
			check = new WinChecker();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Couldn't find the data file!");
			System.exit(0);
		}
        if(DEBUG) {
        	for(int t = 0; t < check.getList().size(); t++) {
                System.out.println(check.getList().get(t).getPiece() + ", [" + check.getList().get(t).getX() + ","+ check.getList().get(t).getY() + "]");
        	}
        	System.out.println(check.getList().get(0).getAdj()); // NOTE: Use an array_list instead of a 2D array and calculate tile_num directly.
        }
    }
}
