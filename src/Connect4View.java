/**
 * @author Mauricio Herrera, Adrian Bao
 * 
 * CSC 335 - Object Oriented Programming and Design
 * 
 * Title: Networked Connect 4
 * 
 * File: Connect4View.java
 * 
 * Description: This class launches the GUI for the Connect 4 game, and
 * creates handlers to add new tokens to board. This class also acts as
 * an Observer and is handles updates to the Model in the update method.
 */

import java.util.Observable;
import java.util.Set;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;

public class Connect4View extends Application implements java.util.Observer {
	
	// Class variables
	private static final float DEFAULT_GAP = 8.0f;
	private static final float CIRCLE_RADIUS = 20.0f;
	
	// Calculated detectable range for a single column
	private static final int OUTER_RANGE = (int) ((2*CIRCLE_RADIUS) + DEFAULT_GAP + (DEFAULT_GAP / 2)); // 52 pixels
	private static final int INNER_RANGE = (int) ((2*CIRCLE_RADIUS) + DEFAULT_GAP); // 48 pixels
	
	private Stage primaryStage;
	private GridPane gridPane;
	private double sceneWidth;
	private boolean gameExists;
	
	private Connect4Controller controller;
	private Connect4Model model;

	/**
	 * Method that initiates the Connect 4 Game
	 * called when launching the program.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		
		gameExists = false;
		model = new Connect4Model();
		controller = new Connect4Controller(model);
		model.addObserver(this);
		
		setupStage();
		this.primaryStage.show();
		this.sceneWidth = this.primaryStage.getWidth();
	}
	
	/**
	 * Performs preliminary setup of the primary stage for the 
	 * GUI Application
	 * 
	 * Instantiates a GridPane object and adds the game elements
	 * to the board in their corresponding positions. Sets the 
	 * gridPane event handler to detect the x-location templated
	 * to handle the corresponding events.
	 */
	private void setupStage() {
		createGridPane();
		// Creating MenuBar
		MenuBar menuBar = new MenuBar();
		final Menu fileMenu = new Menu("File");
		menuBar.getMenus().add(fileMenu);
		MenuItem newGameItem = new MenuItem("New Game");
		// Add new game functionality
		newGameItem.setOnAction((e) -> {
			// Add NetworkSetup call
			Connect4NetworkSetup dialog = new Connect4NetworkSetup();
			
			if (!dialog.isCancelled()) {
				String networkingRole = dialog.getNetworkingRole();
				String playerRole = dialog.getPlayerRole();
				String host = dialog.getHost();
				int port = dialog.getPort();
				
				controller.createGame(networkingRole.equals("server"), 
						playerRole.equals("human"), host, port);
				gameExists = true;
				createGridPane();
			}
		});
		fileMenu.getItems().add(newGameItem);
		
		VBox vBox = new VBox();
		vBox.getChildren().add(menuBar);
		vBox.getChildren().add(gridPane);
		
		// Add vBox to Scene & set Scene for Stage
		Scene scene = new Scene(vBox);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Connect 4");
		
	}
	
	/**
	 * Sets up a GridPane object with the corresponding 
	 * components and handlers.
	 */
	private void createGridPane() {
		gridPane = new GridPane();
		gridPane.setStyle("-fx-background-color: #0000FF;");
		gridPane.setHgap(DEFAULT_GAP);
		gridPane.setVgap(DEFAULT_GAP);
		gridPane.setPadding(new Insets(DEFAULT_GAP, DEFAULT_GAP, DEFAULT_GAP, DEFAULT_GAP));
		
		// Handler for GridPane
		gridPane.setOnMouseClicked( e -> {
			// Calculate selected column based on event location
			int targetColumn;
			int xPos = (int)e.getX();
			if (xPos <= OUTER_RANGE) {
				targetColumn = 0;
			} else if (xPos >= sceneWidth - OUTER_RANGE) {
				targetColumn = 6;
			} else {
				targetColumn = (int)((xPos - 4) / INNER_RANGE);
			}
			
			if (gameExists && !controller.isGUIDisabled()) {
				// Pass to model the column requested by user
				controller.humanTurn(targetColumn);
				
//				Thread turnThread = new Thread(new Runnable() {
//
//					@Override
//					public void run() {
//						controller.humanTurn(targetColumn);
//						
//					}
//					
//				});
//				turnThread.start();
			}
//			controller.humanTurn(targetColumn);
//			System.out.println(targetColumn);
//			System.out.println(gameExists);
//			System.out.println(controller.isGUIDisabled());
		});
		
		// Initialize each cell with a WHITE Circle Object
		for (int row = 0; row < Connect4Controller.ROWS; row++) {
			for (int col = 0; col < Connect4Controller.COLUMNS; col++) {
				Circle circle = new Circle(CIRCLE_RADIUS);
				circle.setFill(Color.WHITE);
				gridPane.add(circle, col, row);
//				circle.setOnMouseClicked(e -> {
//					circle.setFill(Color.RED);
//					System.out.println("column: " + GridPane.getColumnIndex(circle));
//					System.out.println("row: " + GridPane.getRowIndex(circle));
//				});
			}
		}
	}

