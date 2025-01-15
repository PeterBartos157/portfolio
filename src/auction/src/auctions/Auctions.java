package auctions;

import java.io.*;
import java.util.*;
import objects.*;

@SuppressWarnings("serial")
public class Auctions implements Serializable {
	private int ID;
	private Item item;
	private String name;
	private String itemName;
	private Date LaunchDate;
	private Date EndDate;
	private Seller seller;
	private String sellername;
	private Buyer buyer;
	private String buyername;
	private double basePrice;
	private double currentPrice;
	private Hashtable<String, Bid> bidHistoryHT; //created for fraud checking later
	private String currentBidder;
	private boolean Running;
	
	public Auctions() {
	}
	
	public Auctions(int ID, String username, String name, Date EndDate, Item item, String itemName, Seller seller, double basePrice, double currentPrice, 
			double lastBid, String currentBidder) {
		setID(ID);
		setName(name);
		setItem(item);
		Date LaunchDate = new Date();
		setLaunchDate(LaunchDate);
		setEndDate(EndDate);
		setItemName(itemName);
		setSeller(seller);
		setSellername(username);
		setbPrice(basePrice);
		if (this.bidHistoryHT == null)
			this.bidHistoryHT = new Hashtable<String, Bid>();
		setCurrentPrice(currentPrice);
		setCurrentBidder(currentBidder);
		setRunning(true);
		setBuyer(null);
		setBuyername(null);
		
	}
	
	public void setID(int ID) {
		this.ID = ID;
	}
	
	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}
	
	public void setRunning(boolean running) {
		this.Running = running;
	}
	
	public void setItem(Item item) {
		this.item = item;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setLaunchDate(Date date) {
		this.LaunchDate = date;
	}
	
	public void setEndDate(Date date) {
		this.EndDate = date;
	}
	
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public void setSeller(Seller seller) {
		this.seller = seller;
	}
	
	public void setbPrice(double basePrice) {
		this.basePrice = basePrice;
	}
	
	public void setCurrentBidder(String currentBidder) {
		this.currentBidder = currentBidder;
	}
	
	
	
	
	public double getCurrentPrice() {
		return this.currentPrice;
	}
	
	public int getID() {
		return this.ID;
	}
	
	public boolean isRunning() {
		return this.Running;
	}
	
	public Item getItem() {
		return this.item;
	}
	
	public Date getLaunchDate() {
		return this.LaunchDate;
	}
	
	public Date getEndDate() {
		return this.EndDate;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getItemName() {
		return this.itemName;
	}
	
	public Seller getSeller() {
		return this.seller;
	}
	
	public double getbPrice() {
		return this.basePrice;
	}
	
	public String getCurrentBidder() {
		return this.currentBidder;
	}

	public String getSellername() {
		return sellername;
	}

	public void setSellername(String sellername) {
		this.sellername = sellername;
	}

	public String getBuyername() {
		return buyername;
	}

	public void setBuyername(String buyername) {
		this.buyername = buyername;
	}

	public Buyer getBuyer() {
		return buyer;
	}

	public void setBuyer(Buyer buyer) {
		this.buyer = buyer;
	}

	public Hashtable<String, Bid> getBidHistoryHT() {
		return bidHistoryHT;
	}

	public void setBidHistoryHT(Hashtable<String, Bid> bidHistoryHT) {
		this.bidHistoryHT = bidHistoryHT;
	}

}
