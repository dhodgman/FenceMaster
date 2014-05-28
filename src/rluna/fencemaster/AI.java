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
	
	/** Keeps track of the evaluation function variables weights */
	private EvalWeights weights;
	
/* The constructor(s) */
    /** Creates a new AI object. 
     * @param player The color of the player this object corresponds to. */
    public AI(int player) {
    	this.player = player;
    	swap = false;
    	this.weights = new EvalWeights();
    }
    
/* The class methods */
    /** Calculates the next move this player will make.
     * @param state The current game state. 
     * @param move_number The number of moves that have been made up to this point.
     * @return Returns the new move to be made. */
	public Move makeMove(GameState state, int move_number) {
		// Looking ahead ply moves. NOTE: Ply must always be odd to end on a move made by this player.
		ply = 1;
		// If this is the second move, enable a swap move.
		if(move_number == 1) {swap = true;}
		// Looks ahead to the current ply and determines the best move.
		Node prior_node = new Node(0);
		Node current_node = new Node(1);
		// Calls mini-max algorithm with max set to true for top-level.
		Node best_node = miniMax(state, current_node, prior_node, true, move_number);
		Move best_move = best_node.getBestMove();		
		weights.getGameStates().add(new TDLeaf(best_node, weights));
		weights.updateWeightList();
		// Resets the swap variable and a-B variables.
		if(swap == true) {swap = false;}
		return best_move;
	}
	
    /** Determines recursively which path is the best choice from the current node.
     * @param current_state The game state according to this node on the mini-max tree.
     * @param current_node The node that has just been created by the previous call.
     * @param prior_node The node from which this method is being called.
     * @param max Set to true if this should return the max utility or to false if it should return the min utility.
     * @param move_number A track of the number of moves that have been made so far.
     * @return Returns a node containing the best move that can be made at this branch according to the evaluation heuristic. */
	public Node miniMax(GameState current_state, Node current_node, Node prior_node, Boolean max, int move_number) {
		// If the passed node is at the cut-off depth then evaluate and return it.
		if(current_node.getDepth() == ply + 1) {
			current_node.setUtility(eval(current_state, current_node, move_number));
			return current_node;
		}
		// Creates an array list of all of the immediately possible moves.
		ArrayList<Node> ops = calcOps(current_state, current_node);
		// Iterates through the possible moves and determine which move has the highest/lowest utility.
		Node best_path = new Node(0);
		for(int i = 0; i < ops.size(); i++) {
			// Concatenates the move_record of this node to the successor nodes.
			ops.get(i).getMoveRecord().addAll(current_node.getMoveRecord());
			// Recursively calls the miniMax method to determine the node at the start of the best path from this new leaf.
			Node path = miniMax(current_state, ops.get(i), current_node, !max, move_number + 1);
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
		current_node.setBestNode(best_path);
		return current_node;
	}
	
    /** Creates a list of possible moves from a specified game state.
     * @param origin_state The state from which to determine the possible moves.
     * @param prior_node The node immediately preceding the nodes initialized by this method.
     * @return Returns an array list of all the possible moves from this game state. */
	public ArrayList<Node> calcOps(GameState origin_state, Node prior_node) {
		// Increment the depth of these new nodes.
		int depth = prior_node.getDepth() + 1;
		ArrayList<Node> ops = new ArrayList<Node>();
		tile_loop: for(int i = 0; i < origin_state.getNumTiles(); i++) {
			Node new_node = new Node(depth);		
			// Check that either this tile is empty or a swap move is allowed.
			if(origin_state.getTileList().get(i).getPiece() == Rluna.EMPTY || swap) {
				int row = origin_state.getTileList().get(i).getRow();
				int col = origin_state.getTileList().get(i).getCol();
				for(int q = 0; q < prior_node.getMoveRecord().size(); q++) {					
					
					// Guard to check that a move doesn't already exist at this tile in the move_record.
					if(prior_node.getMoveRecord().get(q).Row == row && prior_node.getMoveRecord().get(q).Col == col) {
						continue tile_loop;
					}
				}
				Move temp_move;
				// Creates a move object that represents a piece placed at this position.
				if(swap) {
					temp_move = new Move(player, true, row, col);
				} else {
					temp_move = new Move(player, false, row, col);
				}
				new_node.setMove(temp_move);
				ops.add(new_node);
			}
		}
		return ops;
	}
	
	/** Determines the utility value of the possible move given GameState it creates using a derived heuristic function.
     * @param current_state The GameState that is created by playing the possible move.
     * @param possible_move The Move whose utility value is being determined.
     * @param move_number The number of moves that have been made to date, including possible_move
     * @return Returns a double that represents the utility value of the possible move. */
	public double eval(GameState current_state, Node possible_move, int move_number) {
		// The tile ID of the possible move to be made.
		int tile_ID = GameState.calcTileID(possible_move.getMove().Row, possible_move.getMove().Col);
		// Factors that need to be considered:
		int is_corner = 0;
		int is_side = 0;
		int is_middle = 0;
		// Determines if the move was made on a corner, side or middle tile
		if(current_state.isEdge(tile_ID, Tile.CORNER) == 1){
			is_corner = 1;
		} else if(current_state.isEdge(tile_ID, Tile.SIDE) == 1){
			is_side = 1;
		} else {
			is_middle = 1;
		}
		// Creates a temporary deep copy of the current game state and updates it according to the move_record in the evaluated node.
		GameState eval_state = new GameState(current_state);
		for(int q = 0; q < possible_move.getMoveRecord().size(); q++) {
			eval_state.updateState(GameState.calcTileID(possible_move.getMoveRecord().get(q).Row, possible_move.getMoveRecord().get(q).Col), possible_move.getMoveRecord().get(q).P);
		}
		// White priority (how many white pieces surround the move) (range: 0 - 6)
		// and black priority (how many black pieces surround the move) (range: 0 - 6)
		// are modulated by 4 to give the original range the following priority:
		// [0 = 0, 1 = 1, 2 = 2, 3 = 3, 4 = 0, 5 = 1, 6 = 2]
		int wp = eval_state.getTileList().get(tile_ID).getWhitePriority()%4;
		int bp = eval_state.getTileList().get(tile_ID).getBlackPriority()%4;
		
		int will_win = 0;
		// Guard to check that a minimum number of moves have been made to enable either player to win.
		if(move_number < 12) {
			will_win = 0;
		} else {
			int result = eval_state.calcResult();
			// Determine if the move leads to the player winning, neither player winning or the opponent winning.
			if(result == possible_move.getMove().P) {
				will_win = 1;
			} else if(result == Rluna.INVALID){
				will_win = 0;
			} else {
				will_win = -1;
			}
		}
		
		/**Currently not sure of the last two factors
		 * Not sure if will win should be split up
		 * progressively estimating if move will lead to a loop or tripod sounds difficult
		 * blocking also proving to be difficult**/

		// will move block opponent?
		// Not sure how to check if move is blocking yet
		
		// plan is to use these factors as variables, each with their own weight factor
		// I'm just not sure how to update the weights.
		// might have to manually change the weights after each run by having the game spit out the new weights
		
		double eval = weights.getWeightList()[0]*is_corner + weights.getWeightList()[1]*is_side + weights.getWeightList()[2]*is_middle 
				+ weights.getWeightList()[3]*wp + weights.getWeightList()[4]*bp + weights.getWeightList()[5]*will_win;
		return eval;
	}
}