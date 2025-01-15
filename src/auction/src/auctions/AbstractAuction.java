package auctions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import objects.Buyer;
import objects.Item;
import objects.Seller;
import objects.User;

public interface AbstractAuction {
	
	public List<Auctions> getAuctions();
	
	public List<User> getUsers();
	
	public List<Seller> getSellers();
	
	public List<Buyer> getBuyers();
	
	public List<Item> getItems();
	
	public void setAuctions(List<Auctions> auctions);
	
	public void setUsers(List<User> users);
	
	public void setSellers(List<Seller> sellers);
	
	public void setBuyers(List<Buyer> buyers);
	
	public void setItems(List<Item> items);
	
	public int UserLogin(String login, String password, Connection con) throws SQLException;
	
	public int UserRegister(String fname, String lname, String login, String password, String email, Connection con) throws SQLException;
	
	public int SellerSell(Date endDate, String login, String auctionName, String itemName, String itemDescription, int itemID, double itemBasePrice, Connection con) throws ClassNotFoundException, IOException, SQLException;
	
	public int closeAuction(String login, int ID, Connection con) throws ClassNotFoundException, IOException, SQLException;
	
	public int cancelAuction(String login, int ID) throws ClassNotFoundException, IOException;
	
	public Auctions searchAuction(int ID);
	
	public int buyItem(String login, int  ID, Connection con) throws SQLException, ClassNotFoundException, IOException;
	
	public int bidOnAuction(String login, int ID, double bid, Connection con) throws ClassNotFoundException, IOException;
	
	public int WithdrawBid(String login, int ID);
}
