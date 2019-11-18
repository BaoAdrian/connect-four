/**
 * Connect4Controller
 * @author Mauricio Herrera, Adrian Bao
 * 
 * Purpose: Controller of the MVC Design Pattern implemented
 * for Connect 4. Contains all of the game logic including turns
 * win detection, and state update transfers.
 */

import java.util.List;
import java.util.Random;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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
	
	public Connect4Controller(Connect4Model model) {
		this.model = model;
	}
	
	public Connect4Controller(List<List<Integer>> board) {
		this.model = new Connect4Model(board);
	}
	
	public void createGame(boolean isServer, boolean isHuman, String host, int port) {
		this.isServer = isServer;
		this.isHuman = isHuman;
		
		// Network role is server, start server.
		if (isServer) {
			server = new Connect4Server(port, this);
			GUIDisabled = false;
		} else {
			client = new Connect4Client(host, port, this);
			client.connect();
			client.waitForMessage();
			GUIDisabled = true;
		}
		
////		// Instantiating both server and client for debugging purposes.
//		server = new Connect4Server(4000, this);
//		client = new Connect4Client("localhost", 4000, this);
//		client.connect();
//		GUIDisabled = true;
	}
	
	/**
	 * Public Accessor method for GUIDisabled
	 * @return boolean value of GUIDisabled attribute
	 */
	public boolean isGUIDisabled() {
		return GUIDisabled;
	}
	
	/**
	 * Simple mutator for GUIDisabled attribute
	 * to set it equal to true, disabling the GUI.
	 */
	public void disableGUI() {
		GUIDisabled = true;
	}
	
	/**
	 * Simple mutator for GUIDisabled attribute
	 * to set it equal to false, enabling the GUI.
	 */
	public void enableGUI() {
		GUIDisabled = false;
	}
	
	/**
	 * Passes a message received from the GUI to the model
	 * to perform the require update to the board with the 
	 * requested move.
	 * 
	 * @param message Connect4MoveMessage object containing
	 * placement and color information.
	 */
	public void handleMessage(Connect4MoveMessage message) {
		model.updateBoard(message);
		enableGUI();
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
			disableGUI();
			placeInRow(col);
		} else {
			// Erroneous selection, notify user
			Alert columnFullAlert = new Alert(AlertType.WARNING);
			columnFullAlert.setContentText("Column full, pick somewhere else!");
			columnFullAlert.showAndWait();
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
	 * Checks the given column to see if its a valid move
	 * 
	 * Retrieves the board state and queries to see if the
	 * requested column is valid
	 * 
	 * @param column Requested column to be validated
	 * @return boolean result on the validity check of move
	 */
	private boolean hasOpenSlot(int column) {
		// Query the Model to see if there is open space in requested column
		List<List<Integer>> board = model.getBoard();
		return board.get(column).get(ROWS - 1) == null;
	}
	
	/**
	 * Places a token in the next available slot inside
	 * of the provided column. 
	 * 
	 * @param col Column to insert a token into
	 */
	private void placeInRow(int col) {
		Connect4MoveMessage  message;
		List<Integer> column = model.getBoard().get(col);
		int row = 0;
		while (column.get(row) != null) {
			row ++;
		}
		
		if (isServer) {
			model.updateBoard(col, row, Connect4MoveMessage.YELLOW);
			message = new Connect4MoveMessage(row, col, Connect4MoveMessage.YELLOW);
			server.sendMessage(message);
			server.waitForMessage();
		} else {
			message = new Connect4MoveMessage(row, col, Connect4MoveMessage.RED);
			model.updateBoard(col, row, Connect4MoveMessage.RED);
			client.sendMessage(message);
			client.waitForMessage();
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
	private boolean isBoardFull() {
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
	 * @return boolean result on if the game is over
	 */
	public boolean checkIfGameOver() {
		List<List<Integer>> board = model.getBoard();
		if (isBoardFull() || checkRows() != -1 || 
				checkCols() != -1 || checkDiagonals() != -1) {
			return true;
		}
		return false;
	}
	
	/**
	 * Declares the winner of the game
	 * 
	 * Finds the winning player and passes the information 
	 * along to the model to update the View with the winning
	 * and losing Alerts.
	 */
	public void declareWinner() {
		Integer winningId = -1;
		winningId = checkRows();
		if (winningId != -1) {
			model.updateBoard(winningId);
		}
		
		winningId = checkCols();
		if (winningId != -1) {
			model.updateBoard(winningId);
		}
		
		winningId = checkDiagonals();
		if (winningId != -1) {
			model.updateBoard(winningId);
		}
	}
	
	/**
	 * Searches all rows on the existing board to see 
	 * if a winner exists. Returns winnnigId if one exists,
	 * -1 otherwise.
	 * 
	 * @return Integer id of the winning id, -1 if none
	 */
	private Integer checkRows() {
		Integer currId = -1;
		for (int row = 0; row < ROWS; row++) {
			int currCount = 0;
			for (int col = 0; col < COLUMNS; col++) {
				Integer id = model.getBoard().get(col).get(row);
				if (id == currId) {
					currCount++;
					if (currCount == WINNING_COUNT) {
						return currId;
					}
				} else {
					if (id != null) {
						currId = model.getBoard().get(col).get(row);
						currCount = 1;
					}
				}
			}
		}
		return -1;
	}
	
	/**
	 * Searches all columns on the existing board to see 
	 * if a winner exists. Returns winnnigId if one exists,
	 * -1 otherwise.
	 * 
	 * @return Integer id of the winning id, -1 if none
	 */
	private Integer checkCols() {
		Integer currId = -1;
		for (int col = 0; col < COLUMNS; col++) {
			int currCount = 0;
			for (int row = 0; row < ROWS; row++) {
				Integer id = model.getBoard().get(col).get(row);
				if (id == currId) {
					currCount++;
					if (currCount == WINNING_COUNT) {
						return currId;
					}
				} else {
					if (id != null) {
						currId = model.getBoard().get(col).get(row);
						currCount = 1;
					}
				}
			}
		}
		return -1;
	}

	/**
	 * Searches all diagonals on the existing board to see 
	 * if a winner exists. Returns winnnigId if one exists,
	 * -1 otherwise.
	 * 
	 * @return Integer id of the winning id, -1 if none
	 */
	private Integer checkDiagonals() {
		
		// Check all rows in column 0
		for (int row = 0; row < ROWS; row++) {
			if (model.getBoard().get(0).get(row) == null) {
				continue;
			}
			Integer winningId = checkRightDiagonals(0, row);
			if (winningId != -1) {
				return winningId;
			}
		}
		
		// Check all rows in column COLUMNS - 1
		for (int row = 0; row < ROWS; row++) {
			if (model.getBoard().get(COLUMNS - 1).get(row) == null) {
				continue;
			}
			Integer winningId = checkLeftDiagonals(COLUMNS - 1, row);
			if (winningId != -1) {
				return winningId;
			}
		}
		
		// Check both left and right diagonals from bottom of board
		for (int col = 0; col < COLUMNS; col++) {
			if (model.getBoard().get(col).get(0) == null) {
				continue;
			}
			Integer winningId = checkRightDiagonals(col, 0); 
			if (winningId != -1) {
				return winningId;
			}
			winningId = checkLeftDiagonals(col, 0); 
			if (winningId != -1) {
				return winningId;
			}
		}		
		return -1;
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
	 * @return Integer id of the winning id, -1 if none
	 */
	private Integer checkRightDiagonals(int col, int row) {
		int currCount = 0;
		Integer currId = -1;
		
		// Starting from board[row][col], increment both until a bound is hit
		while (row < ROWS && col < COLUMNS) {
			Integer id = model.getBoard().get(col).get(row);
			if (id == currId) {
				currCount++;
				if (currCount == WINNING_COUNT) {
					return currId;
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
		return -1;
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
	 * @return Integer id of the winning id, -1 if none
	 */
	private Integer checkLeftDiagonals(int col, int row) {
		int currCount = 0;
		Integer currId = -1;
		
		// Starting from board[row][col], increment both until a bound is hit
		while (row < ROWS && col >= 0) {
			Integer id = model.getBoard().get(col).get(row);
			if (id == currId) {
				currCount++;
				if (currCount == WINNING_COUNT) {
					return currId;
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
		return -1;
	}
}
