import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Connect4Server {
	
	// Fields
	private ServerSocket server;
	private Socket connection;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Connect4Controller controller;
	
	// Constructor
	public Connect4Server (int port, Connect4Controller controller) {
		try {
			this.controller = controller;
			server = new ServerSocket(port);
			Thread connectionThread = new Thread(new RunnableConnection());
			connectionThread.start();
//			output = new ObjectOutputStream(connection.getOutputStream());
//			input = new ObjectInputStream(connection.getInputStream());
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void sendMessage(Connect4MoveMessage message) {
		RunnableMessage runnableMsg = new RunnableMessage(message);
		Thread messageThread = new Thread(runnableMsg);
		messageThread.start();
	}
	
	
	public Connect4MoveMessage waitForMessage() {
		
	}
	
	private class RunnableMessage implements Runnable {
		private Connect4MoveMessage message;
		
		public RunnableMessage(Connect4MoveMessage message) {
			this.message = message;
		}
		@Override
		public void run() {
			try {
				output = new ObjectOutputStream(connection.getOutputStream());
				output.writeObject(message);
				output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private class RunnableConnection implements Runnable {

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
