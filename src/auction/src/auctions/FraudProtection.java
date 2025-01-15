package auctions;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import objects.Observer;
import windows.MainRun;

@SuppressWarnings("serial")
public class FraudProtection implements Observer, Serializable {
	private List<Integer> shieldRisksID;
	private List<String> shieldRisksLogin;
	private List<Double> shieldRisksBid;
	private List<Double> shieldRisksPrice;
	
	public void inform (int ID, String login, double newBid, double CurrentPrice, Connection con) throws IOException {
		MainRun.getAuction().setFraudAlert(1);	//sets fraud alert to 1
		if (MainRun.getAuction().getAdmin() != null) {	//if admin is logged in
			MainRun.getAuction().getAdmin().PotentialShieldNotification(ID, login, newBid, CurrentPrice, con);	//it gives him a pop up with potential risk
			if (MainRun.getAuction().getAdmin().getNotifications() == null)
				MainRun.getAuction().getAdmin().setNotifications(new ArrayList<>());
			MainRun.getAuction().getAdmin().getNotifications().add(login + " | " + ID + " | " + CurrentPrice + " | " + newBid + " | Potential shield risk!");	//and sends him notification
		}
		else {
			if (shieldRisksID == null)
				shieldRisksID = new ArrayList<>();
			shieldRisksID.add(ID);
			if (shieldRisksLogin == null)
				shieldRisksLogin = new ArrayList<>();
			shieldRisksLogin.add(login);
			if (shieldRisksBid == null)
				shieldRisksBid = new ArrayList<>();
			shieldRisksBid.add(newBid);
			if (shieldRisksPrice == null)
				shieldRisksPrice = new ArrayList<>();
			shieldRisksPrice.add(CurrentPrice);
		}
	}
}
