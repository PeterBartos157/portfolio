package objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class User implements Serializable {
	private String fname;
	private String lname;
	private String login;
	private int password;
	private String email;
	private double balance;
	private List<String> notifications;
	private List<String> notificationIDs;
	private int notificationCheck = 0;
	private boolean banned = false;
	
	public User () {
	}
	
	public User (String fname, String lname, String login, int password, String email) {
		setFName(fname);
		setLName(lname);
		setLogin(login);
		setPassword(password);
		setEmail(email);
		if (this.notifications == null)
			this.notifications = new ArrayList<>();
	}
	
	public void setFName(String fname) {
		this.fname = fname;
	}
	
	public void setLName(String lname) {
		this.lname = lname;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public void setPassword(int password) {
		this.password = password;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getFName() {
		return this.fname;
	}
	
	public String getLName() {
		return this.lname;
	}
	
	public String getLogin() {
		return this.login;
	}
	
	public int getPassword() {
		return this.password;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void addFunds(double Funds) {
		Funds = Math.round(Funds * 100.0) / 100.0;
		this.balance = balance + Funds;
		this.balance = Math.round(balance * 100.0) / 100.0;
	}

	public void setBalance(double balance) {
		balance = Math.round(balance * 100.0) / 100.0;
		this.balance = balance;
	}

	public double getBalance() {
		return this.balance;
	}

	public List<String> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<String> notifications) {
		this.notifications = notifications;
	}

	public List<String> getNotificationIDs() {
		return notificationIDs;
	}

	public void setNotificationIDs(List<String> notificationIDs) {
		this.notificationIDs = notificationIDs;
	}

	public int getNotificationCheck() {
		return notificationCheck;
	}

	public void setNotificationCheck(int notificationCheck) {
		this.notificationCheck = notificationCheck;
	}

	public boolean isBanned() {
		return banned;
	}

	public void setBanned(boolean banned) {
		this.banned = banned;
	}
	
}
