package windows;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import auctions.Auction;
import auctions.Auctions;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import objects.Item;

public class AuctionListController implements Initializable {
	
	@FXML
    private TextArea aid;

    @FXML
    private TextArea aname;

    @FXML
    private Button bid;

    @FXML
    private TextArea bprice;

    @FXML
    private Button buy;

    @FXML
    private TextArea cbid;

    @FXML
    private TextArea cdate;

    @FXML
    private TextArea idesc;

    @FXML
    private TextArea iname;

    @FXML
    private ScrollPane infopane;

    @FXML
    private TextArea ldate;

    @FXML
    private TextArea seller;
    
    @FXML
    private Button close;
    
    @FXML
    private Button show;
    
    @FXML
    private Button cancel;
    
    @FXML
    private Button aclose;
    
    @FXML
    private Button reg;
    
    @FXML
    private Button withdraw;

    @FXML
    private ListView<String> list;
    
    private List<Auctions> auctions;
    private Stage userWindow;
    private String username;
    private double balance;
    private Connection con;
    private UserInterfaceController userC;

	public UserInterfaceController getUserC() {
		return userC;
	}

	public void setUserC(UserInterfaceController userC) {
		this.userC = userC;
	}

	public Connection getConnection() {
		return con;
	}

	public void setConnection(Connection con) {
		this.con = con;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
    	
    	list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				
				show(null);
				
			}
		});
	}
    
    public Stage getUserWindow() {
		return userWindow;
	}

	public void setUserWindow(Stage userWindow) {
		this.userWindow = userWindow;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void setInfo(Stage userWindow, String username) throws SQLException {
		Auction auction = MainRun.getAuction();
    	this.setUserWindow(userWindow);
    	this.setUsername(username);
    	this.setBalance(auction.getBalance(username, this.getConnection()));
    }

    @FXML
    void show(ActionEvent event) {
    	
    	ObservableList<String> selection = list.getSelectionModel().getSelectedItems();
    	String item = selection.get(0);
		String id = "";
		char idc;
		for (int i = 0; i < item.length(); i++) {
			idc = item.charAt(i);
			if (idc == ' ')
				break;
			id = id + idc;
		}
		Iterator<Auctions> iterA = auctions.iterator();
    	Auctions auction;
    	for (int i = 0; i <= auctions.size(); i++) {
    		auction = iterA.next();
    		if (auction.getID() == Integer.parseInt(id)) {
    			this.infopane.setVisible(true);
    			this.aid.setText("" + auction.getID());
    			this.aname.setText(auction.getName());
    			this.bprice.setText("" + auction.getbPrice());
    			this.cbid.setText("" + auction.getCurrentPrice());
    			this.cdate.setText("" + auction.getEndDate());
    			Item item1 = auction.getItem();
    			this.idesc.setText(item1.getDesc());
    			this.iname.setText(item1.getName());
    			this.ldate.setText("" + auction.getLaunchDate());
    			this.seller.setText(auction.getSellername());
    			if (this.getUsername().compareTo(auction.getSellername()) == 0) {
    				this.aclose.setDisable(false);
    				this.cancel.setDisable(false);
    				this.bid.setDisable(true);
    				this.buy.setDisable(true);
    				this.withdraw.setDisable(true);
    			}
    			else {
    				this.aclose.setDisable(true);
    				this.cancel.setDisable(true);
    				this.bid.setDisable(false);
    				this.buy.setDisable(false);
    				this.withdraw.setDisable(false);
    			}
    				
    			break;
    		}
    	}
    	
    }
    
    public void hideScroll() {
    	this.infopane.setVisible(false);
    }
    
    public void setAuctions(List<Auctions> auctions) {
    	this.auctions = auctions;
    	if (auctions == null)
    		return;
    	Iterator<Auctions> iterAuctions = auctions.iterator();
    	while (iterAuctions.hasNext() != false) {
    		Auctions auction = iterAuctions.next();
    		if (auction.isRunning() == true) {
    			list.getItems().add(auction.getID() + " | " + auction.getName() + " | Bid " + auction.getCurrentPrice() + "€ | Buy " + auction.getbPrice() + "€");
    		}
    	}
    }
    
    public List<Auctions> getAuctions() {
    	return this.auctions;
    }
    
    @FXML
    void close(ActionEvent event) {
    	Stage alInter = (Stage) close.getScene().getWindow();
    	alInter.hide();
    	alInter.close();
    	return;
    }
    
    @FXML
    void bid(ActionEvent event) throws IOException {
    	Stage UserWindow = this.getUserWindow();
    	double x = UserWindow.getX(), y = UserWindow.getY();
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/BidAuction.fxml"));
		Parent root = (Parent) loader.load();
		
		BidController bidC = loader.getController();
		bidC.setUserC(getUserC());
		bidC.setInfo(username);
		bidC.setID(Integer.parseInt(this.aid.getText()));
		bidC.setConnection(this.getConnection());
		Stage parentWindow = (Stage) bid.getScene().getWindow();
		bidC.setParentWindow(parentWindow);
		
		Stage bidWindow = new Stage();
		Image icon = new Image(getClass().getResourceAsStream("sources/icon.png"));
		bidWindow.getIcons().add(icon);
		bidWindow.setScene(new Scene(root));
		bidWindow.setX(x);
		bidWindow.setY(y);
		bidWindow.setResizable(false);
		bidWindow.show();
    }

    @FXML
    void buy(ActionEvent event) throws IOException {
    	Stage UserWindow = this.getUserWindow();
    	double x = UserWindow.getX(), y = UserWindow.getY();
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/BuyAuction.fxml"));
		Parent root = (Parent) loader.load();
		
		BuyController buyC = loader.getController();
		buyC.setUserC(getUserC());
		buyC.setInfo(username);
		buyC.setID(Integer.parseInt(this.aid.getText()));
		buyC.setConnection(this.getConnection());
		Stage parentWindow = (Stage) buy.getScene().getWindow();
		buyC.setParentWindow(parentWindow);
		
		Stage buyWindow = new Stage();
		Image icon = new Image(getClass().getResourceAsStream("sources/icon.png"));
		buyWindow.getIcons().add(icon);
		buyWindow.setScene(new Scene(root));
		buyWindow.setX(x);
		buyWindow.setY(y);
		buyWindow.setResizable(false);
		buyWindow.show();
    }
    
    @FXML
    void aclose(ActionEvent event) throws IOException {
    	Stage UserWindow = this.getUserWindow();
    	double x = UserWindow.getX(), y = UserWindow.getY();
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/CloseAuction.fxml"));
		Parent root = (Parent) loader.load();
		
		CloseController clC = loader.getController();
		clC.setUserC(getUserC());
		clC.setInfo(this.getUsername());
		clC.setID(Integer.parseInt(this.aid.getText()));
		clC.setConnection(this.getConnection());
		Stage parentWindow = (Stage) buy.getScene().getWindow();
		clC.setParentWindow(parentWindow);
		
		Stage closeWindow = new Stage();
		Image icon = new Image(getClass().getResourceAsStream("sources/icon.png"));
		closeWindow.getIcons().add(icon);
		closeWindow.setScene(new Scene(root));
		closeWindow.setX(x);
		closeWindow.setY(y);
		closeWindow.setResizable(false);
		closeWindow.show();
    }
    
    @FXML
    void cancel(ActionEvent event) throws IOException {
    	Stage UserWindow = this.getUserWindow();
    	double x = UserWindow.getX(), y = UserWindow.getY();
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/CancelAuction.fxml"));
		Parent root = (Parent) loader.load();
		
		CancelController caC = loader.getController();
		caC.setUserC(getUserC());
		caC.setInfo(username);
		caC.setConnection(this.getConnection());
		caC.setID(Integer.parseInt(this.aid.getText()));
		Stage parentWindow = (Stage) buy.getScene().getWindow();
		caC.setParentWindow(parentWindow);
		
		Stage cancelWindow = new Stage();
		Image icon = new Image(getClass().getResourceAsStream("sources/icon.png"));
		cancelWindow.getIcons().add(icon);
		cancelWindow.setScene(new Scene(root));
		cancelWindow.setX(x);
		cancelWindow.setY(y);
		cancelWindow.setResizable(false);
		cancelWindow.show();
    }
    
    @FXML
    void regA(ActionEvent event) throws IOException {
    	Stage UserInter = (Stage) this.getUserWindow();
    	double x = UserInter.getX() + (UserInter.getX() / 4), y = UserInter.getY();
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/auctionRegInterface.fxml"));
		Parent root = (Parent) loader.load();
		
		RegisterAuctionController regAuctionC = loader.getController();
		regAuctionC.setConnection(getConnection());
		regAuctionC.setUserC(getUserC());
		regAuctionC.setUsername(username);
		Stage parentWindow = (Stage) buy.getScene().getWindow();
		regAuctionC.setParentWindow(parentWindow);
		regAuctionC.setNull();
		
		
		Stage arWindow = new Stage();
		Image icon = new Image(getClass().getResourceAsStream("sources/icon.png"));
		arWindow.getIcons().add(icon);
		arWindow.setScene(new Scene(root));
		arWindow.setX(x);
		arWindow.setY(y);
		arWindow.setResizable(false);
		arWindow.show();
    }
    
    @FXML
    void withdraw(ActionEvent event) throws IOException {
    	Stage UserWindow = this.getUserWindow();
    	double x = UserWindow.getX(), y = UserWindow.getY();
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/WithdrawBidAuction.fxml"));
		Parent root = (Parent) loader.load();
		
		WithdrawBidController bidC = loader.getController();
		bidC.setUserC(getUserC());
		bidC.setInfo(username);
		bidC.setID(Integer.parseInt(this.aid.getText()));
		bidC.setConnection(this.getConnection());
		Stage parentWindow = (Stage) bid.getScene().getWindow();
		bidC.setParentWindow(parentWindow);
		
		Stage bidWindow = new Stage();
		Image icon = new Image(getClass().getResourceAsStream("sources/icon.png"));
		bidWindow.getIcons().add(icon);
		bidWindow.setScene(new Scene(root));
		bidWindow.setX(x);
		bidWindow.setY(y);
		bidWindow.setResizable(false);
		bidWindow.show();
    }
    
    @FXML
    void hand(MouseEvent event) {
    	Scene scene = (Scene) close.getScene();
    	scene.setCursor(Cursor.HAND);
    }

    @FXML
    void point(MouseEvent event) {
    	Scene scene = (Scene) close.getScene();
    	scene.setCursor(Cursor.DEFAULT);
    }
    
}