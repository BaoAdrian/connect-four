import java.util.List;

public class Connect4Controller {
	// Creates Model and View
	private Connect4Model model;
	
	// Public constants
	public static final int ROWS = 6;
	public static final int COLUMNS = 7;
	
	public Connect4Controller() {
		this.model = new Connect4Model();
	}
	
	/**
	 * Checks the given column to see if its a valid move
	 * 
	 * Retrieves the board state and queries to see if the
	 * requested column is valid
	 * @param column
	 * @return
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
	 * Process the move detected from the View
	 * 
	 * The requested column has been validated and this function
	 * will now pass the requested move to update the Model. 
	 * 
	 * @param column Requested column number to update
	 */
	public void placeMove(int column) {
		// update model 
//		model.placeMove(column);
	}
	
	/**
	 * Checks state of the board to see if game is over
	 * @return
	 */
	public boolean checkIfGameOver() {
		List<List<Integer>> board = model.getBoard();
		
		// Logic to check game state
		
		return false;
	}
}