	/**
	 * Method called by the Observable to update the Observers
	 * with the corresponding parameters. If an int[] is provided, 
	 * parses the row,col coordinates to insert a token of a 
	 * given color into the board.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Connect4MoveMessage) {
			Connect4MoveMessage message = (Connect4MoveMessage)arg;
			int row = Connect4Controller.ROWS - message.getRow() - 1;
			int col = message.getColumn();
			int color = message.getColor();
//			if (color == Connect4MoveMessage.YELLOW) {
//				Platform.runLater(new TokenRunnable(Color.YELLOW, row, col));
//			} else {
//				Platform.runLater(new TokenRunnable(Color.RED, row, col));
//			}
//			System.out.println(gridpa)
//			int[] args = (int[])arg;
//			System.out.println(args[0]);
//			System.out.println(args[1]);
//			System.out.println(args[2]);

			// Pull adjusted placement onto GridPane
//			int rowPlacement = Connect4Controller.ROWS - ((int[])arg)[1] - 1;
//			int colPlacement = ((int[])arg)[0];

			for (Node child : gridPane.getChildren()) {
				// If matching row,col -> Update with corresponding color
				if (GridPane.getRowIndex(child).equals(row) 
						&& GridPane.getColumnIndex(child).equals(col)) {
					if (color == Connect4MoveMessage.YELLOW) {
//						((Circle)child).setFill(Color.YELLOW);
						System.out.println("YELLOW");
//						Platform.runLater(new TokenRunnable((Circle)child, Color.YELLOW, row, col));
						Platform.runLater(() -> ((Circle)child).setFill(Color.YELLOW));
					} else {
						System.out.println("RED");
//						((Circle)child).setFill(Color.RED);
//						Platform.runLater(new TokenRunnable((Circle)child, Color.RED, row, col));
						Platform.runLater(() -> ((Circle)child).setFill(Color.RED));
					}
					System.out.println(((Circle)child).getFill());
					System.out.println("VIEW::::    " + Thread.currentThread().getName());
				}
			
			}
			
			// After each update, check if game is over
			if (controller.checkIfGameOver()) {
				controller.declareWinner();
			}
			
		} else if (arg instanceof Integer) {
			gridPane.setDisable(true);
			
			// Holds logic for detecting when game is over
			if (((Integer)arg) != -1) {
				Alert winningAlert = new Alert(AlertType.INFORMATION);
				winningAlert.setContentText("You Win!");
				winningAlert.showAndWait();
				gameExists = false;
			} else {
				Alert losingAlert = new Alert(AlertType.INFORMATION);
				losingAlert.setContentText("You Lose!");
				losingAlert.showAndWait();
				gameExists = false;
			}
		}
		
		
	}
	
//	private class TokenRunnable implements Runnable {
//		private Color color;
//		private int row;
//		private int col;
//		
//		public TokenRunnable(Color color, int row, int col) {
//			this.color = color;
//			this.row = row;
//			this.col = col;
//		}
//
//		@Override
//		public void run() {
//			for (Node child : gridPane.getChildren()) {
//				// If matching row,col -> Update with corresponding color
//				if (GridPane.getRowIndex(child).equals(row) 
//						&& GridPane.getColumnIndex(child).equals(col)) {
//					((Circle)child).setFill(color);
//					System.out.println(((Circle)child).getFill());
//					System.out.println("THREAD::::    " + Thread.currentThread().getName());
//				}
//			}
////			token.setFill(color);
//		}
//		
//	}
}
