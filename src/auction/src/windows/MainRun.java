package windows;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import auctions.Auction;
import auctions.Auctions;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import objects.Buyer;
import objects.Seller;
import objects.User;

public class MainRun extends Application {
    
	static private Auction auction;
	
	static public Auction getAuction() {
		if (auction == null)
			auction = new Auction();
		return auction;
	}
	
	@Override
	public void start(Stage loginWindow) throws Exception {
		Connection con = this.createConnection();	//setting up connection
		if (auction == null) {	//setting up auction
			auction = new Auction();
			auction.setListenerCurrentID(0);
		}
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/loginInterface.fxml"));	//setting up fxml
		Parent root = (Parent) loader.load();
		
		LoginInterfaceController loginC = loader.getController();	//setting up controller
		loginC.setConnection(con);
		loginC.setFailNull();	//setting up controller so it does not show "wrong password" message
		
		loginWindow.setTitle("");	//setting title of window to ""
		Image icon = new Image(getClass().getResourceAsStream("sources/icon.png"));	//setting icon of window
	    loginWindow.getIcons().add(icon);
	    loginWindow.setResizable(false);
		loginWindow.setScene(new Scene(root));	//setting scene
		
		try {
			auction.load();	//trying to load auction from file locally
			
			if (auction.getAuctions() != null || auction.getAuctionsHT() == null) {	//special case
				auction.createAuctionsHT();
			}
			
			Statement stmnt = con.createStatement();
			ResultSet rs = stmnt.executeQuery("SELECT * FROM AUCTION_TIMER");	//getting closing times of auctions from MySQL
			while (rs.next()) {
				Auctions auctionst = auction.getAuctionsHT().get(rs.getInt("auctionID"));
				if (auctionst == null)
					break;
				else if (auctionst.isRunning() == true) {	//creating timer for every running auction so it closes when it is supposed to
					Timer timer = new Timer();
					TimerTask task = new TimerTask() {
						@Override
						public void run() {
							try {
								if (auctionst.isRunning() == true)
									auction.closeAuction(auctionst.getSellername(), auctionst.getID(), con);
							} catch (ClassNotFoundException | IOException | SQLException e) { e.printStackTrace(); }
						}
					};
					timer.schedule(task, auctionst.getEndDate());
				}
			}
			
			if (auction.getUsers() != null || auction.getSellers() != null || auction.getBuyers() != null) {	//setting up hashtables
				auction.createBuyersHT();
				auction.createSellersHT();
				auction.createUsersHT();
			}
		} catch (ClassNotFoundException | IOException e1) {	//if the program is not able to load auction this exception is thrown to keep the program running
			//e1.printStackTrace();
			System.out.println("Exception caught: Getting users from MySQL server...");
			auction.createAuctionsHT();
			Statement stmnt = con.createStatement();
			ResultSet rs = stmnt.executeQuery("SELECT * FROM USERS");	//it gets the users list from MySQL
			List<User> users = auction.getUsers();
			while (rs.next()) {	//it saves every user from MySQL to local file
				if (auction.getUsers() == null) {
					users = new ArrayList<>();
					auction.setUsers(users);
					List<Buyer> buyers = new ArrayList<>();
					auction.setBuyers(buyers);
					List<Seller> sellers = new ArrayList<>();
					auction.setSellers(sellers);
				}
				if (auction.getUsers() != null || auction.getSellers() != null || auction.getBuyers() != null) {	//creating hashtables
					auction.createBuyersHT();
					auction.createSellersHT();
					auction.createUsersHT();
				}
				auction.getUsers().add(new User(rs.getString("First Name"), rs.getString("Last Name"), rs.getString("Login"), rs.getInt("Password"), rs.getString("Email")));	//adding user
				auction.getBuyers().add(new Buyer(rs.getString("First Name"), rs.getString("Last Name"), rs.getString("Login"), rs.getInt("Password"), rs.getString("Email")));	//adding buyer
				auction.getSellers().add(new Seller(rs.getString("First Name"), rs.getString("Last Name"), rs.getString("Login"), rs.getInt("Password"), rs.getString("Email"))); //adding seller
				User user = users.get(users.size() - 1);
				user.addFunds(auction.getBalance(rs.getString("Login"), con));	//adding funds to user
				auction.getUsersHT().put(users.get(users.size() - 1).getLogin(), users.get(users.size() - 1)); //adding them to hashtables
				auction.getBuyersHT().put(auction.getBuyers().get(auction.getBuyers().size() - 1).getLogin(), auction.getBuyers().get(auction.getBuyers().size() - 1));
				auction.getSellersHT().put(auction.getSellers().get(auction.getSellers().size() - 1).getLogin(), auction.getSellers().get(auction.getSellers().size() - 1));
				loginWindow.show();	//showing the window
			}
		}
		
		loginWindow.show();	//showing the login window
	}

	
	public static void main(String[] args) throws Exception	{
		launch(args);
	}
	
	public Connection createConnection() throws ClassNotFoundException, SQLException {	//establishing connection to MySQL database
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Blind_auction_database", "root", "12345");
		System.out.println("Database connection success");
		return con;
	}
}
