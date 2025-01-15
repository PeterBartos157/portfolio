package objects;

import auctions.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@SuppressWarnings("serial")
public class Seller extends User {
	private List<Auctions> sellerAuctions;
	private Hashtable<Integer, Auctions> sellerAuctionsHT;
	transient private Auctions sellerAuction;
	
	public Seller () {	//Polymorphism
		super();
	}
	
	public Seller (String fname, String lname, String login, int password, String email) {
		super(fname, lname, login, password, email);
	}
	
	public int Sell(Auctions auction) {
		if (this.sellerAuctions == null)
			this.sellerAuctions = new ArrayList<>();
		if (this.sellerAuctionsHT == null)
			this.sellerAuctionsHT = new Hashtable<>();
		this.sellerAuctions.add(auction);	//adds to sellers auction list
		this.sellerAuctionsHT.put(auction.getID(), auction); //adds to hashtable
		return 0;
	}
	
	public List<Auctions> getAuctionList() {
		if (sellerAuctions == null)
			return null;
		return this.sellerAuctions;
	}
	
	public Hashtable<Integer, Auctions> getAuctionsHT() {
		if (sellerAuctionsHT == null)
			return null;
		return this.sellerAuctionsHT;
	}
	
	public int cancelAuction(Auction main, int ID, Hashtable<Integer, Auctions> auctions) throws ClassNotFoundException, IOException {
		if (sellerAuctions == null)
			return -3;
		sellerAuction = sellerAuctionsHT.get(ID);
		if (sellerAuction != null && ID == sellerAuction.getID()) {	//finds auction from hash table
			Auctions auction = auctions.get(ID);
			if (auction != null && auction.getID() == ID) {	//finds auction from auctions
				if (sellerAuction.isRunning() == false && auction.isRunning() == false) {	//if auction is already cancelled
					//System.out.println("Auction is canceled already...");
					return -4;
				}
				auction.setRunning(false);	//cancels auction
				Date date = new Date();
				auction.setEndDate(date);
				sellerAuction.setRunning(false);
				sellerAuction.setEndDate(date);
				main.save();
				return 0;
			}
		}	
		return -3;
	}
	
	public int closeAuction(Auction main, int ID, Hashtable<Integer, Auctions> auctions, Hashtable<String, Buyer> buyers, String slogin, Connection con) throws ClassNotFoundException, IOException, SQLException {
		if (sellerAuctions == null)
			return -3;
		sellerAuction = sellerAuctionsHT.get(ID);
		if (sellerAuction != null && ID == sellerAuction.getID()) {	//finds sellers auction
			Auctions auction = auctions.get(ID);
			if (auction != null && auction.getID() == ID) {	//finds auction
				if (sellerAuction.isRunning() == false && auction.isRunning() == false) {
					System.out.println("Auction is closed already...");
					return -4;
				}
				String blogin = auction.getCurrentBidder();
				if (blogin == null) {
					auction.setRunning(false);
					sellerAuction.setRunning(false);
					return 0;
				}
				Buyer buyer = buyers.get(blogin);	//finds current bidder
				if (buyer != null && blogin.compareTo(buyer.getLogin()) == 0) {
					Buyer sbuyer = buyers.get(slogin);
					if (sbuyer != null && slogin.compareTo(sbuyer.getLogin()) == 0) {	//finds seller
						main.addFunds(slogin, auction.getCurrentPrice(), con);	//adds funds to seller
						auction.setRunning(false);	//closes auction
						sellerAuction.setRunning(false);
						main.subFunds(blogin, 0 - auction.getCurrentPrice(), con); //subs funds from buyer
						buyer.addBoughtAuction(auction);	//adds to buyers bought auction list
						Date date = new Date();
						auction.setEndDate(date);
						sellerAuction.setEndDate(date);	//sets information about auction
						auction.setBuyername(auction.getCurrentBidder());
						auction.setBuyer(buyers.get(auction.getCurrentBidder()));
						sellerAuction.setBuyername(sellerAuction.getCurrentBidder());
						sellerAuction.setBuyer(buyers.get(sellerAuction.getCurrentBidder()));
						main.save();	//saves locally
						return 0;
					}
				}
			}	
		}
		return -5;
	}
	
	public int updateBid(double bid, int ID, String login, Bid thisBid) {	//simple function to update bid
		if (sellerAuctions == null)
			return -8;
			sellerAuction = sellerAuctionsHT.get(ID);
			if (ID == sellerAuction.getID()) {
				sellerAuction.getBidHistoryHT().put(login, thisBid);
				sellerAuction.setCurrentPrice(bid);
				return 0;
		}
		return -9;
	}
	
	public List<Auctions> getAuctions() {
		return this.sellerAuctions;
	}
	
}
