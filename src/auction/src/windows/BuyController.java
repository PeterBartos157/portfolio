package windows;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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

public class BuyController {

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

	public void setID(int ID) {
    	aid.setText("" + ID);
    }

    public void setInfo(String username) {
		this.username = username;
	}
    
    @FXML
    void close(ActionEvent event) {
    	Stage buyInter = (Stage) close.getScene().getWindow();
    	buyInter.hide();
    	buyInter.close();
    	return;
    }

    @FXML
    void confirm(ActionEvent event) throws IOException, InterruptedException, NumberFormatException, SQLException, ClassNotFoundException {
    	if (aid.getText().compareTo("") == 0) {
			return;
		}
		int decision = -10;
		Auction auction = MainRun.getAuction();
		decision = auction.buyItem(username, Integer.parseInt(aid.getText()), this.getConnection());
		
		Stage buyWindow = (Stage) close.getScene().getWindow();
		double x = buyWindow.getX() + (buyWindow.getWidth() / 3), y = buyWindow.getY() + (buyWindow.getHeight() / 3);
		if (decision == 0) {
			if (this.getParentWindow() != null)
				this.getParentWindow().close();
			
			int ID = Integer.parseInt(aid.getText());
			//Listener listener = new Listener(-2, MainRun.getAuction().getAuctionsHT().get(ID));
    		//MainRun.getAuction().getListenerHT().put(ID, listener);
			MainRun.getAuction().setListener(-2);
			MainRun.getAuction().setListenerCurrentID(ID);
			
			buyWindow.close();
			this.success(x, y, buyWindow);
			return;	
		}
		else if (decision == -1 || decision == -4 || decision == -5) {
			String info2 = "Transaction failed";
			this.fail(x, y, buyWindow, info2, "Auction was", "not found");
		}
		else if (decision == -2) {
			String info1 = "Transaction failed";
			String info2 = "You can not buy";
			String info3 = "your own auction";
			this.fail(x, y, buyWindow, info1, info2, info3);
		}
		else if (decision == -3) {
			String info2 = "Transaction failed";
			String info3 = "Insufficient funds";
			this.fail(x, y, buyWindow, info2, info3, "");
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
