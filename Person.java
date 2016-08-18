import java.util.*;

public class Person {
	
	private int life;
	private int x;
	private int y;
	private int z;
	private ArrayList<Item> inventory;

	public Person(int l) {
		life = l;
		inventory = new ArrayList<Item>(3);
	}
	
	public boolean isDead(){
		if(life==0) {
			return true;
		}
		return false;
	}

	public int getLife() {
		return life;
	}

	public void attacked(int damage) {
		life = life - damage;
	}

	public void setX(int value) {
		x = value;
	}

	public void setY(int value) {
		y = value;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getXY() {
		//converts x,y to xy
		return ((10*x)+y);
	}

	public String getProperties() {
		String properties = life+" "+x+" "+y;
		return properties;
	}

	public void setProperties(String property) {
		String[] properties = property.split(" ");
		life = Integer.parseInt(properties[0]);
		x = Integer.parseInt(properties[1]);
		y = Integer.parseInt(properties[2]);
	}

	public void printInventory(String person) {
		Commands.print(person + " inventory:");
		for (Item item : inventory){
			Commands.print(item.getType());
		}
	}

	public boolean inventoryFull() {
		if (inventory.size()<4) {
			return false;
		}
		return true;
	}

	public void takeItem (Item item) {
		if (inventory.size()<4){
			inventory.add(item);
		} else {
			Commands.print("Inventory full.");
		}
	}

	public void dropItem (Item item) {
		if (inventory.contains(item)) {
			inventory.remove(item);
		} else {
			Commands.print("You don't have this item in your inventory.");
		}
	}

	public boolean hasItem(Item item) {
		
		if (inventory.contains(item)){
			return true;
		}
		return false;
	}

	public void getShot() {
		//decrease life
		if (Math.random()<0.8) {
			life--;
			Commands.print("You've been shot. You health has decreased to " + life + ".");
		} else {
			Commands.print("You barely missed that bullet!");
		}
	}

	public void shootEnemy() {
		//decrease life
		if (Math.random()<0.8) {
			life--;
			Commands.print("You hit your target. Target's health has decreased to " + life + ".");
		} else {
			Commands.print("You  missed your target.");
		}
	}
}