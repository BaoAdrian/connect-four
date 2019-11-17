import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Connect4Server {
	// Class constants
	public static final String DEFAULT_HOST = "localhost";
	public static final int DEFAULT_PORT = 4000;
	
	// Fields
	private ServerSocket server;
	private Socket connection;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	// Constructor
	public Connect4Server () {
		try {
			server = new ServerSocket(DEFAULT_PORT);
			connection = server.accept();
			output = new ObjectOutputStream(connection.getOutputStream());
			input = new ObjectInputStream(connection.getInputStream());
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	
}
