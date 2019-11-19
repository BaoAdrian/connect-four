import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class Connect4Test {
	
	@Test
	public void testFullBoard() {
		List<List<Integer>> fullBoard = buildBoard(1);
		Connect4Controller controller = new Connect4Controller(fullBoard);
		assertTrue(controller.getValidColumn() == -1);
	}
	
	@Test
	public void testModelInitialization() {
		Connect4Controller controller = new Connect4Controller(new Connect4Model());
		assertTrue(controller != null);
	}
	
	@Test
	public void testGameOverDetection() {
		// Test method on empty board
		Connect4Controller cOne = new Connect4Controller(buildBoard(null));
		assertFalse(cOne.checkIfGameOver());
		
		// Test method winning board
		List<List<Integer>> board = buildBoard(null);
		board.get(6).set(0, Connect4MoveMessage.RED);
		board.get(4).set(0, Connect4MoveMessage.YELLOW);
		board.get(3).set(1, Connect4MoveMessage.YELLOW);
		board.get(2).set(2, Connect4MoveMessage.YELLOW);
		board.get(1).set(3, Connect4MoveMessage.YELLOW);
		Connect4Controller cTwo = new Connect4Controller(board);
				
		assertTrue(cTwo.checkIfGameOver());
	}
	
	@Test
	public void testRowWinDetection() {
		// Test method on empty board
		Connect4Controller cOne = new Connect4Controller(buildBoard(null));
		assertFalse(cOne.checkIfGameOver()); // implicit call to checkRows()
		
		// Test method on winning board
		Connect4Controller cTwo = new Connect4Controller(buildRowWinBoard());
		assertTrue(cTwo.checkIfGameOver()); // implicit call to checkRows()
	}
	
	@Test
	public void testColWinDetection() {
		// Test method on empty board
		Connect4Controller cOne = new Connect4Controller(buildBoard(null));
		assertFalse(cOne.checkIfGameOver()); // implicit call to checkCols()

		// Test method on winning board
		Connect4Controller cTwo = new Connect4Controller(buildColWinBoard());
		assertTrue(cTwo.checkIfGameOver()); // implicit call to checkCols()
	}
	
	@Test
	public void testDiagonalWinDetection() {
		/*
		 * Cases:
		 * 1. Empty Board
		 * 2. Right-directed diagonal (@ column 0)
		 * 3. Right-directed diagonal (elsewhere)
		 * 4. Left-directed diagonal (@ column 5)
		 * 5. Left-directed diagonal (elsewhere)
		 */
		
		// Case 1 - Empty Board
		List<List<Integer>> emptyBoard = buildBoard(null);
		Connect4Controller cOne = new Connect4Controller(emptyBoard);
		assertFalse(cOne.checkIfGameOver()); // implicit call to checkDiagonals()
		
		// Case 2 - Right-directed Diagonal (@ column 0)
		List<List<Integer>> rdOne = buildBoard(null);
		rdOne.get(0).set(0, Connect4MoveMessage.YELLOW);
		rdOne.get(1).set(1, Connect4MoveMessage.YELLOW);
		rdOne.get(2).set(2, Connect4MoveMessage.YELLOW);
		rdOne.get(3).set(3, Connect4MoveMessage.YELLOW);
		Connect4Controller cTwo = new Connect4Controller(rdOne);
		assertTrue(cTwo.checkIfGameOver()); // implicit call to checkDiagonals()
		
		// Case 3 - Right-directed Diagonal (elsewhere)
		List<List<Integer>> rdTwo = buildBoard(null);
		rdTwo.get(2).set(0, Connect4MoveMessage.YELLOW);
		rdTwo.get(3).set(1, Connect4MoveMessage.YELLOW);
		rdTwo.get(4).set(2, Connect4MoveMessage.YELLOW);
		rdTwo.get(5).set(3, Connect4MoveMessage.YELLOW);
		Connect4Controller cThree = new Connect4Controller(rdTwo);
		assertTrue(cThree.checkIfGameOver()); // implicit call to checkDiagonals()
		
		
		// Case 4 - Left-directed Diagonal (@ column 6)
		List<List<Integer>> ldOne = buildBoard(null);
		ldOne.get(6).set(0, Connect4MoveMessage.YELLOW);
		ldOne.get(5).set(1, Connect4MoveMessage.YELLOW);
		ldOne.get(4).set(2, Connect4MoveMessage.YELLOW);
		ldOne.get(3).set(3, Connect4MoveMessage.YELLOW);
		Connect4Controller cFour = new Connect4Controller(ldOne);
		assertTrue(cFour.checkIfGameOver()); // implicit call to checkDiagonals()
		
		// Case 5 - Left-directed Diagonal (elsewhere)
		List<List<Integer>> ldTwo = buildBoard(null);
		ldTwo.get(6).set(0, Connect4MoveMessage.RED);
		ldTwo.get(4).set(0, Connect4MoveMessage.YELLOW);
		ldTwo.get(3).set(1, Connect4MoveMessage.YELLOW);
		ldTwo.get(2).set(2, Connect4MoveMessage.YELLOW);
		ldTwo.get(1).set(3, Connect4MoveMessage.YELLOW);
		Connect4Controller cFive = new Connect4Controller(ldTwo);

		assertTrue(cFive.checkIfGameOver());
	}
	
	@Test
	public void testEndGame() {
		List<List<Integer>> board = buildBoard(Connect4MoveMessage.RED);
		Connect4Controller controller = new Connect4Controller(board);
		
		assertTrue(controller.checkIfGameOver());

	}
	
	@Test
	public void testMoveMessageObject() {
		// Test creation/accessors
		Connect4MoveMessage yellowMessage = new Connect4MoveMessage(1, 2, Connect4MoveMessage.YELLOW);
		Connect4MoveMessage redMessage = new Connect4MoveMessage(0, 1, Connect4MoveMessage.RED);
		assertEquals(redMessage.getRow(), 0);
		assertNotEquals(redMessage.getRow(), -1);
		assertEquals(redMessage.getColumn(), 1);
		assertNotEquals(redMessage.getColumn(), -1);
		assertEquals(redMessage.getColor(), Connect4MoveMessage.RED);
		assertNotEquals(redMessage.getColor(), Connect4MoveMessage.YELLOW);		
		
		// Test message output for differing message IDs
		assertNotNull(yellowMessage.toString());
		assertNotNull(redMessage.toString());
	}
	
	@Test
	public void testNetworking() {
		// Test game creation as SERVER (true) and CLIENT (false)
		Connect4Controller serverC = new Connect4Controller(buildBoard(null));
		serverC.createGame(true, true, "localhost", 4000);
		
		Connect4Controller clientC = new Connect4Controller(buildBoard(null));
		clientC.createGame(false, false, "localhost", 4000);
		
		serverC.enableGUI();
		serverC.disableGUI();
		serverC.isGUIDisabled();
		
		serverC.handleMessage(new Connect4MoveMessage(1, 0, Connect4MoveMessage.RED));
		clientC.handleMessage(new Connect4MoveMessage(1, 2, Connect4MoveMessage.YELLOW));
		
		clientC.computerTurn();
		for (int i = 0; i < 3; i++) {
			serverC.humanTurn(0);
		}
		serverC.declareWinner();
		
		serverC.closeConnections();
		clientC.closeConnections();
	}
	
	/**
	 * Utility function to build board filled with 
	 * whatever value is passed in (null or valid ids)
	 * 
	 * @param val integer to be placed inside board
	 * @return Generated board
	 */
	public List<List<Integer>> buildBoard(Integer val) {
		List<List<Integer>> board = new ArrayList<List<Integer>>();
		board = new ArrayList<List<Integer>>();
		for (int i = 0; i < Connect4Controller.COLUMNS; i ++) {
			List<Integer> column = new ArrayList<Integer>();
			for (int j = 0; j < Connect4Controller.ROWS; j ++) {
				column.add(val);
			}
			board.add(column);
		}
		return board;
	}
	
	/**
	 * Utility function to build winning board via 
	 * tokens placed in sequence in a row.
	 * 
	 * @return Generated board
	 */
	public List<List<Integer>> buildRowWinBoard() {
		List<List<Integer>> board = buildBoard(null);
		board.get(0).set(0, Connect4MoveMessage.YELLOW);
		board.get(1).set(0, Connect4MoveMessage.YELLOW);
		board.get(2).set(0, Connect4MoveMessage.YELLOW);
		board.get(3).set(0, Connect4MoveMessage.YELLOW);
		return board;
	}
	
	/**
	 * Utility function to build winning board via 
	 * tokens placed in sequence in a col.
	 * 
	 * @return Generated board
	 */
	public List<List<Integer>> buildColWinBoard() {
		List<List<Integer>> board = buildBoard(null);
		board.get(0).set(0, Connect4MoveMessage.YELLOW);
		board.get(0).set(1, Connect4MoveMessage.YELLOW);
		board.get(0).set(2, Connect4MoveMessage.YELLOW);
		board.get(0).set(3, Connect4MoveMessage.YELLOW);
		return board;
	}
}
