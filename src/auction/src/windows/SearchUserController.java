package windows;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import objects.User;

public class SearchUserController {

    @FXML
    private Button back;

    @FXML
    private TextArea balance;

    @FXML
    private Button ban;

    @FXML
    private TextArea email;

    @FXML
    private Label fail;

    @FXML
    private TextArea fname;

    @FXML
    private TextField id;

    @FXML
    private CheckBox isBanned;

    @FXML
    private Label label;

    @FXML
    private TextArea lname;

    @FXML
    private TextArea login;

    @FXML
    private Button search;

    @FXML
    private Button unban;
    
    private Connection con;
    
    public void setInfo(String login, String fname, String lname, String email, double balance, boolean decision) {
    	this.login.setText(login);
    	this.lname.setText(fname);
    	this.fname.setText(lname);
    	this.email.setText(email);
    	this.balance.setText("" + balance);
    	this.ban.setDisable(decision);
    	this.unban.setDisable(decision);
    }
    
    public void IsBanned(boolean decision) {
    	this.isBanned.setSelected(decision);
    }
   
    
    public Connection getConnection() {
		return con;
	}

	public void setConnection(Connection con) {
		this.con = con;
	}

	public void setFail() {
		this.fail.setText("User not found");
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

    @FXML
    void back(ActionEvent event) {
    	Stage searchWindow = (Stage) search.getScene().getWindow();
    	searchWindow.close();
    	return;
    }

    @FXML
    void search(ActionEvent event) throws IOException {
    	this.setFailNull();
    	if (id.getText().compareTo("") != 0) {
    		
    		if (MainRun.getAuction().getUsersHT() == null) {
    			this.setFail();
        		return;
    		}
    		
    		User user = MainRun.getAuction().getUsersHT().get(id.getText());
    		if (user == null) {
    			this.setFail();
        		return;
    		}
    		this.setFailNull();
    		this.setInfo(user.getLogin(), user.getFName(), user.getLName(), user.getEmail(), user.getBalance(), false);
    		this.IsBanned(user.isBanned());
    	}
    	else
    	this.setFailNull();
    }

    @FXML
    void unban(ActionEvent event) throws SQLException {
    	MainRun.getAuction().getAdmin().unbanUser(this.login.getText(), this.getConnection());
    	this.IsBanned(MainRun.getAuction().getUsersHT().get(this.login.getText()).isBanned());
    }
    
    @FXML
    void ban(ActionEvent event) throws SQLException {
    	MainRun.getAuction().getAdmin().banUser(this.login.getText(), this.getConnection());
    	this.IsBanned(MainRun.getAuction().getUsersHT().get(this.login.getText()).isBanned());
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