import java.util.ArrayList;
import java.util.List;

public class Connect4Model {
	// In Board, inner list represents column. Elements in list represent rows.
	// [0][0] represents lower left position.
	private List<List<Integer>> board;
	
	// Constructor
	public Connect4Model() {
		initializeBoard();
	}
	
	/**
	 * This method initializes the 2D list that will contain the tokens.
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
	
	public void placeMove(Player player, int column) {
		
	}
	
	/**
	 * Getter method for the board object
	 * @return 2D list containing tokens.
	 */
	public List<List<Integer>> getBoard() {
		return board;
	}
}
