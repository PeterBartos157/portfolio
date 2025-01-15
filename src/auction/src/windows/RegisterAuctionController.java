package windows;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import auctions.Auction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RegisterAuctionController {

    @FXML
    private TextField aname;

    @FXML
    private Button close;

    @FXML
    private DatePicker date;

    @FXML
    private TextArea idesc;

    @FXML
    private TextField iname;

    @FXML
    private TextField iprice;

    @FXML
    private Button submit;
    
    @FXML
    private Label items;

    @FXML
    private Label names;

    @FXML
    private Label prices;
    
    @FXML
    private Label dates;

    @FXML
    private Label descs;
    
    private String username;
    private Stage parentWindow;
    private UserInterfaceController userC;
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

	public void setUserC(UserInterfaceController userC) {
		this.userC = userC;
	}
    
    public Stage getParentWindow() {
		return parentWindow;
	}

	public void setParentWindow(Stage parentWindow) {
		this.parentWindow = parentWindow;
	}

	public void setNull() {
    	this.items.setText("");
    	this.names.setText("");
    	this.prices.setText("");
    	this.dates.setText("");
    	this.descs.setText("");
    }
    
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@FXML
    void close(ActionEvent event) {
    	Stage arInter = (Stage) close.getScene().getWindow();
    	arInter.close();
    	return;
    }

    @FXML
    void submit(ActionEvent event) throws InterruptedException, IOException, ClassNotFoundException, NumberFormatException, SQLException {
    	this.setNull();
    	
    	int dec = 0;
    	if (aname.getText().length() < 5) {
			this.names.setText(" Too\n short");
			dec = 1;
		}
		else if (aname.getText().length() > 20) {
			this.names.setText(" Too\n long");
			dec = 1;
		}
		
		 if (iname.getText().length() < 5) {
			this.items.setText(" Too\n short");
			dec = 1;
		 }
		else if (iname.getText().length() > 30) {
			this.items.setText(" Too\n long");
			dec = 1;
		}
		
		if (idesc.getText().length() < 10) {
			this.descs.setText(" Too\n short");
			dec = 1;
		}
		else if (idesc.getText().length() > 300) {
			this.descs.setText(" Too\n long");
			dec = 1;
		}
		if (iprice.getText().compareTo("") == 0 || Double.parseDouble(iprice.getText()) < 0.1) {
			this.prices.setText(" Small\n price");
			dec = 1;
		}
		
		if (date.getValue() == null) {
			return;
		}
		java.sql.Date sqlDate =java.sql.Date.valueOf(date.getValue());
        java.util.Date date = new java.util.Date(sqlDate.getTime());
        Date now = new Date();
        if (date.compareTo(now) < 0) {
        	this.dates.setText(" Invalid");
        	dec = 1;
        }
        
		if (dec == 1)
			return;
		
    	if (aname.getText().compareTo("") == 0 || iname.getText().compareTo("") == 0 || idesc.getText().compareTo("") == 0 || iprice.getText().compareTo("") == 0 || date == null)
			return;
    	Auction auction = MainRun.getAuction();
    	int ID = (int) (Math.random() * ((9999999 - 999999) + 1)) + 999999;
    	int decision = auction.SellerSell(date, getUsername(), aname.getText(), iname.getText(), idesc.getText(), ID, Double.parseDouble(iprice.getText()), this.getConnection());
    	Stage arInter = (Stage) close.getScene().getWindow();
		double x = arInter.getX() + (arInter.getWidth() / 3), y = arInter.getY() + (arInter.getHeight() / 5);
    	if (decision == 0) {
    		arInter.close();
    		if (this.getParentWindow() != null)
				this.getParentWindow().close();
			success(x, y, arInter);
        	return;
    	}
    	else {
    		this.fail(x, y, arInter, "Registration failed", "Something went", "wrong");
    		return;
    	}
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