package auctions;

import objects.*;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@SuppressWarnings("serial")
public class Auction implements Serializable, AbstractAuction {
	private List<Item> items;
	private List<User> users;
	private List<Seller> sellers;
	private List<Buyer> buyers;
	private List<Auctions> auctions;
	
	private Hashtable<String, Seller> sellersHT;
	private Hashtable<String, Buyer> buyersHT;
	private Hashtable<String, User> usersHT;
	private Hashtable<Integer, Auctions> auctionsHT;
	
	transient private User user;
	transient private Seller seller;
	transient private Buyer buyer;
	transient private Auctions auction_item;
	
	private int ListenerCurrentID;
	private int Listener;
	private UserCounter userCounter = new UserCounter();
	
	private FraudProtection Fraud = new FraudProtection();
	private Admin Admin;
	private int FraudAlert = 0;
	
	//Setters and getters
	public int getFraudAlert() {
		return FraudAlert;
	}

	public void setFraudAlert(int fraudAlert) {
		FraudAlert = fraudAlert;
	}

	public Admin getAdmin() {
		return Admin;
	}
	
	public UserCounter getUserCounter() {
		return userCounter;
	}

	public void setUserCounter(UserCounter userCounter) {
		this.userCounter = userCounter;
	}
	
	public int getListener() {
		return Listener;
	}

	public void setListener(int listener) {
		Listener = listener;
	}

	public int getListenerCurrentID() {
		return ListenerCurrentID;
	}

	public void setListenerCurrentID(int listenerCurrentID) {
		ListenerCurrentID = listenerCurrentID;
	}

	public Hashtable<Integer, Auctions> getAuctionsHT() {
		return auctionsHT;
	}

	public void createAuctionsHT() {
		auctionsHT = new Hashtable<Integer, Auctions>();
		
		if (auctions != null) {
			Iterator<Auctions> iterAuctions = auctions.iterator();
			while (iterAuctions.hasNext() == true) {
				this.auction_item = iterAuctions.next();
				this.auctionsHT.put(this.auction_item.getID(), this.auction_item);
			}
		}
	}
	
	public Hashtable<String, User> getUsersHT() {
		return usersHT;
	}

	public void createUsersHT() {
		usersHT = new Hashtable<String, User>();
		
		Iterator<User> iterUsers = users.iterator();
		while (iterUsers.hasNext() == true) {
			this.user = iterUsers.next();
			this.usersHT.put(this.user.getLogin(), this.user);
		}
	}
	
	public Hashtable<String, Buyer> getBuyersHT() {
		return buyersHT;
	}

	public void createBuyersHT() {
		buyersHT = new Hashtable<String, Buyer>();
		
		Iterator<Buyer> iterBuyers = buyers.iterator();
		while (iterBuyers.hasNext() == true) {
			this.buyer = iterBuyers.next();
			this.buyersHT.put(this.buyer.getLogin(), this.buyer);
		}
	}

	public Hashtable<String, Seller> getSellersHT() {
		return sellersHT;
	}

	public void createSellersHT() {
		sellersHT = new Hashtable<String, Seller>();
		
		Iterator<Seller> iterSellers = sellers.iterator();
		while (iterSellers.hasNext() == true) {
			this.seller = iterSellers.next();
			this.sellersHT.put(this.seller.getLogin(), this.seller);
		}
	}
	
	public List<Auctions> getAuctions() {
		return this.auctions;
	}
	
	public List<User> getUsers() {
		return this.users;
	}
	
	public List<Seller> getSellers() {
		return this.sellers;
	}
	
	public List<Buyer> getBuyers() {
		return this.buyers;
	}
	
	public List<Item> getItems() {
		return this.items;
	}
	
	public void setAuctions(List<Auctions> auctions) {
		this.auctions = auctions;
	}
	
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	public void setSellers(List<Seller> sellers) {
		this.sellers = sellers;
	}
	
	public void setBuyers(List<Buyer> buyers) {
		this.buyers = buyers;
	}
	
	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	
	
