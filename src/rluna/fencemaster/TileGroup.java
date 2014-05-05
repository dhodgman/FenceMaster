/* COMP30024 Artificial Intelligence
 * FenceMaster AI
 * Authors: Rosa Luna <rluna> and Ryan Hodgman <hodgmanr>
 */

package rluna.fencemaster;

import java.util.ArrayList;

/** Represents a group of connected tiles of the same colour. */
public class TileGroup{
/* The class variables */
	/** An array that stores all of the tile IDs in this group. */
	public ArrayList<Integer> group_tiles;
	
	/** Stores the colour of this group of tiles. */
	private char player_colour;
	
	/** Stores the ID of this tile group. */
	private int ID;

/* The constructor(s) */
    /** Creates a new TileGroup object.
     * @param ID The designated ID of this tile group.
     * @param player The colour of the player corresponding to this tile group. */
    public TileGroup(int ID, char player_colour) {
    	this.ID = ID;
    	this.player_colour = player_colour;
    	group_tiles = new ArrayList<Integer>();
    }

/* The getter and setter methods */
	/** Returns the ID of this group. */
	public int getID() {
		return ID;
	}   
	
	/** Returns the colour of the player this group belongs to. */
	public char getPlayer() {
		return player_colour;
	}   
	
	/** Sets the colour of the player this group belongs to. */
	public void setPlayer(char colour) {
		player_colour = colour;
	}   
}