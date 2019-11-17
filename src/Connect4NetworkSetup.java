

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Connect4NetworkSetup extends Stage {

	private Stage stage;
	private String hostInfo;
	private int portInfo;
	private String networkingRole; // Client vs. Server
	private String playerRole; // Human vs. Computer
	
	public Connect4NetworkSetup() {
		setupNetworkDialog();
	}
	
	/**
	 * Generates the Network Setup dialog box
	 * 
	 * Sets up the formatted dialog box to retrieve
	 * the host/port/role information for the game and
	 * handles it accordingly.
	 */
	public void setupNetworkDialog() {
		this.stage = new Stage();
		stage.setTitle("Network Setup");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.centerOnScreen();
		
		VBox vbox = new VBox();
		HBox createHBox = generateHBox("Create: ", "Server", "Client");
		HBox playAsHBox = generateHBox("Play as: ", "Human", "Computer");
		HBox networkHBox = generateNetworkHBox();
		Button okBtn = new Button("OK");
		Button cancelBtn = new Button("Cancel");
		HBox buttonBox = new HBox(10); // Spacing of 10
		buttonBox.getChildren().addAll(okBtn, cancelBtn);
		buttonBox.setPadding(new Insets(10,10,10,10));
		
		// Handlers
		okBtn.setOnAction( e -> {
			// Server vs. Client
			for (Node node : createHBox.getChildren()) {
				if (node instanceof CheckBox) {
					if (((CheckBox)node).isSelected()) {
						if (((CheckBox) node).getText().equals("Server")) {
							this.networkingRole = "server";
							System.out.println("Networking Role: " + this.networkingRole);
						} else {
							this.networkingRole = "client";
							System.out.println("Networking Role: " + this.networkingRole);
						}
					}
				}
			}
			
			for (Node node : playAsHBox.getChildren()) {
				if (node instanceof CheckBox) {
					if (((CheckBox)node).isSelected()) {
						if (((CheckBox) node).getText().equals("Human")) {
							this.playerRole = "human";
							System.out.println("Player Role: " + this.playerRole);
						} else {
							this.playerRole = "computer";
							System.out.println("Player Role: " + this.playerRole);
						}
					}
				}
			}
					
			boolean isServerTF = true;
			for (Node node : networkHBox.getChildren()) {
				if (node instanceof TextField) {
					if (isServerTF) {
						this.hostInfo = ((TextField)node).getText();
					} else {
						this.portInfo = Integer.valueOf(((TextField)node).getText());
					}
				}
			}

		});
		
		// Cancel button simply closes the dialog box
		cancelBtn.setOnAction( e -> {
			stage.close();
		});

		vbox.getChildren().addAll(createHBox, playAsHBox, networkHBox, buttonBox);		
		Scene scene = new Scene(vbox);
		stage.setScene(scene);
		stage.showAndWait();
	}
	
	/**
	 * Generates an HBox consisting of a Label and two CheckBox
	 * objects with given strings. 
	 * 
	 * @param labelText	String to be set as the Label's text
	 * @param leftBoxText String to be set as the CheckBox's text
	 * @param rightBoxText String to be set as the CheckBox's text
	 * @return Generated HBox object with the Label/CheckBox Objects
	 */
	public HBox generateHBox(String labelText, String leftBoxText, String rightBoxText) {
		HBox hbox = new HBox(20);
		Label label = new Label(labelText);
		CheckBox leftBox = new CheckBox(leftBoxText);
		CheckBox rightBox = new CheckBox(rightBoxText);
		
		// HBox formatting
		hbox.setPrefWidth(300);
		hbox.setPrefHeight(30);
		hbox.setPadding(new Insets(10,10,10,10));
		
		// Handlers
		leftBox.setOnAction( e -> {
			if (!leftBox.isSelected()) {
				// If selected, simply revert
				leftBox.setSelected(false);
			} else {
				leftBox.setSelected(true);
				// If selected and clientBox is checked, swap them
				if (rightBox.isSelected()) {
					rightBox.setSelected(false);
				}
				
			}
		});
		
		rightBox.setOnAction( e -> {
			if (!rightBox.isSelected()) {
				rightBox.setSelected(false);
			} else {
				rightBox.setSelected(true);
				if (leftBox.isSelected()) {
					leftBox.setSelected(false);				
				}
			}
		});
		
		leftBox.setSelected(true);
		hbox.getChildren().addAll(label, leftBox, rightBox);
		return hbox;
	}
	
	/**
	 * Generates HBox containing the TextFields for 
	 * Networking Information. Retrieves the server/host &
	 * the port number from the user to setup Server/Client.
	 * 
	 * @return HBox containing Label/TextField pairs for Host/Port
	 */
	public HBox generateNetworkHBox() {
		HBox hbox = new HBox(10);
		Label serverLabel = new Label("Server");
		TextField serverTF = new TextField();
		Label portLabel = new Label("Port");
		TextField portTF = new TextField();
		
		// HBox Formatting
		hbox.setPrefWidth(450);
		hbox.setPrefHeight(30);
		hbox.setPadding(new Insets(10,10,10,10));
		
		hbox.getChildren().addAll(serverLabel, serverTF, portLabel, portTF);
		
		return hbox;
	}
	
	/**
	 * Public Accessor for hostInfo
	 * 
	 * @return String value of hostInfo
	 */
	public String getHost() {
		return this.hostInfo;
	}
	
	/**
	 * Public Accessor for portInfo
	 *
	 * @return String value of portInfo
	 */
	public int getPort() {
		return this.portInfo;
	}
	
	/**
	 * Public Accessor for networkingRole
	 * 
	 * @return String value of networkingRole
	 */
	public String getNetworkingRole() {
		return this.networkingRole;
	}
	
	/**
	 * Public Accessor for playerRole
	 * 
	 * @return String value of playerRole
	 */
	public String getPlayerRole() {
		return this.playerRole;
	}
	

}
