package objects;

import java.io.Serializable;
import windows.MainRun;

@SuppressWarnings("serial")
public class UserCounter implements Serializable {	//Simple user counter
	private int counter = 0;
	private int check = 0;

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	
	public void countLoggedUsers() {
		this.counter++;
	}
	
	public void subLoggedUsers() {
		this.counter--;
		this.check--;
		if (this.counter < 0)
			this.counter = 0;
		if (this.check < 0)
			this.check = 0;
	}

	public int getCheck() {
		return check;
	}

	public void setCheck(int check) {
		this.check = check;
	}
	
	public void Queue() {
		this.check++;
		if (check == counter) {
			MainRun.getAuction().setListener(0);
			this.check = 0;
		}
	}
	
}
