package objects;

import java.io.*;

@SuppressWarnings("serial")
public class Item implements Serializable {
	private String Name;
	private String Description;
	private boolean boughtState;
	private int ID;
	private double basePrice;
	
	public Item(String Name, String Description, int ID, double Price)	{
		setDetails( Name, Description, ID, Price);
		setBoughtState(false);
	}
	
	public void setDetails(String Name, String Description, int ID, double basePrice)  {
		this.ID = ID;
		this.basePrice = basePrice;
		this.Name = Name;
		this.Description = Description;
	}
	
	public void setBoughtState(boolean boughtState) {
		this.boughtState = boughtState;
	}
	
	public int getID() {
		return this.ID;
	}
	
	public double getBasePrice() {
		return this.basePrice;
	}
	
	public String getName() {
		return this.Name;
	}
	
	public String getDesc() {
		return this.Description;
	}
	
	public boolean isBought() {
		return this.boughtState;
	}
}
