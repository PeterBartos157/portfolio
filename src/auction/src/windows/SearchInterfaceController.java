package windows;

import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import auctions.Auction;
import auctions.Auctions;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import objects.Item;
import objects.Seller;

public class SearchInterfaceController {

    @FXML
    private Label label;

    @FXML
    private CheckBox active;

    @FXML
    private TextArea aid;
    
    @FXML
    private TextArea bprice;

    @FXML
    private TextArea aname;

    @FXML
    private Button back;

    @FXML
    private Button bid;

    @FXML
    private Button buy;

    @FXML
    private TextArea cbid;

    @FXML
    private TextArea edate;

    @FXML
    private TextField id;

    @FXML
    private TextArea idesc;

    @FXML
    private TextArea iname;

    @FXML
    private Label label1;

    @FXML
    private TextArea rdate;

    @FXML
    private Button search;
    
    @FXML
    private Button aclose;
    
    @FXML
    private Button acancel;

    @FXML
    private TextArea seller;
    
    @FXML
    private Label fail;
    private UserInterfaceController userC;
    private String aSeller;

	public String getaSeller() {
		return aSeller;
	}

	public void setaSeller(String aSeller) {
		this.aSeller = aSeller;
	}

	public void setTID(int ID) {
		this.id.setText("" + ID);
	}
    
	public UserInterfaceController getUserC() {
		return userC;
	}

	public void setUserC(UserInterfaceController userC) {
		this.userC = userC;
	}
    
