/**
 * @author Mauricio Herrera, Adrian Bao
 * 
 * CSC 335 - Object Oriented Programming and Design
 * 
 * Title: Networked Connect 4
 * 
 * Description: This class launches the GUI for the Connect 4 game.
 */
import javafx.application.Application;

public class Connect4 {

	/**
	 * This method initializes view class.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		Application.launch(Connect4View.class, args);
	}

}
