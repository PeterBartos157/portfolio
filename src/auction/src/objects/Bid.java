package objects;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Bid implements Serializable {
	private double lastBid;
	private User lastBidder;
	
	public Bid () {
	}
	
	public Bid (User bidder, double bid) {
		this.setBid(bid);
		this.setBidder(bidder);
	}
	
	public double getBid() {
		return lastBid;
	}
	public void setBid(double lastBid) {
		this.lastBid = lastBid;
	}
	public User getBidder() {
		return lastBidder;
	}
	public void setBidder(User lastBidder) {
		this.lastBidder = lastBidder;
	}
	
	
}
