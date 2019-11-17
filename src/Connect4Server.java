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
//			connection = server.accept();
//			output = new ObjectOutputStream(connection.getOutputStream());
//			input = new ObjectInputStream(connection.getInputStream());
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	
	private class RunnableConnection implements Runnable {

		@Override
		public void run() {
			try {
				connection = server.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	
}
