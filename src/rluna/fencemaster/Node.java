/* COMP30024 Artificial Intelligence
 * FenceMaster AI
 * Authors: Rosa Luna <rluna> and Ryan Hodgman <hodgmanr>
 */

package rluna.fencemaster;

import aiproj.fencemaster.*;

/** Represents a group of connected tiles of the same colour. */
public class Node{
/* The class variables */	
	/** The move represented by this node. **/
	private Move move;
	
	/** A node containing the details of the best move from this node. **/
	private Node best_path;
	
	/** The result of the evaluation heuristic on this node. **/
	private int utility;
	
	/** a-B pruning: Tracks the max value achieved so far in the mini-max tree. **/
	private int alpha;

	/** a-B pruning: Tracks the min value achieved so far in the mini-max tree. **/
	private int beta;
	
	/** How deep into the tree this node is. **/
	private int depth;
	
/* The getter and setter methods */
	/** Returns the move that this node represents. */
	public Move getMove() {
		return move;
	} 
	
	/** Sets the move that this node represents. */
	public void setMove(Move move) {
		this.move = move;
	}   
	
	/** Returns the best move from this node. */
	public Move getBestMove() {
		return best_path.getMove();
	}
	
	/** Sets the best move from this node. */
	public void setBestMove(Node best_path) {
		this.best_path = best_path;
	}  
	
	/** Returns the utility of this node according to the evaluation heuristic. */
	public int getUtility() {
		return utility;
	}   
	
	/** Sets the utility of this node. */
	public void setUtility(int utility) {
		this.utility = utility;
	}
	
	/** Returns the maximum value achieved in the descendants of this node. */
	public int getAlpha() {
		return alpha;
	}   
	
	/** Sets the maximum value achieved in the descendants of this node. */
	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}   
	
	/** Returns the minimum value achieved in the descendants of this node. */
	public int getBeta() {
		return beta;
	}   
	
	/** Sets the minimum value achieved in the descendants of this node. */
	public void setBeta(int beta) {
		this.beta = beta;
	}   
	
	/** Returns the depth of this node. */
	public int getDepth() {
		return depth;
	}
	
/* The constructor(s) */
    /** Creates a new node object. 
     * @param depth The depth of this node into the mini-max tree. */
    public Node(int depth) {
		// a-B pruning: Initialises local variables to represent the max and min achieved values at this node.
    	alpha = Integer.MIN_VALUE;
    	beta = Integer.MAX_VALUE;
    	this.depth = depth;
    }
}