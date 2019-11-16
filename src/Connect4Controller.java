import java.util.List;
import java.util.Random;

public class Connect4Controller {
	// Creates Model and View
	private Connect4Model model;
	
	// Public constants
	public static final int ROWS = 6;
	public static final int COLUMNS = 7;
	private static final int WINNING_COUNT = 4;
	
	public Connect4Controller() {
		this.model = new Connect4Model();
	}
	
	/**
	 * Checks the given column to see if its a valid move
	 * 
	 * Retrieves the board state and queries to see if the
	 * requested column is valid
	 * 
	 * @param column Requested column to be validated
	 * @return boolean result on the validity check of move
	 */
	public boolean isMoveValid(int column) {
		// Query the Model to see if there is open space in requested column
		List<List<Integer>> board = model.getBoard();
		int lastIdx = board.get(column).size();
		if (board.get(0).get(lastIdx) != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * Executes a requested move on behalf of the human
	 * 
	 * Accepts a column requested to place a token and 
	 * provides that request to the model to update 
	 * accordingly. 
	 * 
	 * @param col Column player has requested to play in
	 */
	public void humanTurn(int col) {
		
	}
	
	/**
	 * Executes a move on behalf of the computer
	 * 
	 * Generates a randomly generated column 
	 * number that yields a VALID select and proceeds
	 * to place a token at that position.
	 */
	public void computerTurn() {
		int column = getValidColumn(); // RNG Logic
		// model.placeMove(Player comp, column);
	}
	
	private int getValidColumn() {
		// Check if board is full, if so, return -1
		if (isBoardFull()) {
			return -1;
		}
		
		// Find a valid column
		Random random = new Random();
		int selectedCol = random.nextInt(COLUMNS);
		while (!hasOpenSlot(selectedCol)) {
			selectedCol = random.nextInt(COLUMNS);
		}
		return selectedCol;
	}
	
	/**
	 * Checks to see if the board is currently full, i.e.
	 * there are no open slots to choose from in any column
	 * Returns true if board is full, false otherwise.
	 * 
	 * @return boolean result of board checking
	 */
	private boolean isBoardFull() {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLUMNS; col++) {
				if (model.getBoard().get(row).get(col) != null) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Checks a given column for an open slot
	 * 
	 * Starts at the top row of the board and works down
	 * towards the bottom search for an open slot (slot == null)
	 * and returns true if one exists. Otherwise, that column 
	 * is full and function returns false
	 * 
	 * @param column Requested column to query
	 * @return boolean result of the column query
	 */
	private boolean hasOpenSlot(int column) {
		for (int row = ROWS - 1; row >= 0; row++) {
			if (model.getBoard().get(column) == null) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks state of the board to see if game is over
	 * @return
	 */
	public boolean checkIfGameOver() {
		List<List<Integer>> board = model.getBoard();
		
		if (isBoardFull() || checkRows() || checkCols() || checkDiagonals()) {
			return true;
		}
		
		return false;
	}
	
	// Checks rows for a winner
	private boolean checkRows() {
		int currCount = 0;
		Integer currId = null;
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLUMNS; col++) {
				if (model.getBoard().get(row).get(col).equals(currId)) {
					currCount++;
					if (currCount == WINNING_COUNT) {
						return true;
					}
				} else {
					currId = model.getBoard().get(row).get(col);
					currCount = 1;
				}
			}
		}
		return false;
	}
	
	// Checks cols for a winner
	private boolean checkCols() {
		int currCount = 0;
		Integer currId = null;
		for (int col = 0; col < COLUMNS; col++) {
			for (int row = 0; row < ROWS; row++) {
				if (model.getBoard().get(row).get(col).equals(currId)) {
					currCount++;
					if (currCount == WINNING_COUNT) {
						return true;
					}
				} else {
					currId = model.getBoard().get(row).get(col);
					currCount = 1;
				}
			}
		}
		
		return false;
	}

	// Checks diagonals for a winner
	private boolean checkDiagonals() {
		
		// Two-stage checking
		// From lower-left -> upper-right
		// From upper-left -> lower-right
		
		// lower-left -> upper-right
		/*
		 * [0][2]
		 * [0][1]
		 * [0][0]
		 * [1][0]
		 * [2][0]
		 * [3][0]
		 */
		
		
		
		// upper-left -> lower-right
		/*
		 * [0][3]
		 * [0][4]
		 * [0][5]
		 * [0][6]
		 * [1][6]
		 * [2][6]
		 * [3][6]
		 */
		
		return false;
	}
}
