/* COMP30024 Artificial Intelligence
 * FenceMaster AI
 * Authors: Rosa Luna <rluna> and Ryan Hodgman <hodgmanr>
 */

package rluna.fencemaster;

import aiproj.fencemaster.*;
import java.util.ArrayList;

/** Represents a group of connected tiles of the same colour. */
public class Node{
/* The class variables */	
	/** The move represented by this node. **/
	private Move move;
	
	/** A record of the moves made along the path below this node, including this node's move. **/
	private ArrayList<Move> move_record;
	
	/** A node containing the details of the best move from this node. **/
	private Node best_path;
	
	/** The result of the evaluation heuristic on this node. **/
	private double utility;
	
	/** a-B pruning: Tracks the max value achieved so far in the mini-max tree. **/
	private double alpha;

	/** a-B pruning: Tracks the min value achieved so far in the mini-max tree. **/
	private double beta;
	
	/** How deep into the tree this node is. **/
	private int depth;
	
/* The getter and setter methods */
	/** Returns the move that this node represents. */
	public Move getMove() {
		return move;
	} 
	
	/** Returns the move_record. */
	public ArrayList<Move> getMoveRecord() {
		return move_record;
	}
	
	/** Sets the move that this node represents. */
	public void setMove(Move move) {
		this.move = move;
		move_record.add(move);
	}   
	
	/** Returns the best move from this node. */
	public Move getBestMove() {
		return best_path.getMove();
	}
	
	/** Sets the best move from this node. */
	public void setBestNode(Node best_path) {
		this.best_path = best_path;
	}
	
	/** Returns the utility of this node according to the evaluation heuristic. */
	public double getUtility() {
		return utility;
	}   
	
	/** Sets the utility of this node. */
	public void setUtility(double utility) {
		this.utility = utility;
	}
	
	/** Returns the maximum value achieved in the descendants of this node. */
	public double getAlpha() {
		return alpha;
	}   
	
	/** Sets the maximum value achieved in the descendants of this node. */
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}   
	
	/** Returns the minimum value achieved in the descendants of this node. */
	public double getBeta() {
		return beta;
	}   
	
	/** Sets the minimum value achieved in the descendants of this node. */
	public void setBeta(double beta) {
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
    	move_record = new ArrayList<Move>();
    }
}