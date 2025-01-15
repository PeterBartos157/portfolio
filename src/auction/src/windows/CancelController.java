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

public class CancelController {

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
    void confirm(ActionEvent event) throws IOException, InterruptedException, NumberFormatException, ClassNotFoundException {
		Stage cancelWindow = (Stage) confirm.getScene().getWindow();
		double x = cancelWindow.getX() + (cancelWindow.getWidth() / 4), y = cancelWindow.getY() + (cancelWindow.getHeight() / 5);
		if (aid.getText().compareTo("") == 0) {
			this.fail(x, y, cancelWindow, "Cancel failed", "Auction was", "not found");
			return;
		}
		Auction auction = MainRun.getAuction();
		int cancelA = -1;
		cancelA = auction.cancelAuction(getUsername(), Integer.parseInt(aid.getText()));
		if (cancelA == 0) {
			if (this.getParentWindow() != null)
				this.getParentWindow().close();
			cancelWindow.close();
			
			int ID = Integer.parseInt(aid.getText());
			MainRun.getAuction().setListener(-3);
			MainRun.getAuction().setListenerCurrentID(ID);
			
			this.success(x, y, cancelWindow);
			return;
		}
		else if (cancelA == -1 || cancelA == -2) {
			this.fail(x, y, cancelWindow, "Cancel failed", "Invalid session", "");
			return;
		}
		else if (cancelA == -4) {
			this.fail(x, y, cancelWindow, "Cancel failed", "Auction is", "already cancelled");
			return;
		}
		else if (cancelA == -3) {
			this.fail(x, y, cancelWindow, "Cancel failed", "Auction was", "not found");
			return;
		}
    }
    
    @FXML
    void close(ActionEvent event) {
    	Stage acInter = (Stage) close.getScene().getWindow();
    	acInter.close();
    	return;
    }
    
    public void success(double x, double y, Stage thisWindow) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/successfull.fxml"));
    	Parent root = (Parent) loader.load();
    	
    	SuccessController sC = loader.getController();
    	sC.setLabel("Auction cancelled", "successfully");
    	
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