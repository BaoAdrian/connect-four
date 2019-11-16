import java.util.List;
import java.util.Random;

public class Connect4Controller {
	// Creates Model and View
	private Connect4Model model;
	
	// Public constants
	public static final int ROWS = 6;
	public static final int COLUMNS = 7;
	private static final int WINNING_COUNT = 4;
	
	//Fields
	private boolean isServer;
	
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
		if (board.get(0).get(lastIdx - 1) != null) {
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
	
	/**
	 * Searches all rows on the existing board to see 
	 * if a winner exists. Returns true if so, otherwise
	 * return false
	 * 
	 * @return boolean result of the row-search on the board
	 */
	private boolean checkRows() {
		Integer currId = -1;
		for (int row = 0; row < ROWS; row++) {
			int currCount = 0;
				// currCount should be inside first for loop
			for (int col = 0; col < COLUMNS; col++) {
				if (model.getBoard().get(col).get(row).equals(currId)) {
					currCount++;
					if (currCount == WINNING_COUNT) {
						return true;
					}
				} else {
					currId = model.getBoard().get(col).get(row);
					currCount = 1;
				}
			}
		}
		return false;
	}
	
	/**
	 * Searches all columns on the existing board to see 
	 * if a winner exists. Returns true if so, otherwise
	 * return false
	 * 
	 * @return boolean result of the column-search on the board
	 */
	private boolean checkCols() {
		int currCount = 0;
		Integer currId = -1;
		for (int col = 0; col < COLUMNS; col++) {
			for (int row = 0; row < ROWS; row++) {
				if (model.getBoard().get(col).get(row).equals(currId)) {
					currCount++;
					if (currCount == WINNING_COUNT) {
						return true;
					}
				} else {
					currId = model.getBoard().get(col).get(row);
					currCount = 1;
				}
			}
		}
		
		return false;
	}

	/**
	 * Searches all diagonals on the existing board to see 
	 * if a winner exists. Returns true if so, otherwise
	 * return false
	 * 
	 * @return boolean result of the diagonal-search on the board
	 */
	private boolean checkDiagonals() {
		
		// Two-stage checking
		// From lower-left -> upper-right
		// From upper-left -> lower-right
		
		// Checking lower-left -> upper-right
		for (int row = 0; row < ROWS; row++) {
			if (model.getBoard().get(row).get(0) == null) {
				continue;
			}
			if (checkRightDiagonals(row, 0)) {
				return true;
			}
		}
		
		for (int col = 0; col < COLUMNS; col++) {
			if (model.getBoard().get(0).get(col) == null) {
				continue;
			}
			if (checkRightDiagonals(0, col)) {
				return true;
			}
		}		
		
		
		// Checking lower-right -> upper-left
		for (int row = 0; row < ROWS; row++) {
			if (model.getBoard().get(row).get(COLUMNS - 1) == null) {
				continue;
			}
			if (checkLeftDiagonals(row, COLUMNS - 1)) {
				return true;
			}
		}
		
		for (int col = 6; col >= 0; col--) {
			if (model.getBoard().get(0).get(col) == null) {
				continue;
			}
			if (checkLeftDiagonals(0, col)) {
				return true;
			}
		}	
		
		return false;
	}

	/**
	 * Checks all right-directed diagonals on the board to 
	 * determine if a winner exists
	 * 
	 * Starting from the given row/col, traverse the existing
	 * board to see if a winner exists on any right-directed
	 * diagonal (i.e. row++ && col++ increments)
	 * 
	 * @param row Starting row
	 * @param col Starting column
	 * @return boolean result of right-directed diagonal check
	 */
	private boolean checkRightDiagonals(int row, int col) {
		int currCount = 0;
		Integer currId = -1;
		
		// Starting from board[row][col], increment both until a bound is hit
		while (row < ROWS && col < COLUMNS) {
			if (model.getBoard().get(row).get(col).equals(currId)) {
				currCount++;
				if (currCount == WINNING_COUNT) {
					return true;
				}
			} else {
				currId = model.getBoard().get(row).get(col);
				currCount = 1;
			}
			row++;
			col++;
		}
		
		return false;
	}
	
	/**
	 * Checks all left-directed diagonals on the board to 
	 * determine if a winner exists
	 * 
	 * Starting from the given row/col, traverse the existing
	 * board to see if a winner exists on any left-directed
	 * diagonal (i.e. row-- && col-- increments)
	 * 
	 * @param row Starting row
	 * @param col Starting column
	 * @return boolean result of left-directed diagonal check
	 */
	public boolean checkLeftDiagonals(int row, int col) {
		int currCount = 0;
		Integer currId = -1;
		
		// Starting from board[row][col], increment both until a bound is hit
		while (row >= 0 && col >= 0) {
			if (model.getBoard().get(row).get(col).equals(currId)) {
				currCount++;
				if (currCount == WINNING_COUNT) {
					return true;
				}
			} else {
				currId = model.getBoard().get(row).get(col);
				currCount = 1;
			}
			row--;
			col--;
		}
		
		return false;
	}
}