    public void setFail() {
		this.fail.setText("Auction not found");
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                    	setFailNull();
                    }
				});
			}
		};
		timer.schedule(task, 3000);
	}

	public void setFailNull() {
		this.fail.setText("");
	}
    
    private int ID;
    private String username;
	private Stage userWindow;
    private Connection con;
    
    public Connection getConnection() {
		return con;
	}

	public void setConnection(Connection con) {
		this.con = con;
	}
    
    public int getID() {
		return ID;
	}

	public void setID(int iD) {
		this.ID = iD;
	}

	public void setLabels(String login, Stage userWindow) {
    	this.userWindow = userWindow;
    	this.username = login;
    }
    
    public Stage getUserWindow() {
    	return this.userWindow;
    }
    
    public void setaButtons(boolean decision) {
    	aclose.setDisable(decision);
		acancel.setDisable(decision);
		bid.setDisable(decision);
		buy.setDisable(decision);
    }
    
    public void setbButtons(boolean decision) {
    	this.buy.setVisible(decision);
    	this.bid.setVisible(decision);
    }
    
    public void setcButtons(boolean decision) {
    	this.aclose.setVisible(decision);
    	this.acancel.setVisible(decision);
    	if (this.username.compareTo("Admin") == 0)
    		this.aclose.setVisible(false);
    }
    
    public void setInfo(String login, Stage userWindow, Date rdate, String iname, String idesc, Date edate, double cbid, double bprice, int aid, String seller, String aname, boolean running) {
    	this.userWindow = userWindow;
    	this.username = login;
    	this.rdate.setText("" + rdate);
    	this.iname.setText(iname);
    	this.idesc.setText(idesc);
    	this.edate.setText("" + edate);
    	this.cbid.setText("" + cbid + "€");
    	this.aid.setText("" + aid);
    	this.seller.setText(seller);
    	this.aname.setText(aname);
    	this.bprice.setText("" + bprice + "€");
    	this.active.setSelected(running);
    	this.active.setDisable(true);
    }
    
    @FXML
    void search(ActionEvent event) throws IOException {
    	this.setFailNull();
    	Auction auction = MainRun.getAuction();
    	Auctions decision = null;
    	if (id.getText().compareTo("") != 0) {
    		decision = auction.searchAuction(Integer.parseInt(id.getText()));
    		if (decision == null) {
    			this.setFail();
        		return;
    		}
    		Stage UserInter = this.getUserWindow();
        	Stage searchWindow = (Stage) search.getScene().getWindow();
        	double x = searchWindow.getX(), y = searchWindow.getY() - (searchWindow.getY() / 6);
        	searchWindow.close();
        	
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/searchFoundInterface.fxml"));
    		Parent root = (Parent) loader.load();
    		
    		SearchInterfaceController searchC = loader.getController();
    		Item item = decision.getItem();
    		Seller seller = decision.getSeller();
    		boolean running = decision.isRunning();
    		searchC.setUserC(getUserC());
    		searchC.setInfo(username, UserInter, decision.getLaunchDate(), item.getName(), item.getDesc(), decision.getEndDate(), decision.getCurrentPrice(), decision.getbPrice(),
    				decision.getID(), seller.getLogin(), decision.getName(), running);
    		searchC.setaSeller(seller.getLogin());
    		searchC.setFailNull();
    		searchC.setID(decision.getID());
    		if (decision.getSellername().compareTo(username) == 0) {
    			searchC.setbButtons(false);
    			searchC.setcButtons(true);
    		}
    		else if (username.compareTo("Admin") == 0) {
    			searchC.setbButtons(false);
    			searchC.setcButtons(true);
    		}
    		else {
    			searchC.setbButtons(true);
    			searchC.setcButtons(false);
    		}
    		if (running == false) {
    			searchC.setaButtons(true);
    		}
    		else {
    			searchC.setaButtons(false);
    		}

    		
    		searchWindow = new Stage();
    		Image icon = new Image(getClass().getResourceAsStream("sources/icon.png"));
    		searchWindow.getIcons().add(icon);
    		searchWindow.setScene(new Scene(root));
    		searchWindow.setX(x);
    		searchWindow.setY(y);
    		searchWindow.setResizable(false);
    		searchWindow.show();
    	}
    	else
    		decision = null;
    	this.setFailNull();
    }

    @FXML
    void back(ActionEvent event) {
    	Stage searchWindow = (Stage) search.getScene().getWindow();
    	searchWindow.hide();
    	searchWindow.close();
    	return;
    }
    
    @FXML
    void bid(ActionEvent event) throws IOException {
    	Stage parentWindow = (Stage) search.getScene().getWindow();
    	double x = parentWindow.getX() + (parentWindow.getWidth() / 2), y = parentWindow.getY() + (parentWindow.getHeight() / 2);
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/BidAuction.fxml"));
		Parent root = (Parent) loader.load();
		
		BidController bidC = loader.getController();
		bidC.setUserC(this.getUserC());
		bidC.setInfo(username);
		bidC.setConnection(this.getConnection());
		bidC.setID(getID());
		Stage searchWindow = (Stage) search.getScene().getWindow();
		bidC.setParentWindow(searchWindow);
		
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
    	Stage parentWindow = (Stage) search.getScene().getWindow();
    	double x = parentWindow.getX() + (parentWindow.getWidth() / 2), y = parentWindow.getY() + (parentWindow.getHeight() / 2);
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/BuyAuction.fxml"));
		Parent root = (Parent) loader.load();
		
		BuyController buyC = loader.getController();
		buyC.setUserC(this.getUserC());
		buyC.setInfo(username);
		buyC.setConnection(this.getConnection());
		buyC.setID(getID());
		Stage searchWindow = (Stage) search.getScene().getWindow();
		buyC.setParentWindow(searchWindow);
		
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
		clC.setUserC(this.getUserC());
		clC.setInfo(username);
		clC.setConnection(this.getConnection());
		clC.setID(Integer.parseInt(this.aid.getText()));
		Stage parentWindow = (Stage) buy.getScene().getWindow();
		clC.setParentWindow(parentWindow);
		
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
    void acancel(ActionEvent event) throws IOException {
    	Stage UserWindow = this.getUserWindow();
    	double x = UserWindow.getX(), y = UserWindow.getY();
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/CancelAuction.fxml"));
		Parent root = (Parent) loader.load();
		
		CancelController caC = loader.getController();
		caC.setUserC(this.getUserC());
		caC.setConnection(this.getConnection());
		if (this.username.compareTo("Admin") == 0)
			caC.setInfo(this.getaSeller());
		else
			caC.setInfo(username);
		caC.setID(Integer.parseInt(this.aid.getText()));
		Stage parentWindow = (Stage) buy.getScene().getWindow();
		caC.setParentWindow(parentWindow);
		
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
    void hand(MouseEvent event) {
    	Scene scene = (Scene) back.getScene();
    	scene.setCursor(Cursor.HAND);
    }

    @FXML
    void point(MouseEvent event) {
    	Scene scene = (Scene) back.getScene();
    	scene.setCursor(Cursor.DEFAULT);
    }
    
}
