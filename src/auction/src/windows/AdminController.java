package windows;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AdminController implements Initializable {

    @FXML
    private Button Notifications;

    @FXML
    private Button deleteAll;

    @FXML
    private Button deleteLocal;

    @FXML
    private Button logout;

    @FXML
    private Button searchAuction;

    @FXML
    private Button searchUser;
    
    @FXML
    private ImageView notification;
    
    private objects.Admin Admin;
    private Service<Void> service;
    private Stage Window;
    private Connection con;
    
    public Connection getConnection() {
		return con;
	}

	public void setConnection(Connection con) {
		this.con = con;
	}

	public objects.Admin getAdmin() {
		return Admin;
	}

	public void setAdmin(objects.Admin admin) {
		Admin = admin;
	}

	public Stage getWindow() {
		return Window;
	}

	public void setWindow(Stage window) {
		Window = window;
	}

	public Service<Void> getService() {
    	return service;
    }
	
	private boolean decisioner;
    public boolean isDecisioner() {
		return decisioner;
	}
    public ImageView getNotification() {
    	return notification;
    }
	public void setDecisioner(boolean decisioner) {
		this.decisioner = decisioner;
	}
	public void notificationRotate(boolean decisioner) {
		if (decisioner == true)
			notification.setRotate(notification.getRotate() + 5);
		else
			notification.setRotate(notification.getRotate() - 5);
	}
	public void notificationAnimation() { //bell notification as in UserInterfaceController.java
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				int i = 0, j = 0;
				
				while (true) {
					if (i % 2 == 0)
						setDecisioner(true);
					else
						setDecisioner(false);
					
					Platform.runLater(new Runnable() {
	                    @Override
	                    public void run() {
	                    	notificationRotate(isDecisioner());
	                    }
					});
					
					if (getWindow() == null) {
						getNotification().setRotate(0);
						break;
					}
					else if (MainRun.getAuction().getFraudAlert() == 0 || getWindow().isShowing() == false) {
						getNotification().setRotate(0);
						break;
					}
					
					try { Thread.sleep(60); } catch (InterruptedException e) {}
					j++;
					if (j == 6) {
						j = 0;
						i++;
					}
				}
			}
		};
		timer.schedule(task, 1);
	}	
    
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		Service<Void> service = new Service<Void>() {	//Service task on initialisation for notifiaction of admin as in UserInterfaceController.java
	        @Override
	        protected Task<Void> createTask() {
	            return new Task<Void>() {           
	                @Override
	                protected Void call() throws Exception {
	                    //Background work
	                	Thread.sleep(1000);
						int notify = 0;
						if (getWindow() != null)
							MainRun.getAuction().getAdmin().setAdminStage(true);
						
	    	        	while (true) {
	    	        		
	    	        		if (MainRun.getAuction().getFraudAlert() == 0)
	    	        			notify = 0;
	    	        		
	    	        		if (MainRun.getAuction().getFraudAlert() != 0 && notify == 0) {
	    	        			notificationAnimation();
	    	        			notify = 1;
	    	        		}
		                    
		                    if (getService() != null) {
                    			if (getWindow().isShowing() == false) {
                    				getService().cancel();
                    				MainRun.getAuction().getAdmin().setAdminStage(false);
                    			}
                    		}
		                    Thread.sleep(100);
	    	        	}
	                }
	            };
	        }
	    };
	    service.start();
	    this.service = service;
    }

    @FXML
    void Notifications(ActionEvent event) throws IOException {
    	
    	Stage Inter = (Stage) logout.getScene().getWindow();
    	double x = Inter.getX(), y = Inter.getY();
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/NotificationList.fxml"));
		Parent root = (Parent) loader.load();
		
		NotificationController notifyC = loader.getController();
		notifyC.setUsername("Admin");
		notifyC.setNotifications(MainRun.getAuction().getAdmin().getNotifications());
		
		Stage notifyWindow = new Stage();
		Image icon = new Image(getClass().getResourceAsStream("sources/adminIcon.png"));
		notifyWindow.getIcons().add(icon);
		notifyWindow.setScene(new Scene(root));
		notifyWindow.setX(x);
		notifyWindow.setY(y);
		notifyWindow.setResizable(false);
		notifyWindow.show();
		MainRun.getAuction().setFraudAlert(0);
    }

    @FXML
    void deleteAll(ActionEvent event) throws ClassNotFoundException, IOException, SQLException {
    	MainRun.getAuction().delete();
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Blind_auction_database", "root", "12345");
		Statement stmnt = con.createStatement();
		stmnt.execute("TRUNCATE TABLE users");
		stmnt.execute("TRUNCATE TABLE auction_timer");
		stmnt.cancel();
		MainRun.getAuction().save();
		this.success();
    }

    @FXML
    void deleteLocal(ActionEvent event) throws ClassNotFoundException, IOException {
    	MainRun.getAuction().delete();
    	MainRun.getAuction().save();
    	this.success();
    }

    @FXML
    void logout(ActionEvent event) {
    	Stage adminWindow = (Stage) logout.getScene().getWindow();
    	adminWindow.close();
    	this.service.cancel();
    	return;
    }

    @FXML
    void searchAuction(ActionEvent event) throws IOException {
    	
		Stage Inter = (Stage) logout.getScene().getWindow();
		double x = Inter.getX() + (Inter.getX() / 4), y = Inter.getY() + (Inter.getY() / 6);
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/searchInterface.fxml"));
		Parent root = (Parent) loader.load();
		
		SearchInterfaceController searchC = loader.getController();
		searchC.setLabels("Admin", Inter);
		searchC.setConnection(getConnection());
		searchC.setFailNull();
		
		
		Stage searchWindow = new Stage();
		Image icon = new Image(getClass().getResourceAsStream("sources/adminIcon.png"));
		searchWindow.getIcons().add(icon);
		searchWindow.setScene(new Scene(root));
		searchWindow.setX(x);
		searchWindow.setY(y);
		searchWindow.setResizable(false);
		searchWindow.show();
    }

    @FXML
    void searchUser(ActionEvent event) throws IOException {
    	Stage Inter = (Stage) logout.getScene().getWindow();
		double x = Inter.getX() + (Inter.getX() / 4), y = Inter.getY() + (Inter.getY() / 6);
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/FindUser.fxml"));
		Parent root = (Parent) loader.load();
		
		SearchUserController searchC = loader.getController();
		searchC.setConnection(getConnection());
		searchC.setFailNull();
		searchC.IsBanned(false);
		
		
		Stage searchWindow = new Stage();
		Image icon = new Image(getClass().getResourceAsStream("sources/adminIcon.png"));
		searchWindow.getIcons().add(icon);
		searchWindow.setScene(new Scene(root));
		searchWindow.setX(x);
		searchWindow.setY(y);
		searchWindow.setResizable(false);
		searchWindow.show();
    }
    
    @FXML
    void hand(MouseEvent event) {
    	Scene scene = (Scene) logout.getScene();
    	scene.setCursor(Cursor.HAND);
    }
    
    @FXML
    void point(MouseEvent event) {
    	Scene scene = (Scene) logout.getScene();
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