	// ------ AUCTION LOGIC ------- //
	public int UserLogin(String login, String password, Connection con) throws SQLException {	//User login
		Statement stmnt = con.createStatement();
		String cmd = "SELECT * FROM USERS WHERE Login LIKE '" + login + "'";	//Statement creation for MySQL
    	ResultSet rs = stmnt.executeQuery(cmd);
    	
		if (rs == null) {	//If there are no users then return
			stmnt.close();
			return -1;
		}
		while (rs.next()) {
			String dlogin = rs.getString("Login");	//gets login and password of user from MySQL
			int dpassword = rs.getInt("Password");
			
			if (login.compareTo(dlogin) == 0 && password.hashCode() == dpassword) {	//Compares it to input and if it matches, then it logs in the user
				stmnt.close();
				if (usersHT.get(login).isBanned() == true)
					return -2;
				else
					return 0;
			}
		}
		
		if (login.compareTo("Admin") == 0 && password.hashCode() == "Admin".hashCode() ) {	//Comparing for admin account
			if (this.Admin == null)
				this.Admin = new Admin("Admin", "Admin", "Admin", "Admin".hashCode(), "Admin");
			stmnt.close();
			return 1;
		}
		stmnt.close();
		
		return -1;
	}
	
	public int UserRegister(String fname, String lname, String login, String password, String email, Connection con) throws SQLException {	//User register
		Statement stmnt = con.createStatement();	
		String cmd = "SELECT * FROM USERS WHERE Login LIKE '" + login + "'";	//Statement creation for MySQL
    	ResultSet rs = stmnt.executeQuery(cmd);
    	
		if (rs != null) {
			while (rs.next()) {	//Program checks for collisions, if there is a same login already
				String dlogin = rs.getString("Login");
				String dmail = rs.getString("Email");
				int counter = 0, decision = 0;
				if (login.compareTo(dlogin) == 0 || login.compareTo("Admin") == 0) {
					System.out.println("Username is taken...");
					counter = -1;
					decision = 1;
				}
				if (email.compareTo(dmail) == 0 || email.compareTo("admin@eauction.com") == 0) {
					System.out.println("Email is taken...");
					counter = counter - 2;
					decision = 1;
				}
				if (decision == 1)
					return counter;
			}
		}
		
		cmd = "SELECT * FROM USERS WHERE Login LIKE '" + email + "'";
    	rs = stmnt.executeQuery(cmd);
    	
		if (rs != null) {	//Program checks for collisions, if there is a same email already
			while (rs.next()) {
				String dlogin = rs.getString("Login");
				String dmail = rs.getString("Email");
				int counter = 0, decision = 0;
				if (login.compareTo(dlogin) == 0 || login.compareTo("Admin") == 0) {
					System.out.println("Username is taken...");
					counter = -1;
					decision = 1;
				}
				if (email.compareTo(dmail) == 0 || email.compareTo("admin@eauction.com") == 0) {
					System.out.println("Email is taken...");
					counter = counter - 2;
					decision = 1;
				}
				if (decision == 1)
					return counter;
			}
		}
		
		if (users == null) {
			users = new ArrayList<>();
			buyers = new ArrayList<>();
			sellers = new ArrayList<>();
		}
		
		cmd = "INSERT INTO users VALUES ('" + login + "','" + fname + "','" + lname + "','" + email + "','" + password.hashCode() + "','0.00', 0)";	//creates user in MySQL
		stmnt.execute(cmd);
		
		user = new User(fname, lname, login, password.hashCode(), email);	//Creates user
		buyer = new Buyer(fname, lname, login, password.hashCode(), email);
		seller = new Seller(fname, lname, login, password.hashCode(), email);
		users.add(user);	//Adds to list
		buyers.add(buyer);
		sellers.add(seller);
		user = users.get(users.size() - 1);
		user.setBalance(0.00);
		if (usersHT == null) {
			usersHT = new Hashtable<String, User>();
			buyersHT = new Hashtable<String, Buyer>();
			sellersHT = new Hashtable<String, Seller>();
		}
		usersHT.put(user.getLogin(), user);	//Adds to hash tables for quicker searching
		buyersHT.put(buyer.getLogin(), buyer);
		sellersHT.put(seller.getLogin(), seller);
		stmnt.close();
		return 0;
	}
	
