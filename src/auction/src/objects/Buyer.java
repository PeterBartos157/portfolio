package objects;

import auctions.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@SuppressWarnings("serial")
public class Buyer extends User {
	
	private List<Auctions> boughtAuctions;
	
	public Buyer () {	//Polymorphism
		super();
	}
	
	public Buyer (String fname, String lname, String login, int password, String email) {
		super(fname, lname, login, password, email);
	}
	
	public int Buy(Hashtable<String, Seller> sellers, Hashtable<String, Buyer> buyers, Hashtable<String, User> users, Auctions buyAuction, double price, Connection con, Bid thisBid) throws SQLException {
		if (boughtAuctions == null)
			boughtAuctions = new ArrayList<>();
		if (sellers == null)
			return -5;
		
		Seller seller = buyAuction.getSeller();
		Auctions sauction = seller.getAuctionsHT().get(buyAuction.getID());
		if (buyAuction.getID() == sauction.getID()) {	//gets an auction that is the same with seller auction
			User user = users.get(seller.getLogin());
			if (seller.getLogin().compareTo(user.getLogin()) == 0) {	//gets seller
				sauction.getBidHistoryHT().put(buyAuction.getBuyer().getLogin(), thisBid);	//puts bid
				sauction.setCurrentBidder(buyAuction.getBuyer().getLogin());	//sets current bidder
				sauction.setRunning(false);	//closes auction
				Date date = new Date();
				sauction.setEndDate(date);	//sets end date
				sauction.getItem().setBoughtState(true);	//sets bought state
				sauction.setBuyer(buyAuction.getBuyer());	//sets buyer
				sauction.setBuyername(buyAuction.getBuyer().getLogin());	//sets buyername
				this.addBoughtAuction(buyAuction);	//adds to bought auction list
				user.addFunds(price);	//adds funds to seller
					
				Statement stmnt = con.createStatement();
				String cmd = "SELECT * FROM USERS WHERE Login LIKE '" + seller.getLogin() + "'";
			    ResultSet rs = stmnt.executeQuery(cmd);
			    	
			    while (rs.next()) {
					String dlogin = rs.getString("Login");
					if (seller.getLogin().compareTo(dlogin) == 0) {	//updates balance on MySQL of the seller
						cmd = "UPDATE users SET Balance = " + user.getBalance() + " WHERE Login like '" + seller.getLogin() + "'";
						stmnt.execute(cmd);
						stmnt.close();
						return 0;
					}
				}
			}
		}
		return -5;
	}
	
	
	public void addBoughtAuction(Auctions boughtAuction) {
		if (boughtAuctions == null)
			boughtAuctions = new ArrayList<>();
		this.boughtAuctions.add(boughtAuction);
	}
	
	public List<Auctions> getBoughtAuctions() {
		return this.boughtAuctions;
	}
	
}
