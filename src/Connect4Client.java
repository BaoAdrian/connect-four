import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

public class Connect4Client {
	// Fields
	private Socket server;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String host;
	private int port;
	
	// Constructor
	public Connect4Client(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	
	// Returns true if a successful connection was established.
	public boolean connect(){
		try {
			server = new Socket(host, port);
			output = new ObjectOutputStream(server.getOutputStream());
			input = new ObjectInputStream(server.getInputStream());
			return true;
		} catch (ConnectException e) {
			System.out.println("Error, server not initialized yet.");
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
		try {
			output.writeObject(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeServer() {
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
