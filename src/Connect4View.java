/**
 * @author Mauricio Herrera, Adrian Bao
 * 
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;

public class Connect4View extends Application {
	
	// Class variables
	private static final int ROWS = 6;
	private static final int COLUMNS = 7;
	private static final float DEFAULT_GAP = 8.0f;
	private static final float CIRCLE_RADIUS = 20.0f;
	private static final Circle DEFAULT_CIRCLE = new Circle(CIRCLE_RADIUS); // Clones can be used later on
	
	// Calculated detectable range for a single column
	private static final int EXTREME_RANGE = (int) ((2*CIRCLE_RADIUS) + DEFAULT_GAP + (DEFAULT_GAP / 2)); // 52 pixels
	private static final int INNER_RANGE = (int) ((2*CIRCLE_RADIUS) + (2 * DEFAULT_GAP / 2)); // 48 pixels
	
	private Stage primaryStage;
	private GridPane gridPane;
	private double maxRange;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		setupStage();
		this.primaryStage.show();
		
		// Grab size
		this.maxRange = this.primaryStage.getWidth();
		System.out.println(maxRange);
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
		
		// Setup GridPane Object
		gridPane = new GridPane();
		gridPane.setStyle("-fx-background-color: #0000FF;");
		gridPane.setHgap(DEFAULT_GAP);
		gridPane.setVgap(DEFAULT_GAP);
		gridPane.setPadding(new Insets(DEFAULT_GAP,DEFAULT_GAP,DEFAULT_GAP,DEFAULT_GAP));
		
		// Set event for gridPane
		gridPane.setOnMouseClicked( e -> {
			// Pixel calculations
			System.out.println("getX(): " + e.getX()); // stub
			
			int targetColumn;
			int xPos = (int)e.getX();
			if (xPos <= EXTREME_RANGE) {
				// 52 pixel range
				targetColumn = (int)(xPos / EXTREME_RANGE);
			} else {
				// 48 pixel range
				targetColumn = (int)(xPos / INNER_RANGE);
			}
			System.out.println(targetColumn);
		});
		
		// Initialize each cell with a WHITE Circle Object
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLUMNS; col++) {
				Circle circle = new Circle(CIRCLE_RADIUS);
				circle.setFill(Color.WHITE);
				gridPane.add(circle, col, row);
			}
		}
		
		// Add GridPane to Scene & set Scene for Stage
		Scene scene = new Scene(gridPane);
		primaryStage.setScene(scene);
		
	}

}
