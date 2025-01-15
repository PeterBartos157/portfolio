package windows;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class NotificationPopUp implements Initializable {

    @FXML
    private ImageView black;
    
    @FXML
    private Label info1;

    @FXML
    private Label info2;
    
    @FXML
    private Label info3;

    @FXML
    private ImageView white;
    
    @FXML
    private AnchorPane notification;
    
    private boolean decisioner;
    private Stage inter;
    private Service<Void> service;
    private int ID;
    private UserInterfaceController UserC;
    private Stage UserInter;
    private String username;
    private Connection con;
    
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Stage getUserInter() {
		return UserInter;
	}

	public void setUserInter(Stage userInter) {
		UserInter = userInter;
	}

	public Connection getConnection() {
		return con;
	}

	public void setConnection(Connection con) {
		this.con = con;
	}

	public UserInterfaceController getUserC() {
		return UserC;
	}

	public void setUserC(UserInterfaceController userC) {
		this.UserC = userC;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public Service<Void> getService() {
		return service;
	}
    
    public Stage getInter() {
		return inter;
	}

	public void setInter(Stage Inter) {
		this.inter = Inter;
	}
    
    public boolean isDecisioner() {
		return decisioner;
	}

	public void setDecisioner(boolean decisioner) {
		this.decisioner = decisioner;
	}

	public void setVisible(boolean decision) {
    	if (decision == true) {
    		this.black.setVisible(true);
    		this.white.setVisible(false);
    	}
    	else {
    		this.black.setVisible(false);
    		this.white.setVisible(true);
    	}
    }
	
	public void setColor(boolean decision) {
    	if (decision == true)
    		this.info3.setTextFill(Color.GREEN);
    	else
    		this.info3.setTextFill(Color.DARKGREEN);
    }
    
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		Service<Void> service = new Service<Void>() {
	        @Override
	        protected Task<Void> createTask() {
	            return new Task<Void>() {           
	                @Override
	                protected Void call() throws Exception {
	                    //Background work
	                	int i = 0;
	    	        	while (true) {
	    	        		Thread.sleep(500);
	    	        		if (i % 2 == 0)
	    						setDecisioner(true);
	    					else
	    						setDecisioner(false);
		                    final CountDownLatch latch = new CountDownLatch(1);
		                    Platform.runLater(new Runnable() {
		                        @Override
		                        public void run() {
		                            try{
		                            	setVisible(isDecisioner());
		                            	setColor(isDecisioner());
		                        	    Stage inter = getInter();
		                        	    if (inter != null) {
		                        	    	if (inter.isShowing() == false)
		                        	    		getService().cancel();
		                        	    }
		                                //FX Stuff done here
		                            }finally{
		                                latch.countDown();
		                            }
		                        }
		                    });
		                    latch.await();
		                    i++;
		                    //Keep with the background work
	    	        	}
	                }
	            };
	        }
	    };
	    service.start();
	    this.service = service;
    }
    
    public void setNotification(String notify1, String notify2, String notify3) {
    	this.info1.setText(notify1);
    	this.info2.setText(notify2);
    	this.info3.setText(notify3);
    	this.info3.setTextFill(Color.GREEN);
    }
    
    @FXML
    void close0(ScrollEvent event) {
    	Stage inter = (Stage) notification.getScene().getWindow();
    	inter.close();
    	this.service.cancel();
    	return;
    }
    
    @FXML
    void close1(KeyEvent event) {
    	Stage inter = (Stage) notification.getScene().getWindow();
    	inter.close();
    	this.service.cancel();
    	return;
    }
    
    @FXML
    void close2(MouseEvent event) {
    	Stage inter = (Stage) notification.getScene().getWindow();
    	inter.close();
    	this.service.cancel();
    	return;
    }
    
    @FXML
    void search(MouseEvent event) throws IOException {
    	Stage Inter = (Stage) info1.getScene().getWindow();
		double x = Inter.getX() + (Inter.getX() / 4), y = Inter.getY() + (Inter.getY() / 6);
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/searchInterface.fxml"));
		Parent root = (Parent) loader.load();
		
		SearchInterfaceController searchC = loader.getController();
		searchC.setTID(ID);
		searchC.setUserC(this.getUserC());
		searchC.setLabels(username, UserInter);
		searchC.setFailNull();
		searchC.setConnection(getConnection());
		Inter.close();
		//searchC.setID(this.ID);
		
		
		Stage searchWindow = new Stage();
		Image icon = new Image(getClass().getResourceAsStream("sources/icon.png"));
		searchWindow.getIcons().add(icon);
		searchWindow.setScene(new Scene(root));
		searchWindow.setX(x);
		searchWindow.setY(y);
		searchWindow.setResizable(false);
		searchWindow.show();
		
		searchC.search(null);
    	this.service.cancel();
    	return;
    }
    
    @FXML
    void hand(MouseEvent event) {
    	Scene scene = (Scene) info1.getScene();
    	scene.setCursor(Cursor.HAND);
    }

    @FXML
    void point(MouseEvent event) {
    	Scene scene = (Scene) info1.getScene();
    	scene.setCursor(Cursor.DEFAULT);
    }

}