import java.util.ArrayList;
import java.util.List;

public class Connect4Model extends java.util.Observable {
	// In Board, inner list represents column. Elements in list represent rows.
	// [0][0] represents lower left position.
	private List<List<Integer>> board;
	private boolean isOver;
	
	public Connect4Model() {
		initializeBoard();
		isOver = false;
	}
	
	public Connect4Model(List<List<Integer>> board) {
		this.board = board;
		isOver = false;
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
	 * Public Mutator of the board to insert a specified color at 
	 * the given row/col index. As the Observable, sets the state
	 * that it has changed and notifies all observers.
	 * 
	 * @param col Given column to update
	 * @param row Given row to update
	 * @param color Color to be set at the given row,col position
	 */
	public void updateBoard(int col, int row, int color) {
		board.get(col).set(row, color);
		int parameters[] = {col, row, color};
		setChanged();
		notifyObservers(parameters);
	}
	
	/**
	 * Getter method for the board object
	 * @return 2D list containing disks.
	 */
	public List<List<Integer>> getBoard() {
		return board;
	}
	
	/**
	 * Performs an update to the isOver flag and notifies
	 * all observers of the change in game state so that 
	 * the Observers can update accordingly.
	 * 
	 * @param winningId Integer id of the winner
	 */
	public void updateToGameOver(Integer winningId) {
		isOver = true;
		setChanged();
		notifyObservers(winningId);
	}
}
