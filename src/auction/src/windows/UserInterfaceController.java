package windows;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import auctions.Auction;
import auctions.Auctions;
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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UserInterfaceController implements Initializable {

    @FXML
    private Button alist;
    @FXML
    private Button bid;
    @FXML
    private Button buy;
    @FXML
    private Button buylist;
    @FXML
    private Button cancel;
    @FXML
    private Button close;
    @FXML
    private Label euro;
    @FXML
    private Button funds;
    @FXML
    private Button logout;
    @FXML
    private Label money;
    @FXML
    private Button reg;
    @FXML
    private Button reglist;
    @FXML
    private Button reload;
    @FXML
    private Label reloadl;
    @FXML
    private Button search;
    @FXML
    private Label username;
    @FXML
    private Button withdraw;
    @FXML
    private Button notifications;
    @FXML
    private ImageView notification;
    @FXML
    private ImageView reloader;
    
    
    private Auction auction;
    private Stage userWindow;
    private Connection con;
    private Service<Void> service;
    private int currentID;
    private UserInterfaceController userC;
    
    public ImageView getReloadImage() {
    	return reloader;
    }
    
    public UserInterfaceController getUserC() {
		return userC;
	}

	public void setUserC(UserInterfaceController userC) {
		this.userC = userC;
	}

	public int getCurrentID() {
		return currentID;
	}

	public void setCurrentID(int currentID) {
		this.currentID = currentID;
	}

	public String getUsername() {
    	return this.username.getText();
    }
    
    public Service<Void> getService() {
    	return this.service;
    }
    
    public void setReloadNull() {
		this.reloadl.setText("");
	}

	public void setReloadLabel() {	//simple timer to remove the message about reload
		this.reloadl.setText("Reload was successfull");
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                    	setReloadNull();
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

	public void setBalance(double balance) {
    	this.money.setText("" + balance);
    }

	public Stage getUserWindow() {
		return userWindow;
	}

	public void setUserWindow(Stage userWindow) {
		this.userWindow = userWindow;
	}

	public Auction getAuction() {
		return auction;
	}

	public void setAuction() {
		this.auction = MainRun.getAuction();
	}
 
	public void reloadWindowInfo() throws IOException, InterruptedException, SQLException {
		Auction auction = MainRun.getAuction();
		double balance = auction.getBalance(this.username.getText(), this.getConnection());
		balance = (double) Math.round(balance * 100) / 100;
		this.money.setText("" + balance);
		return;
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
	public void notificationRotate(boolean decisioner) {	//rotating image of notifications
		if (decisioner == true)
			notification.setRotate(notification.getRotate() + 5);
		else
			notification.setRotate(notification.getRotate() - 5);
	}
	public void notificationAnimation() {	//Timer to create a simple animation
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				int i = 0, j = 0;
				try { Thread.sleep(1000); } catch (InterruptedException e1) {}
				
				while (true) {
					if (i % 2 == 0)
						setDecisioner(true);
					else
						setDecisioner(false);
					
					Platform.runLater(new Runnable() {
	                    @Override
	                    public void run() {
	                    	notificationRotate(isDecisioner());	//rotates depending on whether i is even or odd
	                    }
					});
					
					if (getUserWindow() == null) {
						getNotification().setRotate(0);
						break;
					}
					else if (MainRun.getAuction().getUsersHT().get(getUsername()).getNotificationCheck() == 0 || getUserWindow().isShowing() == false) {
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


	public void setLabels(String login, double balance) {
    	this.username.setText(login);
    	this.money.setText("" + balance);
    }
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
		Service<Void> service = new Service<Void>() {	//Service task of user interface
	        @Override
	        protected Task<Void> createTask() {
	            return new Task<Void>() {           
	                @Override
	                protected Void call() throws Exception {
	                    //Background work
						int Listener = 0;
						int notify = 0;
	    	        	while (true) {
	    	        		if (MainRun.getAuction().getUsersHT().get(getUsername()).getNotificationCheck() == 0)
	    	        			notify = 0;
	    	        		
	    	        		if (MainRun.getAuction().getUsersHT().get(getUsername()).getNotificationCheck() != 0 && notify == 0) {	//if notificationCheck of user isnt zero anymore, then it start the animation 
	    	        			notificationAnimation();
	    	        			notify = 1;
	    	        		}
	    	        		
		                    final CountDownLatch latch = new CountDownLatch(1);
		                    Platform.runLater(new Runnable() {
		                        @Override
		                        public void run() {
		                            try{
		                            	
		    		                    if (MainRun.getAuction().getUsersHT().get(getUsername()).isBanned() == true) {
		                        			logout(null); }
		    		                    
		    	    	        		int aListener = MainRun.getAuction().getListener();
		    	    	        		
		    	    	        		setCurrentID(MainRun.getAuction().getListenerCurrentID());
		    	    	        		
		                            	if (Listener != aListener && getUserWindow() != null) {
		                            		
		                            		reloadWindowInfo();
		                            		Auctions echoAuction = MainRun.getAuction().getAuctionsHT().get(getCurrentID());
		                            		
		                            		if (aListener == -2 && echoAuction != null) { //depending on listener and user it pops up a pop-up window with notification
		                            			if (echoAuction.getSellername().compareTo(getUsername()) == 0) {
		                            				Stage userInter = (Stage) close.getScene().getWindow();
			                            			double x = userInter.getX() + (userInter.getWidth() / 4), y = userInter.getY() + (userInter.getHeight() / 5);
			                            			notifyUser(x, y, userInter, "Somebody bought your auction", "" + echoAuction.getID(), "Congratulations!", getUserC());
			                            			MainRun.getAuction().getUsersHT().get(getUsername()).getNotifications().add("Somebody bought your auction with ID " + echoAuction.getID() 
			                            			+ " -> Congratulations!");
			                            			MainRun.getAuction().getUsersHT().get(getUsername()).setNotificationCheck(1);
			                            			setCurrentID(-1);
			                            			
		                            			}
		                            			else setCurrentID(-1);
		                            		}
		                            		else if (aListener == -1 && echoAuction != null) {	//depending on listener and user it pops up a pop-up window with notification
		                            			if (echoAuction.getSellername().compareTo(getUsername()) == 0) {
		                            				Stage userInter = (Stage) close.getScene().getWindow();
			                            			double x = userInter.getX() + (userInter.getWidth() / 4), y = userInter.getY() + (userInter.getHeight() / 5);
			                            			notifyUser(x, y, userInter, "Somebody bid on your auction", "" + echoAuction.getID(), 
			                            					"Bid: " + echoAuction.getCurrentPrice() + "€", getUserC());
			                            			MainRun.getAuction().getUsersHT().get(getUsername()).getNotifications().add("Somebody bid on your auction with ID " + echoAuction.getID() 
			                            			+ " -> " + echoAuction.getCurrentPrice() + "€");
			                            			MainRun.getAuction().getUsersHT().get(getUsername()).setNotificationCheck(1);
			                            			setCurrentID(-1);
		                            			}
		                            			else setCurrentID(-1);
		                            		}
		                            		else if (aListener == -6 && echoAuction != null) {	//depending on listener and user it pops up a pop-up window with notification
		                            			if (echoAuction.getSellername().compareTo(getUsername()) == 0) {
		                            				Stage userInter = (Stage) close.getScene().getWindow();
			                            			double x = userInter.getX() + (userInter.getWidth() / 4), y = userInter.getY() + (userInter.getHeight() / 5);
			                            			notifyUser(x, y, userInter, "Bid withdrawal on auction", "" + echoAuction.getID(), "", getUserC());
			                            			MainRun.getAuction().getUsersHT().get(getUsername()).getNotifications().add("Somebody withdrew bid from your auction with ID " + echoAuction.getID());
			                            			MainRun.getAuction().getUsersHT().get(getUsername()).setNotificationCheck(1);
			                            			setCurrentID(-1);
		                            			}
		                            			else setCurrentID(-1);
		                            		}	//depending on listener and user it pops up a pop-up window with notification
		                            		else if (aListener == -3 && echoAuction != null && echoAuction.getBidHistoryHT() != null) {
		                            			if (echoAuction.getBidHistoryHT().get(getUsername()) != null) {
		                            				if ( echoAuction.getBidHistoryHT().get(getUsername()).getBidder().getLogin().compareTo(getUsername()) == 0) {
			                            				Stage userInter = (Stage) close.getScene().getWindow();
				                            			double x = userInter.getX() + (userInter.getWidth() / 4), y = userInter.getY() + (userInter.getHeight() / 5);
				                            			notifyUser(x, y, userInter, "Auction in interest was cancelled", "" + echoAuction.getID(), "", getUserC());
				                            			MainRun.getAuction().getUsersHT().get(getUsername()).getNotifications().add("Auction in interest with ID " + echoAuction.getID() 
				                            			+ " was cancelled");
				                            			MainRun.getAuction().getUsersHT().get(getUsername()).setNotificationCheck(1);
				                            			setCurrentID(-1);
			                            			}
		                            				else setCurrentID(-1);
		                            			}
		                            			else setCurrentID(-1);
		                            		}	//depending on listener and user it pops up a pop-up window with notification
		                            		else if (aListener == -4 && echoAuction != null && echoAuction.getBidHistoryHT() != null) {
		                            			if (echoAuction.getBidHistoryHT().get(getUsername()) != null) {
		                            				if ( echoAuction.getBidHistoryHT().get(getUsername()).getBidder().getLogin().compareTo(getUsername()) == 0) {
				                            			Stage userInter = (Stage) close.getScene().getWindow();
				                            			double x = userInter.getX() + (userInter.getWidth() / 4), y = userInter.getY() + (userInter.getHeight() / 5);
				                            			String winner = "";
				                            			if (echoAuction.getBuyername().compareTo(getUsername()) == 0)
				                            				winner = "You won!";
				                            			notifyUser(x, y, userInter, "Auction in interest was closed", "" + echoAuction.getID(), winner, getUserC());
				                            			MainRun.getAuction().getUsersHT().get(getUsername()).getNotifications().add("Auction in interest with ID " + echoAuction.getID() 
				                            			+ " was closed -> " + winner);
				                            			MainRun.getAuction().getUsersHT().get(getUsername()).setNotificationCheck(1);
				                            			setCurrentID(-1);
			                            			}
		                            				else setCurrentID(-1);
		                            			}
		                            			else setCurrentID(-1);
		                            		}
		                            		else setCurrentID(-1);
		                            	}
		                                //FX Stuff done here
		                            } catch (IOException | InterruptedException | SQLException e) {
										e.printStackTrace();
									}finally{
		                                latch.countDown();
		                            }
		                        }
		                    });
		                    Thread.sleep(100);
		                    latch.await();
		                    
		                    if (getService() != null) {
                    			if (getUserWindow().isShowing() == false) {	//if window isnt showing anymore it cancels the service
                    				MainRun.getAuction().getUserCounter().subLoggedUsers();
                    				getService().cancel();
                    			}
                    		}
		                    
		                    if (getCurrentID() == -1) {
		                    	MainRun.getAuction().getUserCounter().Queue();	//adds into user counter itself
		                    	while (MainRun.getAuction().getListener() != 0) {
		                    		Thread.sleep(100);
		                    	}
		                    }
		                    
	    	        	}
	                }
	            };
	        }
	    };
	    service.start();
	    this.service = service;
    }
    
    
    
    
    @FXML
    void notificationList(ActionEvent event) throws IOException, SQLException {
    	
    	Stage UserInter = (Stage) close.getScene().getWindow();
    	double x = UserInter.getX(), y = UserInter.getY();
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/NotificationList.fxml"));
		Parent root = (Parent) loader.load();
		
		NotificationController notifyC = loader.getController();
		notifyC.setUsername(this.getUsername());
		notifyC.setNotifications(MainRun.getAuction().getUsersHT().get(this.getUsername()).getNotifications());
		
		Stage notifyWindow = new Stage();
		Image icon = new Image(getClass().getResourceAsStream("sources/icon.png"));
		notifyWindow.getIcons().add(icon);
		notifyWindow.setScene(new Scene(root));
		notifyWindow.setX(x);
		notifyWindow.setY(y);
		notifyWindow.setResizable(false);
		notifyWindow.show();
		MainRun.getAuction().getUsersHT().get(getUsername()).setNotificationCheck(0);

    }    
    
    @FXML
    void alist(ActionEvent event) throws IOException, SQLException {
    	Auction auction = MainRun.getAuction();
    	List<Auctions> auctions = auction.getAuctions();
    	
    	Stage UserInter = (Stage) close.getScene().getWindow();
    	double x = UserInter.getX(), y = UserInter.getY();
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/auctionList.fxml"));
		Parent root = (Parent) loader.load();
		
		AuctionListController alC = loader.getController();
		alC.setAuctions(auctions);
		alC.setConnection(getConnection());
		alC.setInfo(UserInter, username.getText());
		alC.setUserC(this);
		alC.hideScroll();
		
		Stage alWindow = new Stage();
		Image icon = new Image(getClass().getResourceAsStream("sources/icon.png"));
		alWindow.getIcons().add(icon);
		alWindow.setScene(new Scene(root));
		alWindow.setX(x);
		alWindow.setY(y);
		alWindow.setResizable(false);
		alWindow.show();

    }
    
    @FXML
    void withdraw(ActionEvent event) throws IOException {
		Stage UserInter = (Stage) username.getScene().getWindow();
		double x = UserInter.getX() + (UserInter.getX() / 4), y = UserInter.getY() + (UserInter.getY() / 6);
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/WithdrawBidAuction.fxml"));
		Parent root = (Parent) loader.load();
		
		WithdrawBidController bidC = loader.getController();
		bidC.setInfo(username.getText());
		bidC.setConnection(getConnection());
		
		Stage bidWindow = new Stage();
		Image icon = new Image(getClass().getResourceAsStream("sources/icon.png"));
		bidWindow.getIcons().add(icon);
		bidWindow.setScene(new Scene(root));
		bidWindow.setX(x);
		bidWindow.setY(y);
		bidWindow.setResizable(false);
		bidWindow.show();
    }    
    
    @FXML
    void bid(ActionEvent event) throws IOException {
		Stage UserInter = (Stage) username.getScene().getWindow();
		double x = UserInter.getX() + (UserInter.getX() / 4), y = UserInter.getY() + (UserInter.getY() / 6);
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/BidAuction.fxml"));
		Parent root = (Parent) loader.load();
		
		BidController bidC = loader.getController();
		bidC.setInfo(username.getText());
		bidC.setConnection(getConnection());
		
		Stage bidWindow = new Stage();
		Image icon = new Image(getClass().getResourceAsStream("sources/icon.png"));
		bidWindow.getIcons().add(icon);
		bidWindow.setScene(new Scene(root));
		bidWindow.setX(x);
		bidWindow.setY(y);
		bidWindow.setResizable(false);
		bidWindow.show();
    }    
    
    @FXML
    void buy(ActionEvent event) throws IOException {
		Stage UserInter = (Stage) username.getScene().getWindow();
		double x = UserInter.getX() + (UserInter.getX() / 4), y = UserInter.getY() + (UserInter.getY() / 6);
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/BuyAuction.fxml"));
		Parent root = (Parent) loader.load();
		
		BuyController buyC = loader.getController();
		buyC.setInfo(username.getText());
		buyC.setConnection(getConnection());
		
		Stage buyWindow = new Stage();
		Image icon = new Image(getClass().getResourceAsStream("sources/icon.png"));
		buyWindow.getIcons().add(icon);
		buyWindow.setScene(new Scene(root));
		buyWindow.setX(x);
		buyWindow.setY(y);
		buyWindow.setResizable(false);
		buyWindow.show();
    }    
    
    @FXML
    void buylist(ActionEvent event) throws IOException, SQLException {
    	Stage UserInter = (Stage) close.getScene().getWindow();
    	double x = UserInter.getX(), y = UserInter.getY();
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/auctionBuyList.fxml"));
		Parent root = (Parent) loader.load();
		
		AuctionBuyListController buyListC = loader.getController();
		buyListC.setUserC(this);
		buyListC.setConnection(getConnection());
		buyListC.setInfo(UserInter, username.getText());
		buyListC.setAuctions(username.getText());
		buyListC.setUserWindow(UserInter);
		buyListC.hideScroll();
		
		Stage ablWindow = new Stage();
		Image icon = new Image(getClass().getResourceAsStream("sources/icon.png"));
		ablWindow.getIcons().add(icon);
		ablWindow.setScene(new Scene(root));
		ablWindow.setX(x);
		ablWindow.setY(y);
		ablWindow.setResizable(false);
		ablWindow.show();
    }    
    
    @FXML
    void cancel(ActionEvent event) throws IOException {
    	Stage UserInter= (Stage) username.getScene().getWindow();
    	double x = UserInter.getX() + (UserInter.getX() / 4), y = UserInter.getY() + (UserInter.getY() / 6);
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/CancelAuction.fxml"));
		Parent root = (Parent) loader.load();
		
		CancelController acC = loader.getController();
		acC.setUsername((username.getText()));
		acC.setConnection(getConnection());
		
		Stage acWindow = new Stage();
		Image icon = new Image(getClass().getResourceAsStream("sources/icon.png"));
		acWindow.getIcons().add(icon);
		acWindow.setScene(new Scene(root));
		acWindow.setX(x);
		acWindow.setY(y);
		acWindow.setResizable(false);
		acWindow.show();
    }        
    
    @FXML
    void close(ActionEvent event) throws IOException {
    	Stage UserInter = (Stage) username.getScene().getWindow();
    	double x = UserInter.getX() + (UserInter.getX() / 4), y = UserInter.getY() + (UserInter.getY() / 6);
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/CloseAuction.fxml"));
		Parent root = (Parent) loader.load();
		
		CloseController acsC = loader.getController();
		acsC.setInfo(username.getText());
		acsC.setConnection(getConnection());
		
		Stage acsWindow = new Stage();
		Image icon = new Image(getClass().getResourceAsStream("sources/icon.png"));
		acsWindow.getIcons().add(icon);
		acsWindow.setScene(new Scene(root));
		acsWindow.setX(x);
		acsWindow.setY(y);
		acsWindow.setResizable(false);
		acsWindow.show();
    }    
    
    @FXML
    void funds(ActionEvent event) throws IOException, SQLException {
    	Auction auction = MainRun.getAuction();
    	Stage UserWindow = (Stage) username.getScene().getWindow();
    	double x = UserWindow.getX() + (UserWindow.getWidth() / 2), y = UserWindow.getY() + (UserWindow.getHeight() / 4);
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/addFunds.fxml"));
		Parent root = (Parent) loader.load();
		
		AddFundsController addC = loader.getController();
		addC.set(this, username.getText(), "" + auction.getBalance(username.getText(), this.getConnection()) );
		addC.setConnection(getConnection());
		
		Stage addWindow = new Stage();
		Image icon = new Image(getClass().getResourceAsStream("sources/icon.png"));
		addWindow.getIcons().add(icon);
		addWindow.setScene(new Scene(root));
		addWindow.setX(x);
		addWindow.setY(y);
		addWindow.setResizable(false);
		addWindow.show();

    }    
    
    @FXML
    void logout(ActionEvent event) {
    	Stage userWindow = (Stage) username.getScene().getWindow();
    	userWindow.close();
    	this.setUserWindow(null);
    	this.getService().cancel();
    	MainRun.getAuction().getUserCounter().subLoggedUsers();
    	return;
    }    
    
    @FXML
    void reg(ActionEvent event) throws IOException {
		
		Stage UserInter = (Stage) close.getScene().getWindow();
    	double x = UserInter.getX() + (UserInter.getX() / 3), y = UserInter.getY();
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/auctionRegInterface.fxml"));
		Parent root = (Parent) loader.load();
		
		RegisterAuctionController regAuctionC = loader.getController();
		regAuctionC.setConnection(this.getConnection());
		regAuctionC.setUserC(this);
		regAuctionC.setUsername(username.getText());
		regAuctionC.setNull();
		
		
		Stage arWindow = new Stage();
		Image icon = new Image(getClass().getResourceAsStream("sources/icon.png"));
		arWindow.getIcons().add(icon);
		arWindow.setScene(new Scene(root));
		arWindow.setX(x);
		arWindow.setY(y);
		arWindow.setResizable(false);
		arWindow.show();

    }    
    
    @FXML
    void reglist(ActionEvent event) throws IOException, SQLException {
    	
    	Stage UserInter = (Stage) close.getScene().getWindow();
    	double x = UserInter.getX(), y = UserInter.getY();
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/auctionRegList.fxml"));
		Parent root = (Parent) loader.load();
		
		AuctionRegListController regListC = loader.getController();
		regListC.setConnection(getConnection());
		regListC.setInfo(UserInter, this.username.getText());
		regListC.setAuctions(this.username.getText());
		regListC.setUserWindow(UserInter);
		regListC.setUserC(this);
		regListC.hideScroll();
		
		
		Stage arlWindow = new Stage();
		Image icon = new Image(getClass().getResourceAsStream("sources/icon.png"));
		arlWindow.getIcons().add(icon);
		arlWindow.setScene(new Scene(root));
		arlWindow.setX(x);
		arlWindow.setY(y);
		arlWindow.setResizable(false);
		arlWindow.show();
    }
    
    private int breaker = 0;
    public int getBreaker() {
    	return breaker;
    }
    public void setBreaker(int input) {
    	breaker = input;
    }
    @FXML
    void reload(ActionEvent event) throws IOException, InterruptedException, SQLException {
    	Scene scene = (Scene) username.getScene();
    	scene.setCursor(Cursor.WAIT);	//changing cursor

		Timer timer = new Timer();	//setting up timer to do the animation with the reload image
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
            	
				getReloadImage().setRotate(0);
            	while (true) {
            		Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                        	getReloadImage().setRotate(getReloadImage().getRotate() + 5);
                        	setBreaker(getBreaker() + 1);
                        	if (getBreaker() == 71)
                        		setReloadLabel();
                        }
    				});
            		try { Thread.sleep(20); } catch (InterruptedException e) { e.printStackTrace(); }
            		if (getBreaker() == 71) {
            			scene.setCursor(Cursor.DEFAULT);
                    	setBreaker(0);
                    	break;
            		}
            	}
			}
		};
		timer.schedule(task, 1);
    	Auction auction = MainRun.getAuction();
    	double balance = auction.getBalance(username.getText(), this.getConnection());
    	balance = (double) Math.round(balance * 100) / 100;
    	this.setBalance(balance);
    	Thread.sleep(100);
    }	
    
    @FXML
    void search(ActionEvent event) throws IOException {
    	
		Stage UserInter = (Stage) close.getScene().getWindow();
		double x = UserInter.getX() + (UserInter.getX() / 4), y = UserInter.getY() + (UserInter.getY() / 6);
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/searchInterface.fxml"));
		Parent root = (Parent) loader.load();
		
		SearchInterfaceController searchC = loader.getController();
		searchC.setUserC(this);
		searchC.setLabels(username.getText(), UserInter);
		searchC.setFailNull();
		searchC.setConnection(getConnection());
		
		
		Stage searchWindow = new Stage();
		Image icon = new Image(getClass().getResourceAsStream("sources/icon.png"));
		searchWindow.getIcons().add(icon);
		searchWindow.setScene(new Scene(root));
		searchWindow.setX(x);
		searchWindow.setY(y);
		searchWindow.setResizable(false);
		searchWindow.show();
    }    
    
    public void notifyUser(double x, double y, Stage thisWindow, String info1, String info2, String info3, UserInterfaceController userC) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("sources/NotificationPopUp.fxml"));
    	Parent root = (Parent) loader.load();
    	
    	NotificationPopUp npC = loader.getController();
    	npC.setConnection(getConnection());
    	npC.setID(Integer.parseInt(info2));
    	npC.setUserC(userC);
    	npC.setUsername(getUsername());
    	npC.setUserInter(getUserWindow());
    	npC.setNotification(info1, info2, info3);
    	
    	
    	Stage npWindow = new Stage();
    	Image icon = new Image(getClass().getResourceAsStream("sources/icons8_google_alerts_48px.png"));
    	npWindow.getIcons().add(icon);
    	npWindow.setScene(new Scene(root));
    	npWindow.setX(x);
    	npWindow.setY(y);
    	npWindow.setResizable(false);
    	npWindow.initModality(Modality.APPLICATION_MODAL);
    	npWindow.initOwner(thisWindow);
    	npWindow.setAlwaysOnTop(true);
    	npC.setInter(npWindow);
    	npWindow.show();
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