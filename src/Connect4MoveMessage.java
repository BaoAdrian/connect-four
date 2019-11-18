/**
 * @author Mauricio Herrera, Adrian Bao
 * 
 * CSC 335 - Object Oriented Programming and Design
 * 
 * Title: Networked Connect 4
 * 
 * File: Connect4MoveMessage.java
 * 
 * Description: This class acts as a container for the descriptions
 * of player moves to be used in client-server communications.
 */

import java.io.Serializable;

public class Connect4MoveMessage implements Serializable{
	// Fields
    public static int YELLOW = 1;
    public static int RED = 2;

    private static final long serialVersionUID = 1L;

    private int row;
    private int col;
    private int color;

    // Constructor
    public Connect4MoveMessage(int row, int col, int color) {
    	this.row = row;
    	this.col = col;
    	this.color = color;
    }

    // Methods
    public int getRow() {
    	return row;
    }

    public int getColumn() {
    	return col;
    }

    public int getColor() {
    	return color;
    }
    
    public String toString() {
    	if (color == YELLOW) {
    		return "column: " + col + ", row: " + row +", color: yellow";
    	} else {
    		return "column: " + col +  ", row: " + row +", color: red";
    	}
    }
}
