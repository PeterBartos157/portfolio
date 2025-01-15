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

public class CloseController {

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

	public void setID(int ID) {
		this.aid.setText("" + ID);
	}

	public Stage getParentWindow() {
		return parentWindow;
	}

	public void setParentWindow(Stage parentWindow) {
		this.parentWindow = parentWindow;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setInfo(String username) {
		this.setUsername(username);
	}

	@FXML
    void confirm(ActionEvent event) throws IOException, InterruptedException, NumberFormatException, ClassNotFoundException, SQLException {
		Stage closeWindow = (Stage) confirm.getScene().getWindow();
		double x = closeWindow.getX() + (closeWindow.getWidth() / 3), y = closeWindow.getY() + (closeWindow.getHeight() / 5);
		
		if (aid.getText().compareTo("") == 0) {
			this.fail(x, y, closeWindow, "Action failed", "Auction ID was", "not found");
			return;
		}
		Auction auction = MainRun.getAuction();
		int closeA = auction.closeAuction(getUsername(), Integer.parseInt(aid.getText()), this.getConnection());
		if (closeA == 0) {
			if (this.getParentWindow() != null)
				this.getParentWindow().close();
			closeWindow.close();
			
			int ID = Integer.parseInt(aid.getText());
			MainRun.getAuction().setListener(-4);
			MainRun.getAuction().setListenerCurrentID(ID);
			
			this.success(x, y, closeWindow);
			return;
		}
		else if (closeA == -1) {
			this.fail(x, y, closeWindow, "Action failed", "Invalid", "session");
			return;
		}
		else if (closeA == -2 || closeA == -5) {
			this.fail(x, y, closeWindow, "Action failed", "Auction was", "not found");
			return;
		}
		else if (closeA == -3) {
			this.fail(x, y, closeWindow, "Action failed", "You have no", "registered auctions");
			return;
		}
		else if (closeA == -4) {
			this.fail(x, y, closeWindow, "Action failed", "Auction is", "already closed");
			return;
		}
    }
    
    @FXML
    void close(ActionEvent event) {
    	Stage acsInter = (Stage) close.getScene().getWindow();
    	acsInter.close();
    	return;
    }
    
    public void success(double x, double y, Stage thisWindow) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/successfull.fxml"));
    	Parent root = (Parent) loader.load();
    	
    	SuccessController sC = loader.getController();
    	sC.setLabel("Action was", "successfull");
    	
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