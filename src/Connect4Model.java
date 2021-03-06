/**
 * @author Mauricio Herrera, Adrian Bao
 * 
 * CSC 335 - Object Oriented Programming and Design
 * 
 * Title: Networked Connect 4
 * 
 * File: Connect4Model.java
 * 
 * Description: This class contains the state of the game 
 * maintained as a board (two-dimensional ArrayLists).
 * Incorporates the appropriate accessors/mutators as 
 * well as update methods to notify any Observer that 
 * this Observable has been changed. This is the Observable 
 * for Connect4.
 */

import java.util.ArrayList;
import java.util.List;

public class Connect4Model extends java.util.Observable {
	// In Board, inner list represents column. Elements in list represent rows.
	// [0][0] represents lower left position.
	private List<List<Integer>> board;
	
	public Connect4Model() {
		initializeBoard();
	}
	
	public Connect4Model(List<List<Integer>> board) {
		this.board = board;
	}
	
	/**
	 * This method initializes the 2D list that will contain the disks.
	 * Where pucks will be represented as integers defined by the id given
	 * by the User object that performed the move. Each inner list in the 
	 * board is a column, each element in the inner list represents a row.
	 */
	private void initializeBoard() {
		board = new ArrayList<List<Integer>>();
		for (int i = 0; i < Connect4Controller.COLUMNS; i ++) {
			List<Integer> column = new ArrayList<Integer>();
			for (int j = 0; j < Connect4Controller.ROWS; j ++) {
				column.add(null);
			}
			board.add(column);
		}
	}
	
	/**
	 * Public mutator of the board to insert specified info given
	 * a Connect4MoveMessage. Notifies all Observers of the change
	 * to handle accordingly
	 *  
	 * @param message Connect4MoveMessage to be parsed for state change
	 */
	public void updateBoard(Connect4MoveMessage message) {
		board.get(message.getColumn()).set(message.getRow(), message.getColor());
		setChanged();
		notifyObservers(message);
	}
	
	
	/**
	 * Getter method for the board object
	 * @return 2D list containing disks.
	 */
	public List<List<Integer>> getBoard() {
		return board;
	}	
}
