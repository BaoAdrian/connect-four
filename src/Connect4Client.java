import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
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
	
	// Constructor
	public Connect4Client(String host, int port, Connect4Controller controller) {
		this.controller = controller;
		this.host = host;
		this.port = port;
	}
	
	
	// Returns true if a successful connection was established.
	public boolean connect(){
		try {
			server = new Socket(host, port);
//			output = new ObjectOutputStream(server.getOutputStream());
//			input = new ObjectInputStream(server.getInputStream());
			return true;
		} catch (NoRouteToHostException e) {
			System.out.println("Error, server not initialized yet.");
			Alert noServerFoundAlert = new Alert(AlertType.WARNING);
			noServerFoundAlert.setContentText("Server not found! Please wait for server to start, and try again.");
			noServerFoundAlert.showAndWait();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isConnected() {
		return server != null;
	}
	
	public void sendMessage(Connect4MoveMessage message) {
		RunnableSendMessage runnableMsg = new RunnableSendMessage(message);
		Thread messageThread = new Thread(runnableMsg);
		messageThread.start();
	}
	
	public void waitForMessage() {
		Thread incomingMessage = new Thread(new RunnableGetMessage());
		incomingMessage.start();
	}
	
	public void closeServer() {
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class RunnableGetMessage implements Runnable {
		@Override
		public void run() {
			try {
				input = new ObjectInputStream(server.getInputStream());
				Connect4MoveMessage message = (Connect4MoveMessage)input.readObject();
				controller.handleMessage(message);
				input.close();
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private class RunnableSendMessage implements Runnable {
		private Connect4MoveMessage message;
		
		public RunnableSendMessage(Connect4MoveMessage message) {
			this.message = message;
		}
		@Override
		public void run() {
			try {
				output = new ObjectOutputStream(server.getOutputStream());
				output.writeObject(message);
				output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
