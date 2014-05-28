package rluna.fencemaster;

/** An object that stores the rewards for a state in the game. */
public class TDLeaf {
	/*The class variables*/
	
	/** The reward for this state*/
	private double reward;
	
	/** An array that contains the reward values for each variable weight*/
	private double[] current_weight;
	
	/** Gets the current weight for the specified element (ie. Variable) */
	public double getCurrentWeightElem(int index){
		return current_weight[index];
	}
	
	/** Gets the reward for this state*/
	public double getReward(){
		return reward;
	}
	
	/* The constructor(s) */
    /** Creates a new TDLeaf object.*/
    public TDLeaf(Node best_move, EvalWeights weights){
    	reward = Math.tanh(best_move.getUtility());
    	current_weight = new double[EvalWeights.NUM_VARIABLES];
    	for(int i=0; i<EvalWeights.NUM_VARIABLES;i++){
    		current_weight[i] = weights.getWeightList()[i]; 
    	}
    	
    }

}
