/* COMP30024 Artificial Intelligence
 * FenceMaster AI
 * Authors: Rosa Luna <rluna> and Ryan Hodgman <hodgmanr>
 */

package rluna.fencemaster;

import aiproj.fencemaster.*;
import java.util.ArrayList;

/** Represents a group of connected tiles of the same colour. */
public class AI{
/* The class variables */	
	/** This player's colour. **/
	private int player;
	
	/** A boolean that states whether or not a swap move is currently legal. **/
	private Boolean swap;
	
	/** Indicates the cut-off depth for the mini-max tree. **/
	private int ply;
	
/* The constructor(s) */
    /** Creates a new AI object. 
     * @param player The color of the player this object corresponds to. */
    public AI(int player) {
    	this.player = player;
    	swap = false;
    }
    
/* The class methods */
    /** Calculates the next move this player will make.
     * @param state The current game state. 
     * @param move_number The number of moves that have been made up to this point.
     * @return Returns the new move to be made. */
	public Move makeMove(GameState state, int move_number) {
		ply = 3;
		// If this is the second move, enable a swap move.
		if(move_number == 1) {swap = true;}
		// Looks ahead to the current ply and determines the best move.
		Node prior_node = new Node(0);
		Node current_node = new Node(1);
		Move best_move = miniMax(state, current_node, prior_node, true).getBestMove();
		// Resets the swap variable and a-B variables.
		if(swap == true) {swap = false;}
		return best_move;
	}
	
    /** Determines recursively which path is the best choice from the current node.
     * @param current_state The game state according to this node on the mini-max tree.
     * @param current_node The node that has just been created by the previous call.
     * @param prior_node The node from which this method is being called.
     * @param max Set to true if this should return the max utility or to false if it should return the min utility.
     * @return Returns a node containing the best move that can be made at this branch according to the evaluation heuristic. */
	public Node miniMax(GameState current_state, Node current_node, Node prior_node, Boolean max) {
		// If the passed node is at the cut-off depth then evaluate and return it.
		if(current_node.getDepth() == ply) {
			current_node.setUtility(eval(current_state, current_node.getMove()));
			return current_node;
		}
		// Creates an array list of all of the immediately possible moves.
		ArrayList<Node> ops = calcOps(current_state, current_node.getDepth() + 1);
		// Iterates through the possible moves and determine which move has the highest/lowest utility.
		Node best_path = new Node(0);
		for(int i = 0; i < ops.size(); i++) {
			// Creates a new game state that represents the board after this move has been made.
			GameState new_state = current_state;
			new_state.updateState(GameState.calcTileID(ops.get(i).getMove().Row, ops.get(i).getMove().Col), player);
			// Recursively calls the miniMax method to determine the node at the start of the best path from this new leaf.
			Node path = miniMax(new_state, ops.get(i), current_node, !max);
			// a-B pruning: This is where alpha and beta are updated and pruning is conducted.
			if(max) {
				if(current_node.getAlpha() < path.getUtility()) {
					current_node.setAlpha(path.getUtility());
					best_path = path;
				}
				// Ignores (prunes) all remaining branches. [NOTE: Potentially should not prune if prior.alpha == this.beta!]
				if(prior_node.getBeta() <= current_node.getAlpha()) {
					break;
				}
			} else {
				if(current_node.getBeta() > path.getUtility()) {
					current_node.setBeta(path.getUtility());
					best_path = path;
				}
				// Ignores (prunes) all remaining branches. [NOTE: Potentially should not prune if prior.alpha == this.beta!]
				if(prior_node.getAlpha() >= current_node.getBeta()) {
					break;
				}
			}
		}
		// Sets the utility of the current node to be equal to that of the best path from this node, then tracks the best path.
		current_node.setUtility(best_path.getUtility());
		current_node.setBestMove(best_path);
		return current_node;
	}
	
    /** Creates a list of possible moves from a specified game state.
     * @param origin_state The state from which to determine the possible moves.
     * @param depth The depth of the node that this is determining branches for.
     * @return Returns an array list of all the possible moves from this game state. */
	public ArrayList<Node> calcOps(GameState origin_state, int depth) {
		ArrayList<Node> ops = new ArrayList<Node>();
		for(int i = 0; i < origin_state.getNumTiles(); i++) {
			// Check that either this tile is empty or a swap move is allowed.
			if(origin_state.getTileList().get(i).getPiece() == Rluna.EMPTY || swap) {
				int row = origin_state.getTileList().get(i).getRow();
				int col = origin_state.getTileList().get(i).getCol();
				Move temp_move;
				// Creates a move object that represents a piece placed at this position.
				if(swap) {
					temp_move = new Move(player, true, row, col);
				} else {
					temp_move = new Move(player, false, row, col);
				}
				Node new_node = new Node(depth);
				new_node.setMove(temp_move);
				ops.add(new_node);
			}
		}
		return ops;
	}
	
