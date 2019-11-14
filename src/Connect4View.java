/**
 * @author Mauricio Herrera, Adrian Bao
 * 
 */

import javafx.application.Application;
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
	private Stage primaryStage;
	private GridPane gridPane;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
	}
	
	private void setupStage() {
		
	}

}
