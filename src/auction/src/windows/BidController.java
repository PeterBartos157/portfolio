package windows;

import java.io.IOException;
import java.sql.Connection;
import auctions.Auction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BidController {

    @FXML
    private TextField abid;

    @FXML
    private TextField aid;

    @FXML
    private Button close;

    @FXML
    private Button confirm;
    
    private String username;
    private Stage parentWindow;
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

	public Stage getParentWindow() {
		return parentWindow;
	}

	public void setParentWindow(Stage parentWindow) {
		this.parentWindow = parentWindow;
	}

    public void setInfo(String username) {
    	this.username = username;
   	}
    
    public void setID(int ID) {
    	aid.setText("" + ID);
    }

    @FXML
    void close(ActionEvent event) {
    	Stage bidInter = (Stage) close.getScene().getWindow();
    	bidInter.hide();
    	bidInter.close();
    	return;
    }

    @FXML
    void confirm(ActionEvent event) throws IOException, InterruptedException, NumberFormatException, ClassNotFoundException {
    	Stage bidWindow = (Stage) close.getScene().getWindow();
		double x = bidWindow.getX() + (bidWindow.getWidth() / 4), y = bidWindow.getY() + (bidWindow.getHeight() / 5);
		
    	if (aid.getText().compareTo("") == 0) {
    		this.fail(x, y, bidWindow, "Bidding failed", "Auction was", "not found");
			return;
		}
		Auction auction = MainRun.getAuction();
		int decision = auction.bidOnAuction(username, Integer.parseInt(aid.getText()), Double.parseDouble(abid.getText()), this.getConnection());
		if (decision == 0) {
			bidWindow.close();
			if (this.getParentWindow() != null)
				this.getParentWindow().close();
			
			int ID = Integer.parseInt(aid.getText());
			//Listener listener = new Listener(-1, MainRun.getAuction().getAuctionsHT().get(ID));
    		//MainRun.getAuction().getListenerHT().put(ID, listener);
			MainRun.getAuction().setListener(-1);
			MainRun.getAuction().setListenerCurrentID(ID);
			
	    	this.success(x, y, bidWindow);
			return;
		}
		else if (decision == -1 || decision == -7) {
			this.fail(x, y, bidWindow, "Bidding failed", "Auction was", "not found");
			return;
		}
		else if (decision == -2) {
			this.fail(x, y, bidWindow, "Bidding failed", "Insufficent amount", "");
			return;
		}
		else if (decision == -3 || decision == -8 || decision == -9) {
			this.fail(x, y, bidWindow, "Bidding failed", "Invalid session", "");
			return;
		}
		else if (decision == -4) {
			this.fail(x, y, bidWindow, "Bidding failed", "You can not bid on", "your own auction");
			return;
		}
		else if (decision == -5) {
			this.fail(x, y, bidWindow, "Bidding failed", "Insufficent funds", "");
			return;
		}
		else if (decision == -6) {
			this.fail(x, y, bidWindow, "Bidding failed", "Current bid", "is larger");
			return;
		}
    }
    
    public void success(double x, double y, Stage thisWindow) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/successfull.fxml"));
    	Parent root = (Parent) loader.load();
    	
    	SuccessController sC = loader.getController();
    	sC.setLabel("Transaction was", "successfull");
    	
    	Stage suWindow = new Stage();
    	Image icon = new Image(getClass().getResourceAsStream("sources/yes.png"));
    	suWindow.getIcons().add(icon);
    	suWindow.setScene(new Scene(root));
    	suWindow.setX(x);
    	suWindow.setY(y);
    	suWindow.setResizable(false);
    	suWindow.initModality(Modality.APPLICATION_MODAL);
    	suWindow.initOwner(thisWindow);
    	suWindow.setAlwaysOnTop(true);
    	suWindow.show();
    }
    
    public void fail(double x, double y, Stage thisWindow, String info1, String info2, String info3) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/unsuccessfull.fxml"));
    	Parent root = (Parent) loader.load();
    	
    	FailController fC = loader.getController();
    	fC.setLabel(info1, info2, info3);
    	
    	Stage usuWindow = new Stage();
    	Image icon = new Image(getClass().getResourceAsStream("sources/no.png"));
    	usuWindow.getIcons().add(icon);
    	usuWindow.setScene(new Scene(root));
    	usuWindow.setX(x);
    	usuWindow.setY(y);
    	usuWindow.setResizable(false);
    	usuWindow.initModality(Modality.APPLICATION_MODAL);
    	usuWindow.initOwner(thisWindow);
    	usuWindow.setAlwaysOnTop(true);
    	usuWindow.show();
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
