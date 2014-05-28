/* COMP30024 Artificial Intelligence
 * FenceMaster AI
 * Authors: Rosa Luna <rluna> and Ryan Hodgman <hodgmanr>
 */

package rluna.fencemaster;


import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/** Represents the weights of the variables of the evaluation function. */
public class EvalWeights {
	/* The class variables */
	/** The number of variables in the evaluation function. */
	public static final int NUM_VARIABLES = 6;
	
	/** An array that stores all the variables weights - Initialisation is default weights */
	// weight_list = {is_corner, is_side, is_middle, wp, bp, will_win} weights
	// Default weights are to be decided via trials
	private double[] weight_list;
	
	/**An array list with the results of each state of the game */
	private ArrayList<TDLeaf> game_states;
	
	/** Returns the weights list */
    public double[] getWeightList() {
    	return weight_list;
    }
    
    /** Returns the game_states list */
    public ArrayList<TDLeaf> getGameStates() {
    	return game_states;
    }
    
    /** Gamma value for weight update rule */
    private static final int GAMMA = 1;
    
    /** Learning rate for weight update rule */
    private static final double LEARNING_RATE = 0.1;
    
    /* The constructor(s) */
    /** Creates a new EvalWeights object.*/
    public EvalWeights(){
    	weight_list = new double[NUM_VARIABLES];
    	game_states = new ArrayList<TDLeaf>(); 
    	try {
    		// Scans the data file.
    		Scanner file_scanner = new Scanner(new File("data" + "/weights.txt"));
    		String line = ""; 
    		while(file_scanner.hasNextLine()){
    			line = file_scanner.nextLine();
    		}
    		System.out.println("line is: " + line);
    		
    		String weights[] = line.split(" "); 
    		if(weights.length == NUM_VARIABLES){
        		for(int i = 0; i < NUM_VARIABLES; i++){
        			weight_list[i] = Double.parseDouble(weights[i]);
        		}
    		} else {
    			System.out.println("Last element was not added to array");
    		}
    		file_scanner.close();
    	} catch(FileNotFoundException e1){
    		if(Rluna.DEBUG) { System.out.println("file not found"); }
    		// Default weights are to be decided via trials
    		for(int i = 0; i < NUM_VARIABLES; i++){
    			weight_list[i] = 1.0;
    		}
    		// Use default weights instead
    		// Do not exit program!
    	}
    }
    
    public void updateWeightList(){
    	
    	// Updates the weights list once there has been 2 or more states of the game
    	if(game_states.size()>2){
    		double weight_sum = 0;
    		// weight_sum is calculated by multiplying the ith weight reward with the sum of
    		// difference between successive game states rewards        	
        	for(int i=0; i<NUM_VARIABLES; i++){
        		for(int j = 0; j<game_states.size()-1; j++){
        			for(int k = 0; k < game_states.size()-1; k++){
        				weight_sum += ((game_states.get(j+1).getReward()-game_states.get(j).getReward())/(game_states.get(j+1).getCurrentWeightElem(i)-game_states.get(j).getCurrentWeightElem(i)))*(GAMMA^(k-j))*(game_states.get(k+1).getReward()-game_states.get(k).getReward());
        			}
        		}
        		weight_list[i] += LEARNING_RATE*weight_sum;
        	}
    	}
    		
	
		try {
			// file_writer is set such that what is written is appended to the specified file
			FileWriter file_writer = new FileWriter(new File("data" + "/weights.txt"), true);
			// Builds a string of weight values to be written to the file
			String weight_list_line = "";
			for(int i = 0; i < NUM_VARIABLES; i++){
				weight_list_line = weight_list_line + weight_list[i] + " ";
			}
			// Appends the string of weight values as a new line in the file
			file_writer.write("\n" + weight_list_line.substring(0, weight_list_line.length() - 1));
			file_writer.close();
		} catch (IOException e) {
			// do not write anything to file as weights stay the same
		} 
    }
}