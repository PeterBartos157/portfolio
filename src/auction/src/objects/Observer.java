package objects;

import java.io.IOException;
import java.sql.Connection;

public interface Observer {
	
	public void inform(int ID, String login, double newBid, double CurrentPrice, Connection con) throws IOException;
	
}
