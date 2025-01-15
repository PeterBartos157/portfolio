package windows;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BanBidderController {

    @FXML
    private Button back;

    @FXML
    private Button ban;

    @FXML
    private Label cprice;

    @FXML
    private Label label;

    @FXML
    private Label login;

    @FXML
    private Label ubid;
    
    private Connection con;
    private int auctionID;
    private String username;

	public void setInfo(String login, double ubid, double cprice, int ID) {
		this.login.setText("User: " + login);
		this.username = login;
		this.cprice.setText("Current price of item: " + cprice + "€");
		this.ubid.setText("New bid on item: " + ubid + "€");
		this.auctionID = ID;
	}

    public Connection getConnection() {
		return con;
	}

	public void setConnection(Connection con) {
		this.con = con;
	}

	@FXML
    void back(ActionEvent event) {
    	Stage Window = (Stage) back.getScene().getWindow();
    	Window.close();
    	return;
    }

    @FXML
    void ban(ActionEvent event) throws SQLException, IOException {
    	MainRun.getAuction().getAdmin().banUser(this.username, this.getConnection());
    	MainRun.getAuction().WithdrawBid(this.username, this.auctionID);
    	Stage Window = (Stage) back.getScene().getWindow();
    	Window.close();
    	success();
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
    
    public void success() throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/successfull.fxml"));
    	Parent root = (Parent) loader.load();
    	
    	SuccessController sC = loader.getController();
    	sC.setLabel("Action was", "successfull");
    	
    	Stage suWindow = new Stage();
    	Image icon = new Image(getClass().getResourceAsStream("sources/yes.png"));
    	suWindow.getIcons().add(icon);
    	suWindow.setScene(new Scene(root));
    	suWindow.setResizable(false);
    	suWindow.initModality(Modality.APPLICATION_MODAL);
    	suWindow.setAlwaysOnTop(true);
    	suWindow.show();
    }

}
