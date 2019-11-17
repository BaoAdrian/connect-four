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
	
	// Constructor
	public Connect4Server (int port) {
		try {
			server = new ServerSocket(port);
			connection = server.accept();
			output = new ObjectOutputStream(connection.getOutputStream());
			input = new ObjectInputStream(connection.getInputStream());
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	
	
	
}
