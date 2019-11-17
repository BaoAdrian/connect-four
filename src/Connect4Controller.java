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
	private boolean isServer; // Server is ALWAYS yellow. || Client is ALWAYS red
	private boolean isHuman;
	private Connect4Server server;
	private Connect4Client client;
	private boolean GUIDisabled;
	
	public Connect4Controller() {
		this.model = new Connect4Model();
	}
	
	public Connect4Controller(List<List<Integer>> board) {
		this.model = new Connect4Model(board);
	}
	
	public void createGame(boolean isServer, boolean isHuman, String host, int port) {
		this.isServer = isServer;
		this.isHuman = isHuman;
		
		// Network role is server, start server.
		if (isServer) {
			server = new Connect4Server(port);
			GUIDisabled = false;
		} else {
			client = new Connect4Client(host, port);
			GUIDisabled = true;
		}
		
		if (!this.isHuman) {
			GUIDisabled = true;
		}
	}
	
	public boolean isGUIDisabled() {
		return GUIDisabled;
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
	public boolean hasOpenSlot(int column) {
		// Query the Model to see if there is open space in requested column
		List<List<Integer>> board = model.getBoard();
		return board.get(column).get(ROWS - 1) == null;
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
		if (hasOpenSlot(col)) {
			placeInRow(col);
		} else {
			// Invalid slot chosen, notify model of erroneous
			// placement, pop modal
		}
	}
	
	/**
	 * Executes a move on behalf of the computer
	 * 
	 * Generates a randomly generated column 
	 * number that yields a VALID select and proceeds
	 * to place a token at that position.
	 */
	public void computerTurn() {		
		int col = getValidColumn(); // RNG Logic
		if (col != -1) {
			placeInRow(col);
		}
	}
	
	/**
	 * Places a token in the next available slot inside
	 * of the provided column. 
	 * 
	 * @param col Column to insert a token into
	 */
	public void placeInRow(int col) {
		Connect4MoveMessage  message;
		List<Integer> column = model.getBoard().get(col);
		int row = 0;
		while (column.get(row) != null) {
			row ++;
		}
		
		if (isServer) {
			message = new Connect4MoveMessage(row, col, Connect4MoveMessage.YELLOW);
			model.updateBoard(col, row, Connect4MoveMessage.YELLOW);
		} else {
			message = new Connect4MoveMessage(row, col, Connect4MoveMessage.RED);
			model.updateBoard(col, row, Connect4MoveMessage.RED);
		}
	}
	
	/**
	 * Retrieves a valid column for the computer to make
	 * a valid selection. Returns the column index to be 
	 * used for token insertion.
	 * 
	 * @return int index of valid column, -1 if none available
	 */
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
	public boolean isBoardFull() {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLUMNS; col++) {
				if (model.getBoard().get(col).get(row) == null) {
					return false;
				}
			}
		}
		return true;
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
	public boolean checkRows() {
		Integer currId = -1;
		for (int row = 0; row < ROWS; row++) {
			int currCount = 0;
			for (int col = 0; col < COLUMNS; col++) {
				Integer id = model.getBoard().get(col).get(row);
				if (id == currId) {
					currCount++;
					if (currCount == WINNING_COUNT) {
						return true;
					}
				} else {
					if (id != null) {
						currId = model.getBoard().get(col).get(row);
						currCount = 1;
					}
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
	public boolean checkCols() {
		Integer currId = -1;
		for (int col = 0; col < COLUMNS; col++) {
			int currCount = 0;
			for (int row = 0; row < ROWS; row++) {
				Integer id = model.getBoard().get(col).get(row);
				if (id == currId) {
					currCount++;
					if (currCount == WINNING_COUNT) {
						return true;
					}
				} else {
					if (id != null) {
						currId = model.getBoard().get(col).get(row);
						currCount = 1;
					}
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
	public boolean checkDiagonals() {
		
		// Check all rows in column 0
		for (int row = 0; row < ROWS; row++) {
			if (model.getBoard().get(0).get(row) == null) {
				continue;
			}
			if (checkRightDiagonals(0, row)) {
				return true;
			}
		}
		
		// Check all rows in column COLUMNS - 1
		for (int row = 0; row < ROWS; row++) {
			if (model.getBoard().get(COLUMNS - 1).get(row) == null) {
				continue;
			}
			if (checkLeftDiagonals(COLUMNS - 1, row)) {
				return true;
			}
		}
		
		// Check both left and right diagonals from bottom of board
		for (int col = 0; col < COLUMNS; col++) {
			if (model.getBoard().get(col).get(0) == null) {
				continue;
			}
			if (checkRightDiagonals(col, 0)) {
				return true;
			}
			if (checkLeftDiagonals(col, 0)) {
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
	public boolean checkRightDiagonals(int col, int row) {
		int currCount = 0;
		Integer currId = -1;
		
		// Starting from board[row][col], increment both until a bound is hit
		while (row < ROWS && col < COLUMNS) {
			Integer id = model.getBoard().get(col).get(row);
			if (id == currId) {
				currCount++;
				if (currCount == WINNING_COUNT) {
					return true;
				}
			} else {
				if (id != null) {
					currId = model.getBoard().get(col).get(row);
					currCount = 1;
				}
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
	public boolean checkLeftDiagonals(int col, int row) {
		int currCount = 0;
		Integer currId = -1;
		
		// Starting from board[row][col], increment both until a bound is hit
		while (row < ROWS && col >= 0) {
			Integer id = model.getBoard().get(col).get(row);
			if (id == currId) {
				currCount++;
				if (currCount == WINNING_COUNT) {
					return true;
				}
			} else {
				if (id != null) {
					currId = model.getBoard().get(col).get(row);
					currCount = 1;
				}
			}
			row++;
			col--;
		}
		
		return false;
	}
}
