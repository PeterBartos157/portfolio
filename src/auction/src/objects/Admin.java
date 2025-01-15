package objects;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import windows.BanBidderController;
import windows.MainRun;

@SuppressWarnings("serial")
public class Admin extends User {
	private boolean AdminStage = false;
	
	public Admin () {
		super();
	}
	
	public Admin (String fname, String lname, String login, int password, String email) {	//Polymorphism
		super(fname, lname, login, password, email);
	}

	public void cancelAuction(int ID) {	//cancel auction
		if (MainRun.getAuction().getAuctionsHT() != null) {
			if (MainRun.getAuction().getAuctionsHT().get(ID) != null)
				MainRun.getAuction().getAuctionsHT().get(ID).setRunning(false);
		}
	}
	
	public boolean isAdminStage() {
		return AdminStage;
	}

	public void setAdminStage(boolean adminStage) {
		AdminStage = adminStage;
	}

	
	public void banUser(String login, Connection con) throws SQLException {	//bans user from application
		
		if (MainRun.getAuction().getUsersHT() != null) {
			if (MainRun.getAuction().getUsersHT().get(login) != null) {	//finds user
				MainRun.getAuction().getUsersHT().get(login).setBanned(true);	//bans user
				Statement stmnt = con.createStatement();
				String cmd = "SELECT * FROM USERS WHERE Login LIKE '" + login + "'";	//statement to get list of users from MySQL
				ResultSet rs = stmnt.executeQuery(cmd);
				
				while (rs.next()) {
					String dlogin = rs.getString("Login");
					if (login.compareTo(dlogin) == 0) {	//finds user in MySQL
						cmd = "UPDATE users SET IsBanned = 1 WHERE Login = '" + dlogin + "'";	//bans him there also
						stmnt.execute(cmd);
						stmnt.close();
						return;
					}
				}
			}
		}
	}
	
	public void unbanUser(String login, Connection con) throws SQLException {	//same as ban user but it unbans
		
		if (MainRun.getAuction().getUsersHT() != null) {
			if (MainRun.getAuction().getUsersHT().get(login) != null) {
				MainRun.getAuction().getUsersHT().get(login).setBanned(false);
				Statement stmnt = con.createStatement();
				String cmd = "SELECT * FROM USERS WHERE Login LIKE '" + login + "'";
				ResultSet rs = stmnt.executeQuery(cmd);
				
				while (rs.next()) {
					String dlogin = rs.getString("Login");
					if (login.compareTo(dlogin) == 0) {
	    			cmd = "UPDATE users SET IsBanned = 0 WHERE Login = '" + dlogin + "'";
	    			stmnt.execute(cmd);
	    			stmnt.close();
	    			return;
					}
				}
			}
		}
	}
	
	public void PotentialShieldNotification(int ID, String login, double newBid, double CurrentPrice, Connection con) throws IOException {	//shows pop-up to admin interface if there is a shield risk
		if (this.isAdminStage() == true) {
	    	
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/BanBidder.fxml"));
			Parent root = (Parent) loader.load();
			
			BanBidderController banC = loader.getController();
			banC.setInfo(login, newBid, CurrentPrice, ID);
			banC.setConnection(con);	
			
			Stage banWindow = new Stage();
			Image icon = new Image(getClass().getResourceAsStream("sources/adminIcon.png"));
			banWindow.getIcons().add(icon);
			banWindow.setScene(new Scene(root));
			banWindow.setResizable(false);
			banWindow.show();
		}
	}
	
}