	//Auction registration by user
	public int SellerSell(Date endDate, String login, String auctionName, String itemName, String itemDescription, int itemID, double itemBasePrice, Connection con) throws ClassNotFoundException, IOException, SQLException {
		if (itemBasePrice < 0.01)
			return -1;
		seller = sellersHT.get(login);
		if (login.compareTo(seller.getLogin()) == 0) {
			int auctionID = (int)(Math.random() * ((999999 - 99999) + 1)) + 99999;	//random number for auction ID
			if (auctions == null) {
				auctions = new ArrayList<>();
				this.createAuctionsHT();
			}
			itemBasePrice = Math.round(itemBasePrice * 100.0) / 100.0;	//Creation of item
			Item itemA = new Item(itemName, itemDescription, (int)(Math.random() * ((9999999 - 999999) + 1)) + 999999, itemBasePrice);
			if (items == null)
				items = new ArrayList<>();
			items.add(itemA);
			Auctions auction = new Auctions(auctionID, login, auctionName, endDate, itemA, itemName, seller, itemBasePrice, 0, 0, null);	//Creation of auction
			auctions.add(auction);
			auctionsHT.put(auctionID, auction);
			seller.Sell(auction);	//calling function from seller to sell
					
			Timer timer = new Timer();	//Timer for closing the auction at given closing time
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					try {
						if (auctionsHT.get(auctionID).isRunning() == true)
							closeAuction(login, auctionID, con);
					} catch (ClassNotFoundException | IOException | SQLException e) { e.printStackTrace(); }
				}
			};
			timer.schedule(task, endDate);	//Timer waits for the task that is going to close the auction
			java.sql.Date sqlDate = new java.sql.Date(endDate.getTime());
			String cmd = "INSERT INTO auction_timer VALUES ('" + auctionID +"', '" + sqlDate + "')";	//It also gets added to MySQL database, so at the start of the program the thread can be created again
			Statement stmnt = con.createStatement();
			stmnt.execute(cmd);
			this.getAuctionsHT().put(auction.getID(), auction);
			this.save();	//saving the program info into file (serialisation)
			return 0;
		}
		return -3;
	}
	
	public List<Auctions> SellersAuctions(String login) {	//Simple function to return sellers auctions
		if (sellers == null)
			return null;
		seller = sellersHT.get(login);
		if (seller != null) {
			if (login.compareTo(seller.getLogin()) == 0) {
				return seller.getAuctionList();
			}
		}
		return null;
	}
	
	public int closeAuction(String login, int ID, Connection con) throws ClassNotFoundException, IOException, SQLException { //Auction closing
		if (sellers == null)
			return -1;
		seller = sellersHT.get(login);	//Finds seller
		if (login.compareTo(seller.getLogin()) == 0) {
			int decision = seller.closeAuction(this, ID, auctionsHT, buyersHT, login, con);	//calls the function to close the auction
			return decision;
		}
		return -2;
	}
	
	public int cancelAuction(String login, int ID) throws ClassNotFoundException, IOException {	//Cancel auction
		if (sellers == null)
			return -1;
		seller = sellersHT.get(login);	//finds the seller
		if (login.compareTo(seller.getLogin()) == 0) {
			int decision = seller.cancelAuction(this, ID, auctionsHT);	//calls the function of seller to cancel the auction
			return decision;
		}
		return -2;
	}
	
	public Auctions searchAuction(int ID) { //searches an auction
		if (auctions == null) {
			System.out.println("No auctions registered...");
			return null;
		}
		
		auction_item = auctionsHT.get(ID);	//searches for auction thanks to ID
		if (auction_item == null)
			return null;
		else if (ID == auction_item.getID())	//if the ID is matching then it returns the auction
			return auction_item;
		
		return null;
	}
	
	
	
	
	public int addFunds(String login, double addFunds, Connection con) throws SQLException {	//Adding funds to user
		Statement stmnt = con.createStatement();
		String cmd = "SELECT * FROM USERS WHERE Login LIKE '" + login + "'";	//Creating a statement for MySQL
    	ResultSet rs = stmnt.executeQuery(cmd);
		
    	if (rs == null)	//Special cases
    		return -1;
    	if (addFunds < 0.1)
    		return -2;
    	while (rs.next()) {	
    		String dlogin = rs.getString("Login");	//searches for user
    		if (login.compareTo(dlogin) == 0) {	//if user is found
    			double balance = rs.getDouble("Balance") + addFunds; //appends his balance
    			balance = (double) Math.round(balance * 100) / 100;	//rounds it up to 2 decimals
    			cmd = "UPDATE users SET Balance =" + balance + " WHERE Login = '" + dlogin + "'";	//sets it in MySQL
    			stmnt.execute(cmd);
    			user = usersHT.get(login);
    				if (login.compareTo(user.getLogin()) == 0) {
    					user.addFunds(addFunds);	//then it gets added localy
    					stmnt.close();
    					return 0;
    			}
    		}
    	}
		
		return -3;
	}
	
	public int subFunds(String login, double addFunds, Connection con) throws SQLException {	//Same as adding funds, but it removes funds
		Statement stmnt = con.createStatement();
		String cmd = "SELECT * FROM USERS WHERE Login LIKE '" + login + "'";
    	ResultSet rs = stmnt.executeQuery(cmd);
		
    	if (rs == null)
    		return -1;
    	if (addFunds > -0.1)
    		return -2;
    	while (rs.next()) {
    		String dlogin = rs.getString("Login");
    		if (login.compareTo(dlogin) == 0) {
    			double balance = rs.getDouble("Balance") + addFunds;
    			balance = (double) Math.round(balance * 100) / 100;
    			cmd = "UPDATE users SET Balance =" + balance + " WHERE Login = '" + dlogin + "'";
    			stmnt.execute(cmd);
    			user = usersHT.get(login);
    				if (login.compareTo(user.getLogin()) == 0) {
    					user.addFunds(addFunds);
    					stmnt.close();
    					return 0;
    			}
    		}
    	}
		
		return -3;
	}
	
	public double getBalance(String login, Connection con) throws SQLException { //gets the balance of user
		Statement stmnt = con.createStatement();
    	ResultSet rs = stmnt.executeQuery("SELECT * FROM USERS");	//MySQL statement to get the list
    	
    	if (rs == null) {	//special case
			System.out.println("Invalid session...");
			return 0.00;
		}
    	
    	while (rs.next()) {
    		String dlogin = rs.getString("Login");
    		if (login.compareTo(dlogin) == 0)	//finds the user
    			return rs.getDouble("Balance");	//gets is balance and returns it
    	}
		System.out.println("Invalid session...");	//special case
		return 0.00;
	}
	
	public List<Auctions> BuyersBoughtItems(String login) {	//simple function to get buyers bought auctions
		if (buyers == null)
			return null;
		buyer = buyersHT.get(login);
		if (buyer != null) {
			if (login.compareTo(buyer.getLogin()) == 0) {
				return buyer.getBoughtAuctions();
			}
		}
		return null;
	}
	
	public int buyItem(String login, int  ID, Connection con) throws SQLException, ClassNotFoundException, IOException {	//buying an item
		if (auctions == null) {
			System.out.println("No auctions registered...");
			return 1;
		}
		else {
			auction_item = auctionsHT.get(ID);	//finds the auction
			if (auction_item.isRunning() == false)
				return -4;
			if (auction_item.getID() == ID) {
				Item aItem = auction_item.getItem();
				if (users == null)
					return -1;
				if (login.compareTo(auction_item.getSeller().getLogin()) == 0) {	//special case
					System.out.println("You cannot buy your own auction...");
					return -2;
				}
				user = usersHT.get(login);
				if (login.compareTo(user.getLogin()) == 0) {	//finds the user
					if (user.getBalance() - auction_item.getbPrice() < 0) {	//special case
						System.out.println("Insufficent funds...");
						return -3;
					}
					double price;
					if (auction_item.getbPrice() > auction_item.getCurrentPrice()) {	//Setting buy price
						price = auction_item.getbPrice();
						auction_item.setCurrentPrice(auction_item.getbPrice());
					}
					else
						price = auction_item.getCurrentPrice();
						
					price = Math.round(price * 100.0) / 100.0;
					user.setBalance(user.getBalance() - price);	//setting balance of the user localy
					Bid thisBid = new Bid(user, price);	//creating new bid
					auction_item.getBidHistoryHT().put(login, thisBid);	//adding it to bid history for fraud checking
					auction_item.setCurrentBidder(login);	//setting current bidder
					auction_item.setRunning(false);	//closing auction
					Date date = new Date();
					auction_item.setEndDate(date);	//setting closing date
					aItem.setBoughtState(true);
					auction_item.setBuyer(buyer);	//setting buyer of the auction
					auction_item.setBuyername(login);
					int decision = buyer.Buy(sellersHT, buyersHT, usersHT, auction_item, price, con, thisBid);	//calling buyers buy function
						
					Statement stmnt = con.createStatement();
					String cmd = "SELECT * FROM USERS WHERE Login LIKE '" + login + "'";
				    ResultSet rs = stmnt.executeQuery(cmd);
				    	
				    while (rs.next()) {
						String dlogin = rs.getString("Login");	//seting balance of user in MySQL
						if (login.compareTo(dlogin) == 0) {
							cmd = "UPDATE users SET Balance = " + user.getBalance() + " WHERE Login like '" + login + "'";
							stmnt.execute(cmd);
							stmnt.close();
							this.save();
							return decision;
						}
					}
					return -4;
				}
			}
		}
		return -4;
	}
	
	public int bidOnAuction(String login, int ID, double bid, Connection con) throws ClassNotFoundException, IOException {
		if (auctions == null) {
			System.out.println("No auctions registered...");
			return -1;
		}
		else {
			auction_item = auctionsHT.get(ID);	//getting an auction by ID from input
			if (auction_item.isRunning() == false)
				return - 7;
			else if (ID == auction_item.getID()) {
				if ((auction_item.getCurrentPrice() != 0 && bid > auction_item.getCurrentPrice() * 100) || (auction_item.getCurrentPrice() == 0 && bid > 1 * 100) )	//fraud checking
					this.Fraud.inform(ID, login, bid, auction_item.getCurrentPrice(), con);
				if (buyers == null)
					return -3;
				buyer = buyersHT.get(login);	//getting buyer
				user = usersHT.get(login);	//getting user
				if (login.compareTo(user.getLogin()) == 0) {	//checking if they are the same, so user cant bid on his own auction
					if (login.compareTo(auction_item.getSeller().getLogin()) == 0) {
						System.out.println("You cannot bid on your own auction...");
						return -4;
					}
					else if (bid < 0.01) {	//special cases
						System.out.println("Insufficient amount...");
						return -2;
					}
					else if (user.getBalance() - bid < 0) {	//special cases
						System.out.println("Insufficent funds...");
						return -5;
					}
					if (bid > auction_item.getCurrentPrice())	//setting current price
						auction_item.setCurrentPrice(bid);
					else
						return -6;
					Bid thisBid = new Bid(user, bid);	//creating new bid
					auction_item.getBidHistoryHT().put(login, thisBid);	//adding it to bid history for fraud checking
					auction_item.setCurrentBidder(login);	//setting current bidder
					
					Seller seller = auction_item.getSeller();	//getting the seller
					int decision = seller.updateBid(bid, auction_item.getID(), login, thisBid);	//updating the bid in sellers auctions
					if (decision != 0)
						return decision;
					this.save();	//saving changes locally
					return 0;
				}
			}
		}
		return -7;
	}
	
	public int WithdrawBid(String login, int ID) {	//Bid withdrawal
		if (auctions == null) {
			System.out.println("No auctions registered...");
			return -1;
		}
		else {
			auction_item = auctionsHT.get(ID);	//getting auction
			if (auction_item == null || auction_item.isRunning() == false)
				return -1;
			else if (ID == auction_item.getID()) {	//if auction is found
				Bid userBid = auction_item.getBidHistoryHT().get(login);	//getting the bid
				if (userBid != null) {
					double thisBid = userBid.getBid();
					auction_item.getBidHistoryHT().remove(login);	///removing the bid
					Set<String> setOfLogins = auction_item.getBidHistoryHT().keySet();	//getting the set to iterate through hash table
					String newKey = null;
					int i = 0;
			        for(String key : setOfLogins) {	//iterating through hash table and finding the next highest bid
			        	if ( (auction_item.getBidHistoryHT().get(key).getBid() < thisBid && i == 0)
			        			|| (auction_item.getBidHistoryHT().get(key).getBid() > thisBid && i == 0 && auction_item.getBidHistoryHT().size() == 1) ) {
			        		thisBid = auction_item.getBidHistoryHT().get(key).getBid();
			        		newKey = key;
			        	}
			        	else if (auction_item.getBidHistoryHT().get(key).getBid() > thisBid && i != 0) {
			        		thisBid = auction_item.getBidHistoryHT().get(key).getBid();
			        		newKey = key;
			        	}
			        	//System.out.println(key + " " + newKey);
			        	i++;
			        }
			        if (auction_item.getBidHistoryHT().size() == 0) {
			        	auction_item.setCurrentBidder(null);
				        auction_item.setCurrentPrice(0);
						return 0;
			        }
			        else if (auction_item.getBidHistoryHT().get(newKey) != null) {	//setting the new highest bid after removal of the last highest bid
			        	auction_item.setCurrentBidder(auction_item.getBidHistoryHT().get(newKey).getBidder().getLogin());
				        auction_item.setCurrentPrice(thisBid);
						//System.out.println(auction_item.getCurrentBidder() + auction_item.getCurrentPrice());
						return 0;
			        }
				}
				else {
					return -2;
				}
			}
		}
		return -3;
	}
	
	
	
	
	
	public Auction() {
	}
	
	//SERIALIZATION
	public void save() throws ClassNotFoundException, IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("auction.out"));
		out.writeObject(this);
		out.close();
	}
	
	public void load() throws ClassNotFoundException, IOException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("auction.out"));
		Auction loadedAuction = (Auction) in.readObject();
		in.close();
		
		items = loadedAuction.items;
		users = loadedAuction.users;
		buyers = loadedAuction.buyers;
		sellers = loadedAuction.sellers;
		auctions = loadedAuction.auctions;
		ListenerCurrentID = 0;
	}
	
	public void delete() throws ClassNotFoundException, IOException {
		Auction auction = new Auction();
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("auction.out"));
		out.writeObject(auction);
		out.close();
	}
}