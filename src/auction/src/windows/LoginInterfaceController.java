package windows;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import auctions.*;
import javafx.application.Platform;
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
import javafx.stage.Stage;

public class LoginInterfaceController {

    @FXML private Button login;
    @FXML private PasswordField password;
    @FXML private Label passwordl;
    @FXML private Button register;
    @FXML private Label registerl;
    @FXML private TextField username;
    @FXML private Label usernamel;
    @FXML private Button close;
    @FXML private Label fail;
    private Connection con;
    
    public void setFailNull() {
		this.fail.setText(" ");
	}

	public void setFail(boolean decisioner) {
		if (decisioner == true)
			this.fail.setText("Wrong login or password...");
		else
			this.fail.setText("You have been banned...");
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
    
    public Connection getConnection() {
		return con;
	}

	public void setConnection(Connection con) {
		this.con = con;
	}


    @FXML
    void login(ActionEvent event) throws IOException, InterruptedException, SQLException {
    	this.setFailNull();
    	
    	Auction auction = MainRun.getAuction();
			int login = auction.UserLogin(String.valueOf(username.getText()), String.valueOf(password.getText()), this.getConnection());
			if (login == 0) {
				double balance = auction.getBalance(username.getText(), this.getConnection());
				balance = (double) Math.round(balance * 100) / 100;
				System.out.println("Login successfull");
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/userInterface.fxml"));
				Parent root = (Parent) loader.load();
				
				UserInterfaceController userC = loader.getController();
				userC.setUserC(userC);
				userC.setLabels(username.getText(), balance);
				userC.setReloadNull();
				
				Stage userWindow = new Stage();
				Image icon = new Image(getClass().getResourceAsStream("sources/icon.png"));
				userWindow.getIcons().add(icon);
				userWindow.setScene(new Scene(root));
				userWindow.setResizable(false);
				userWindow.show();
				userC.setUserWindow(userWindow);
				userC.setConnection(getConnection());
				MainRun.getAuction().getUserCounter().countLoggedUsers();

			}
			else if (login == 1) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/AdminInterface.fxml"));
				Parent root = (Parent) loader.load();
				
				AdminController adminC = loader.getController();
				adminC.setConnection(this.getConnection());
				
				Stage adminWindow = new Stage();
				Image icon = new Image(getClass().getResourceAsStream("sources/adminIcon.png"));
				adminWindow.getIcons().add(icon);
				adminWindow.setScene(new Scene(root));
				adminWindow.setResizable(false);
				adminWindow.show();
				adminC.setWindow(adminWindow);
			}
			else if (login == -2) {
				System.out.println("You have been banned from this application...");
				this.setFail(false);
			}
			else {
				System.out.println("Wrong login or password. Try again...");
				this.setFail(true);
			}
			username.setText("");
			password.setText("");
    }

    @FXML
    void register(ActionEvent event) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/registerInterface.fxml"));
		Parent root = (Parent) loader.load();
		
		RegisterInterfaceController regInterC = loader.getController();
		regInterC.setNull();
		regInterC.setConnection(getConnection());
		
		Stage regWindow = new Stage();
		Image icon = new Image(getClass().getResourceAsStream("sources/icon.png"));
		regWindow.getIcons().add(icon);
		regWindow.setScene(new Scene(root));
		regWindow.setResizable(false);
		Stage thisWindow = (Stage) login.getScene().getWindow();
		regInterC.setLoginStage(thisWindow);
		regWindow.show();
    }
    
    @FXML
    void close(ActionEvent event) {
    	Auction auction = MainRun.getAuction();
    	try {
    		if (auction == null)
    			return;
    		else
    			auction.save();
		} catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
		}
    	Stage LoginInter = (Stage) close.getScene().getWindow();
    	LoginInter.hide();
    	LoginInter.close();
    	System.exit(0);
    	return;
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
