/**
 * @author Mauricio Herrera, Adrian Bao
 * 
 * CSC 335 - Object Oriented Programming and Design
 * 
 * Title: Networked Connect 4
 * 
 * File: Connect4Client.java
 * 
 * Description: This class creates a client connection and handles
 * communications between server and client.
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class Connect4Client {
	// Fields
	private Socket server;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String host;
	private int port;
	private Connect4Controller controller;
	private Connect4MoveMessage message;
	
	// Constructor
	public Connect4Client(String host, int port, Connect4Controller controller) {
		this.controller = controller;
		this.host = host;
		this.port = port;
		message = null;
	}
	
	/**
	 * This method connects to the server indicated by the host and port
	 * indicated in the constructor. If a connection cannot be established,
	 * an alert is displayed to the user.
	 */
	public void connect(){
		try {
			server = new Socket(host, port);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error, server not initialized yet.");
			Alert noServerFoundAlert = new Alert(AlertType.WARNING);
			noServerFoundAlert.setContentText("Server not found! Please wait for server to start, and try again.");
			noServerFoundAlert.showAndWait();
		}
	}
	
	/**
	 * This method transmits a Connect4MoveMessage object to the
	 * server in a new thread.
	 * @param message move message to be transmitted
	 */
	public void sendMessage(Connect4MoveMessage message) {
		RunnableSendMessage runnableMsg = new RunnableSendMessage(message);
		Thread messageThread = new Thread(runnableMsg);
		System.out.println("sent: " + message);
		messageThread.start();
	}
	
	/**
	 * This method creates a new thread to wait for a Connect4MoveMessage to 
	 * be transmitted from the server.
	 */
	public void waitForMessage() {
		Thread incomingMessage = new Thread(new RunnableGetMessage());
		incomingMessage.start();
	}
	
	/**
	 * This method closes the server socket.
	 */
	public void closeServer() {
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @author Mauricio Herrera, Adrian Bao
	 * 
	 * Description: This class adds an implementation to the run
	 * method in the Runnable class to be executed in a separate thread.
	 *
	 */
	private class RunnableGetMessage implements Runnable {
		/**
		 * This method gets a Move message transmitted by the server, 
		 * then it calls for the controller to handle the message.
		 */
		@Override
		public void run() {
			try {
				input = new ObjectInputStream(server.getInputStream());
				message = (Connect4MoveMessage)input.readObject();
				System.out.println("received: " + message);
//				Platform.runLater(new Runnable() {
//
//					@Override
//					public void run() {
//						controller.handleMessage(message);
//					}
//					
//				});
				Platform.runLater(() -> controller.handleMessage(message));
//				controller.handleMessage(message);
//				input.close();											May not need to close
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @author Mauricio Herrera, Adrian Bao
	 * 
	 * Description: This class adds an implementation to the run
	 * method in the Runnable class to be executed in a separate thread.
	 * 
	 */
	private class RunnableSendMessage implements Runnable {
		// Class fields
		private Connect4MoveMessage message;
		
		// Constructor
		public RunnableSendMessage(Connect4MoveMessage message) {
			this.message = message;
		}
		
		/**
		 * This method sends a move message to the server.
		 */
		@Override
		public void run() {
			try {
				output = new ObjectOutputStream(server.getOutputStream());
				output.writeObject(message);
//				output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