	/** Determines the utility value of the possible move given GameState it creates using a derived heuristic function
     * @param current_state The GameState that is created by playing the possible move 
     * @param possible_move The Move who's utility value is being determined
     * @return Returns an int value that represents the utility value of the possible move*/
	public int eval(GameState current_state, Move possible_move){
		// Factors that need to be considered:
		// is it a corner piece? (where 1 = true, 0 = false) (will effect the start more)
		int is_corner = isCorner(current_state, possible_move);
		// is it a side piece? (where 1 = true, 0 = false) (will effect the start more)
		int is_side = isSide(current_state, possible_move, is_corner);
		// is it a middle piece? (where 1 = true, 0 = false) (will effect the start more)
		int is_middle = isMiddle(is_side, is_corner);
		// white priority (how many white pieces surround the move) (any positive integer)
		int wp = current_state.getTileList().get(GameState.calcTileID(possible_move.Row,possible_move.Col)).getWhitePriority();
		// black priority (how many black pieces surround the move) (any positive integer)
		int bp = current_state.getTileList().get(GameState.calcTileID(possible_move.Row,possible_move.Col)).getBlackPriority();
		
		/**Currently not sure of the last two factors
		 * Not sure if will win should be split up
		 * progressively estimating if move will lead to a loop or tripod sounds difficult
		 * blocking also proving to be difficult**/
		// current_state will already have updated group so check group for possible loop or tripod (more effective closer to the end of the game)
		int will_win = 0;
		if(current_state.calcResult()==possible_move.P) {will_win = 1;}
		// will move block opponent?
		// Not sure how to check if move is blocking yet
		
		// plan is to use these factors as variables, each with their own weight factor
		// I'm just not sure how to update the weights.
		// might have to manually change the weights after each run by having the game spit out the new weights
		return 0;
	}
	
	/** Determines if a move is on a corner tile or not
     * @param current_state The GameState that is created by playing the possible move 
     * @param possible_move The Move to be checked if it involves a corner tile 
     * @return Returns 1 if move is on a corner tile or 0 if it is not*/
	public int isCorner(GameState current_state, Move possible_move){
		if(possible_move.Row == 0){
			if(possible_move.Col == 0 || possible_move.Col == (current_state.getDim()-1)){
				return 1;
			}
			return 0;
		}
		if(possible_move.Row == (current_state.getDim()-1)){
			if(possible_move.Col == 0 || possible_move.Col == (2*current_state.getDim()-2)){
				return 1;
			}
			return 0;
		}
		if(possible_move.Row == 2*(current_state.getDim()-1)){
			if(possible_move.Col == (current_state.getDim()-1) || possible_move.Col == (2*current_state.getDim()-2)){
				return 1;
			}
			return 0;
		}
		return 0;
	}
	
	/** Determines if a move is on a side tile or not
     * @param current_state The GameState that is created by playing the possible move 
     * @param possible_move The Move to be checked if it involves a side tile
     * @param is_corner Flags if the move has been already been identified as a corner piece 
     * @return Returns 1 if move is on a side tile or 0 if it is not*/
	public int isSide(GameState current_state, Move possible_move, int is_corner){
		// Checks if move has already been identified as a corner piece
		if(is_corner==1){ return 0;}
		
		//Calculates how many adjacent tiles are INVALID tiles
		int invalid_count = 0;
		for(int i=0; i < Tile.NUM_ADJ; i++){
			if(current_state.getTileList().get(GameState.calcTileID(possible_move.Row,possible_move.Col)).getAdjElement(i) == -1) {
				invalid_count++;
			}
		}
		
		// If there are 2 invalid tiles connected to the tile, then the tile is a side piece 
		if(invalid_count == 2){ return 1; }

		return 0;
	}
	
	/** Determines if a move is on a middle tile or not
     * @param is_side Flags if the move has been already been identified as a side piece 
     * @param is_corner Flags if the move has been already been identified as a corner piece 
     * @return Returns 1 if move is on a middle tile or 0 if it is not*/
	public int isMiddle(int is_side, int is_corner){
		// Checks if the move has already been identified as a side piece or a corner piece
		if(is_side==0 && is_corner==0){
			return 1;
		}
		return 0;
	}
}