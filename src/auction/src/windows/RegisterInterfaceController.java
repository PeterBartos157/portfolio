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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RegisterInterfaceController {

    @FXML
    private TextField emailt;
    @FXML
    private Label fnamel;
    @FXML
    private TextField fnamet;
    @FXML
    private Label lemail;
    @FXML
    private Label lnamel;
    @FXML
    private TextField lnamet;
    @FXML
    private Label loginl; 
    @FXML
    private TextField logint;
    @FXML
    private Button login;
    @FXML
    private Label passwordl;
    @FXML
    private PasswordField passwordt;
    @FXML
    private Button register;
    @FXML
    private Label fnames;
    @FXML
    private Label lnames;
    @FXML
    private Label logins;
    @FXML
    private Label passwords;
    @FXML
    private Label emails;
    private Connection con;
    private Stage LoginStage;
    
	public Stage getLoginStage() {
		return LoginStage;
	}

	public void setLoginStage(Stage loginStage) {
		LoginStage = loginStage;
	}

	public Connection getConnection() {
		return con;
	}

	public void setConnection(Connection con) {
		this.con = con;
	}

	public void setNull() {
    	this.fnames.setText("");
    	this.lnames.setText("");
    	this.logins.setText("");
    	this.passwords.setText("");
    	this.emails.setText("");
    }
    
    @FXML
    void register(ActionEvent event) throws IOException, SQLException {
    	this.setNull();
    	
    	int dec = 0;
    	if (logint.getText().length() < 2) {
			this.logins.setText(" Too short");
			dec = 1;
		}
		else if (logint.getText().length() > 20) {
			this.logins.setText(" Too long");
			dec = 1;
		}
		
		 if (passwordt.getText().length() < 5) {
			this.passwords.setText(" Too short");
			dec = 1;
		 }
		else if (passwordt.getText().length() > 50) {
			this.passwords.setText(" Too long");
			dec = 1;
		}
		 
		if (fnamet.getText().length() < 2) {
			this.fnames.setText(" Too short");
			dec = 1;
		}
		else if (fnamet.getText().length() > 20) {
			this.fnames.setText(" Too long");
			dec = 1;
		}
		
		if (lnamet.getText().length() < 2) {
			this.lnames.setText(" Too short");
			dec = 1;
		}
		else if (lnamet.getText().length() > 20) {
			this.lnames.setText(" Too long");
			dec = 1;
		}
		
		if (emailt.getText().length() < 10) {
			this.emails.setText(" Too short");
			dec = 1;
		}
		else if (emailt.getText().length() > 50) {
			this.emails.setText(" Too long");
			dec = 1;
		}
		else if (emailt.getText().indexOf('@') == -1) {
			this.emails.setText(" Invalid");
			dec = 1;
		}
		
		if (dec == 1)
			return;
    	
    	Auction auction = MainRun.getAuction();
    	int decision = auction.UserRegister(String.valueOf(this.fnamet.getText()), String.valueOf(this.lnamet.getText()), String.valueOf(this.logint.getText()),
				String.valueOf(this.passwordt.getText()), String.valueOf(this.emailt.getText()), this.getConnection());
		if (decision == 0) {
			Stage thisWindow = (Stage) this.login.getScene().getWindow();
			double x = thisWindow.getX() + (thisWindow.getWidth() / 3), y = thisWindow.getY() + (thisWindow.getHeight() / 3);
			thisWindow.close();
 			
 			try {
				auction.save();
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
 			this.success(x, y, thisWindow);
 			return;
		}
		
		if (decision == -1 || decision == -3)
			this.logins.setText(" Username \n taken");
		if (decision == -2 || decision == -3)
			this.emails.setText(" Email taken");
    }
    
    @FXML
    void login(ActionEvent event) {
    	Stage thisWindow = (Stage) login.getScene().getWindow();
    	thisWindow.close();
    	if (this.getLoginStage() != null)
    		this.getLoginStage().show();
		return;
    }
    
    public void success(double x, double y, Stage thisWindow) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/successfull.fxml"));
    	Parent root = (Parent) loader.load();
    	
    	SuccessController sC = loader.getController();
    	sC.setLabel("Registration was", "successfull");
    	
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
    
    @FXML
    void hand(MouseEvent event) {
    	Scene scene = (Scene) login.getScene();
    	scene.setCursor(Cursor.HAND);
    }

    @FXML
    void point(MouseEvent event) {
    	Scene scene = (Scene) login.getScene();
    	scene.setCursor(Cursor.DEFAULT);
    }
    
}