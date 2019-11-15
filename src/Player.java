
public class Player {
	// Fields
	private static int currId = 1;
	private int id;
	
	// Constructor
	public Player() {
		this.id = currId;
		currId ++;
	}
	
	private int getId() {
		return id;
	}
	
	public boolean equals(Object other) {
		if (other instanceof Player) {
			if (((Player) other).getId() == id) {
				return true;
			}
			return false;
		}
		return false;
	}
}
