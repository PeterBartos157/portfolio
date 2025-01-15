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

public class AddFundsController {

    @FXML
    private TextField add;

    @FXML
    private Button back;

    @FXML
    private Button confirm;
    
    private UserInterfaceController userC;
    private String username;
    private double balance;
    private Connection con;
    
	public Connection getConnection() {
		return con;
	}

	public void setConnection(Connection con) {
		this.con = con;
	}

	public UserInterfaceController getUserC() {
		return userC;
	}

	public void set(UserInterfaceController userC, String username, String balance) {
		this.userC = userC;
    	this.username = username;
    	this.balance = Double.parseDouble(balance);
    	return;
    }
    
    @FXML
    void back(ActionEvent event) {
    	Stage addWindow = (Stage) confirm.getScene().getWindow();
    	addWindow.hide();
    	addWindow.close();
    	return;
    }

    @FXML
    void confirm(ActionEvent event) throws IOException, InterruptedException, SQLException, ClassNotFoundException {
    	Auction auction = MainRun.getAuction();
    	int decision = auction.addFunds(this.username, Double.parseDouble(this.add.getText()), this.getConnection());
    	if (decision == -2 || decision == -1) {
    		Stage addWindow = (Stage) confirm.getScene().getWindow();
    		double x = addWindow.getX() + (addWindow.getWidth() / 5), y = addWindow.getY() + (addWindow.getWidth() / 5);
    		fail(x, y, addWindow);
    		return;
    	}
    	
    	Stage addWindow = (Stage) confirm.getScene().getWindow();
    	double funds = Double.parseDouble(add.getText());
    	
    	this.balance = this.balance + funds;
    	this.balance = (double) Math.round(balance * 100) / 100;
    	this.getUserC().reloadWindowInfo();
    	
		double x = addWindow.getX() + (addWindow.getWidth() / 5), y = addWindow.getY() + (addWindow.getWidth() / 5);
		addWindow.close();
		success(x, y, addWindow);
		
		auction.save();
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
    
    public void fail(double x, double y, Stage thisWindow) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/unsuccessfull.fxml"));
    	Parent root = (Parent) loader.load();
    	
    	FailController fC = loader.getController();
    	fC.setLabel("", "Transaction failed", "");
    	
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
    	Scene scene = (Scene) back.getScene();
    	scene.setCursor(Cursor.HAND);
    }

    @FXML
    void point(MouseEvent event) {
    	Scene scene = (Scene) back.getScene();
    	scene.setCursor(Cursor.DEFAULT);
    }

}