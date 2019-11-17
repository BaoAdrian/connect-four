import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class Connect4Test {
	
	@Test
	public void testModelInitialization() {
		Connect4Controller controller = new Connect4Controller(new Connect4Model());
	}
	
	@Test
	public void testGameOverDetection() {
		List<List<Integer>> emptyBoard = buildBoard(null);
		Connect4Controller controllerOne = new Connect4Controller(emptyBoard);
		assertFalse(controllerOne.checkIfGameOver());
		
		List<List<Integer>> winningBoard = buildBoard(Connect4MoveMessage.YELLOW);
		Connect4Controller controllerTwo = new Connect4Controller(winningBoard);
		assertTrue(controllerTwo.checkIfGameOver());
	}
	
	@Test
	public void testRowWinDetection() {
		List<List<Integer>> emptyBoard = buildBoard(null);
		Connect4Controller controllerOne = new Connect4Controller(emptyBoard);
		assertFalse(controllerOne.checkIfGameOver());
		
		List<List<Integer>> winningBoard = buildRowWinBoard();
		Connect4Controller controllerTwo = new Connect4Controller(winningBoard);
		assertTrue(controllerTwo.checkIfGameOver());
	}
	
	@Test
	public void testColWinDetection() {
		List<List<Integer>> emptyBoard = buildBoard(null);
		Connect4Controller controllerOne = new Connect4Controller(emptyBoard);
		assertFalse(controllerOne.checkIfGameOver());
		
		List<List<Integer>> winningBoard = buildColWinBoard();
		Connect4Controller controllerTwo = new Connect4Controller(winningBoard);
		assertTrue(controllerTwo.checkIfGameOver());
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
		assertFalse(cOne.checkIfGameOver());
		
		// Case 2 - Right-directed Diagonal (@ column 0)
		List<List<Integer>> rdOne = buildBoard(null);
		rdOne.get(0).set(0, Connect4MoveMessage.YELLOW);
		rdOne.get(1).set(1, Connect4MoveMessage.YELLOW);
		rdOne.get(2).set(2, Connect4MoveMessage.YELLOW);
		rdOne.get(3).set(3, Connect4MoveMessage.YELLOW);
		Connect4Controller cTwo = new Connect4Controller(rdOne);
		assertTrue(cTwo.checkIfGameOver());
		
		// Case 3 - Right-directed Diagonal (elsewhere)
		List<List<Integer>> rdTwo = buildBoard(null);
		rdTwo.get(2).set(0, Connect4MoveMessage.YELLOW);
		rdTwo.get(3).set(1, Connect4MoveMessage.YELLOW);
		rdTwo.get(4).set(2, Connect4MoveMessage.YELLOW);
		rdTwo.get(5).set(3, Connect4MoveMessage.YELLOW);
		Connect4Controller cThree = new Connect4Controller(rdTwo);
		assertTrue(cThree.checkIfGameOver());
		
		// Case 4 - Left-directed Diagonal (@ column 6)
		List<List<Integer>> ldOne = buildBoard(null);
		ldOne.get(6).set(0, Connect4MoveMessage.YELLOW);
		ldOne.get(5).set(1, Connect4MoveMessage.YELLOW);
		ldOne.get(4).set(2, Connect4MoveMessage.YELLOW);
		ldOne.get(3).set(3, Connect4MoveMessage.YELLOW);
		Connect4Controller cFour = new Connect4Controller(ldOne);
		assertTrue(cFour.checkIfGameOver());
		
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
		controller.declareWinner();
	}
	
	@Test
	public void testComputerTurn() {
		List<List<Integer>> board = buildBoard(null);
		Connect4Controller controller = new Connect4Controller(board);
		int max = Connect4Controller.ROWS * Connect4Controller.COLUMNS + 1;
		for(int i = 0; i < max; i++) {
			controller.computerTurn();
		}
	}
	
	@Test
	public void testHumanTurn() {
		List<List<Integer>> board = buildBoard(null);
		Connect4Controller controller = new Connect4Controller(board);
		// Place tokens (attempt extra placements) at column 0
		for (int i = 0; i < 8; i++) {
			controller.humanTurn(0);
		}
	}
	
	@Test
	public void testMoveMessage() {
		Connect4MoveMessage message = new Connect4MoveMessage(0, 1, Connect4MoveMessage.RED);
		assertEquals(message.getRow(), 0);
		assertNotEquals(message.getRow(), -1);
		assertEquals(message.getColumn(), 1);
		assertNotEquals(message.getColumn(), -1);
		assertEquals(message.getColor(), Connect4MoveMessage.RED);
		assertNotEquals(message.getColor(), Connect4MoveMessage.YELLOW);		
	}
	
	/**
	 * Utility function to build board filled with 
	 * whatever value is passed in (null or valid ids)
	 * 
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
