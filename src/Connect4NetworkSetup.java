

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
	
	public Connect4NetworkSetup() {
		setupNetworkDialog();
	}
	
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
							// Instantiate server 
							// HERE
							System.out.println("Instantiate Server");
						} else {
							// Instantiate client 
							// HERE
							System.out.println("Instantiate Client");
						}
					}
				}
			}
					
			boolean isFirstTF = true;
			for (Node node : networkHBox.getChildren()) {
				if (node instanceof TextField) {
					if (isFirstTF) {
						System.out.println("Server: " + ((TextField)node).getText());
					} else {
						System.out.println("Port: " + ((TextField)node).getText());
					}
				}
			}

		});
		
		cancelBtn.setOnAction( e -> {
			stage.close();
		});
		
		vbox.getChildren().addAll(createHBox, playAsHBox, networkHBox, buttonBox);
		
		Scene scene = new Scene(vbox);
		stage.setScene(scene);
		
		stage.showAndWait();
	}
	
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
	

}
