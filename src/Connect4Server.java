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
//			input = new ObjectInputStream(connection.getInputStream());
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	// Add a throws null pointer exception and check for it in case client has not connected
	public void sendMessage(Connect4MoveMessage message){
		System.out.println(message);
		RunnableSendMessage runnableMsg = new RunnableSendMessage(message);
		Thread messageThread = new Thread(runnableMsg);
		messageThread.start();
	}
	
	
	public void waitForMessage() {
		Thread incomingMessage = new Thread(new RunnableGetMessage());
		incomingMessage.start();
	}
	
	private class RunnableGetMessage implements Runnable {
		@Override
		public void run() {
			try {
				input = new ObjectInputStream(connection.getInputStream());
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
//				output = new ObjectOutputStream(connection.getOutputStream());
				output.writeObject(message);
//				output.close();
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
				output = new ObjectOutputStream(connection.getOutputStream());
				controller.enableGUI();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	
}
