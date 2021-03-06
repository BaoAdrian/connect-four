/**
 * @author Mauricio Herrera, Adrian Bao
 * 
 * CSC 335 - Object Oriented Programming and Design
 * 
 * Title: Networked Connect 4
 * 
 * File: Connect4Server.java
 * 
 * Description: This class creates a server to be used to communicate
 * between players of Connect4. 
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Connect4Server {
	
	// Fields
	private ServerSocket server;
	private Socket connection;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Connect4Controller controller;
	private Connect4MoveMessage message;
	
	// Constructor
	/**
	 * Instantiates a server object and creates a connection to the client
	 * @param port port number to be used for ServerSocket
	 * @param controller Controller instance of the game
	 * @throws IOException in the case where a server instance is already running
	 * in the same host
	 */
	public Connect4Server (int port, Connect4Controller controller) throws IOException {
		this.controller = controller;
		server = new ServerSocket(port);
		Thread connectionThread = new Thread(new RunnableConnection());
		connectionThread.start();
		message = null;
	}
	
	/**
	 * This method transmits a message to the client by using a new
	 * thread.
	 * @param message Connect4MoveMessage instance to be sent
	 */
	public void sendMessage(Connect4MoveMessage message){
		System.out.println("sent: " + message);
		RunnableSendMessage runnableMsg = new RunnableSendMessage(message);
		Thread messageThread = new Thread(runnableMsg);
		messageThread.start();
	}
	
	/**
	 * This method creates a new thread to wait for a new move message to
	 * be transmitted by the client. After that, it calls for the 
	 * controller to handle the move.
	 */
	public void waitForMessage() {
		Thread incomingMessage = new Thread(new RunnableGetMessage());
		incomingMessage.start();
	}
	
	/**
	 * This method closes the server socket.
	 */
	public void closeConnection() {
		try {
			output.close();
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
		 * This method obtains a message from the client and passes it 
		 * over to the controller to handle the move.
		 */
		@Override
		public void run() {
			try {
				input = new ObjectInputStream(connection.getInputStream());
				message = (Connect4MoveMessage)input.readObject();
				System.out.println("received: " + message);
				Platform.runLater(() -> controller.handleMessage(message));
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
		// class fields
		private Connect4MoveMessage message;
		
		// Constructor
		public RunnableSendMessage(Connect4MoveMessage message) {
			this.message = message;
		}
		
		/**
		 * This method sends a move message to the client.
		 */
		@Override
		public void run() {
			try {
				output = new ObjectOutputStream(connection.getOutputStream());
				output.writeObject(message);
//				output.close();
			} catch (IOException e) {
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
	private class RunnableConnection implements Runnable {
		/**
		 * This method accepts a connection to the client.
		 */
		@Override
		public void run() {
			try {
				connection = server.accept();
				controller.enableGUI();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	
}
