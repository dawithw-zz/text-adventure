import java.io.*;
import java.lang.ArrayIndexOutOfBoundsException;

public class Action {
	//person
	private Person player;
	private Person target;
	private Person guard1;
	private Person guard2;
	private Person guard3;
	private Person delivery;
	//location
	private Location location;
	//disguise
	private boolean alert;
	private boolean alertFull;
	private boolean disguise;
	//items
	private Item basementKey;
	private Item carKey;
	private Item crate;
	private Item mail;
	private Door door;
	private Item gun;
	private Item knife;
	//mission
	private boolean basementVisited;
	private boolean targetKilled;

	public Action() {
		//defaults

		//add world
		location = new Location();
		//create people
		player = new Person(6);
		target = new Person(4);
		guard1 = new Person(2);
		guard2 = new Person(2);
		guard3 = new Person(2);
		delivery = new Person(1);
		//create items
		basementKey = new Item("basementKey");
		carKey = new Item("carKey");
		crate = new Item("crate");
		mail = new Item("mail");
		door = new Door("basementDoor");
		gun = new Item("gun");
		knife = new Item("knife");
		//set locations
		player.setX(0);
		player.setY(4);
		target.setX(3);
		target.setY(0);
		guard1.setX(1);
		guard1.setY(0);
		guard2.setX(3);
		guard2.setY(3);
		guard3.setX(3);
		guard3.setY(0);
		delivery.setX(2);
		delivery.setY(3);
		basementKey.setLocation(2,5);
		carKey.setLocation(target.getX(),target.getY());
		crate.setLocation(2,3);
		mail.setLocation(2,5);
		door.setLocation(1,8);
		gun.setLocation(3,8);
		knife.setLocation(0,6);
		//add inventories
		player.takeItem(gun);
		target.takeItem(carKey);
		gun.setAmmo(10);
		//set state
		alert = false;
		alertFull = false;
		disguise = false;
		basementVisited = false;
		targetKilled = false;
		
	}

	public void start() {
		//read intro
		Commands.readFile("prologue.txt");
		//print location
		location.print(player.getXY());

	}

	public void load() {
		
		try {
			//loading
			Commands.print("Loading . . .");
			Commands.print();
			//set of properties
			String[] properties = new String[16];
			BufferedReader br = new BufferedReader(new FileReader("saved.txt"));
			//properties container
			int i = 0;
			String lineCheck;
			while (true) {
				lineCheck = br.readLine();
				if (lineCheck==null){
					break;
				}
				//add lines to array
			 	properties[i] = lineCheck;
			 	i++; 
			}
			//close file
			br.close();
			//set properties based on array items
			setProperties(properties);
			//look around
			look();
		} catch (FileNotFoundException e) {
			Commands.print("No previously saved game state found.");
		} catch (ArrayIndexOutOfBoundsException ex) {
			Commands.print("ArrayIndexOutOfBoundsException @ Game.load");
		} catch (IOException ex) {
			Commands.print("IOException @ Game.load");
		}
		
	}

	public void save() {

		Commands.print("Saving . . .");
		Commands.print();

		//write array to file
		try {
			PrintWriter pw = new PrintWriter(new FileWriter("saved.txt"));

			//load properties
			String[] properties = getProperties();
			//line = property to be written
			for (String property : properties) {
				pw.println(property);
			}
			pw.close();

		} catch (IOException e) {
			Commands.print("IOException @ Game.save");
		}

		Commands.print("Game succesfully saved. Previous save file overwritten.");
	}

	public void setProperties(String[] properties) {

		//set each property in the right order
		player.setProperties(properties[0]);
		guard1.setProperties(properties[1]);
		guard2.setProperties(properties[2]);
		guard3.setProperties(properties[3]);
		target.setProperties(properties[4]);
		delivery.setProperties(properties[5]);
		basementKey.setProperties(properties[6]);
		carKey.setProperties(properties[7]);
		crate.setProperties(properties[8]);
		mail.setProperties(properties[9]);
		door.setProperties(properties[10]);
		gun.setProperties(properties[11]);
		knife.setProperties(properties[12]);
		alert = Commands.stringToBoolean(properties[13]);
		alertFull = Commands.stringToBoolean(properties[14]);
		disguise = Commands.stringToBoolean(properties[15]);
		
		//add possible item to inventory
		if (basementKey.getXY() == 38) {
			player.takeItem(basementKey);
		} 
		if (carKey.getXY() == 38) {
			player.takeItem(basementKey);
		}
		if (crate.getXY() == 38) {
			player.takeItem(basementKey);
		}
		if (knife.getXY() == 38) {
			player.takeItem(basementKey);
		}
		if (mail.getXY() == 38) {
			player.takeItem(basementKey);
		}
		if (gun.getXY() == 38) {
			player.takeItem(basementKey);
		}
	}
	public String[] getProperties() {

		String[] properties = new String[16];
		properties[0] = player.getProperties();
		properties[1] = guard1.getProperties();
		properties[2] = guard2.getProperties();
		properties[3] = guard3.getProperties();
		properties[4] = target.getProperties();
		properties[5] = delivery.getProperties();
		properties[6] = basementKey.getProperties();
		properties[7] = carKey.getProperties();
		properties[8] = crate.getProperties();
		properties[9] = mail.getProperties();
		properties[10] = door.getProperties();
		properties[11] = gun.getProperties();
		properties[12] = knife.getProperties();
		properties[13] = Commands.booleanToString(alert);
		properties[14] = Commands.booleanToString(alertFull);
		properties[15] = Commands.booleanToString(disguise);
		return properties;	
	}

	public void move(String direction) {
		
		int xy = player.getXY();
		//switch directions
		switch (direction) {
			case "north":
				//check invalid locations
				if (xy==10 || xy==20  || xy==30  || xy==13  || xy==23  || xy==34) {
					prohibited(1);
				} else if (xy==00) {
					prohibited(2);
				} else if (xy==05 || xy==15 || xy==25 || xy==35 || xy==07 || xy==17 || xy==27) {
					prohibited(3); 
				} else if (xy==12) {
					//south garage -> garage
					player.setX(3);
					player.setY(7);
					//print location
					location.print(player.getXY());
				} else if (xy==37){
					//garage -> frontyard
					player.setX(1);
					player.setY(1);
					//print location
					location.print(player.getXY());
				} else {
					//set location
					player.setY(player.getY()-1);
					//print location
					location.print(player.getXY());
				}
				break;
			case "south":
				//check invalid loc
				if (xy==12 || xy==22  || xy==33) {
					prohibited(1);
				} else if (xy==04 || xy==14 || xy==24 || xy==34) {
					prohibited(2);
				} else if (xy== 16 || xy==26 || xy==36 || xy==18) {
					prohibited(3);
				} else if (xy==06) {
					//kitchen -> south mansion
					player.setX(2);
					player.setY(2);
					//print location
					location.print(player.getXY());
				} else if (xy==11) {
					//frontyard -> garage
					player.setX(3);
					player.setY(7);
					//print location
					location.print(player.getXY());
				} else if (xy==37){
					//garage -> garage south
					player.setX(1);
					player.setY(2);
					//print location
					location.print(player.getXY());
				} else if (xy==8 || xy==28) {
					//stair here
					prohibited(3);
					Commands.print("Did you mean to take the stairs instead?");
				}	else if (xy==17 && door.isLocked()){
					//basement door
					if (!player.hasItem(basementKey)) {
						Commands.print("The door is locked.");
					} else {
						unlockDoor();
						enterDoor();
						basementVisited = true;
					}
				} else {
					//set location
					player.setY(player.getY()+1);
					//print location
					location.print(player.getXY());
				}
				break;
			case "east":
				//check invalid loc
				if (xy==01 || xy==02 || xy==30 || xy==31 || xy==32 || xy==33) {
					prohibited(1);
				} else if (xy==34) {
					prohibited(2);
				} else if (xy==06 || xy==15 || xy==25 || xy==35 || xy==8 || xy==18 || xy==27 || xy==28 || xy==37) {
					prohibited(3);
				} else if (xy==36) {
					//hallway -> garden
					player.setX(3);
					player.setY(1);
					//print location
					location.print(player.getXY());
				} else if (xy==21) {
					//front door -> living room
					player.setX(0);
					player.setY(5);
					//print location
					location.print(player.getXY());
				} else {
					player.setX(player.getX()+1);
					//print location
					location.print(player.getXY());

				}
				break;
			case "west":
				if (xy==11 || xy==12) {
					prohibited(1);
				} else if (xy==00 || xy==01 || xy==02 || xy==03 || xy==04) {
					prohibited(2);
				} else if (xy==06 || xy==16 || xy==25 || xy==35 || xy==18 || xy==28 || xy==37) {
					prohibited(3);
				} else if(xy==05) {
					//living room -> front door
					player.setX(2);
					player.setY(1);
					//print location
					location.print(player.getXY());
				} else if (xy==31) {
					//garden -> hallway
					player.setX(3);
					player.setY(6);
					//print location
					location.print(player.getXY());
				} else {
					player.setX(player.getX()-1);
					//print location
					location.print(player.getXY());
				}
				break;
		}
		//move delivery guy		
		deliver();
		//surrounding items
		checkForPerson();
		checkForItems();
	}

	public void prohibited(int type) {
		switch (type) {
			case 1:
				Commands.print("The compound is gated. The wall prevents you from going further.");
				break;
			case 2:
				Commands.print("Where are you trying to go? Focus on your mission.");
				break;
			case 3:
				Commands.print("There's a wall that way.");
				break;
			case 4:
				Commands.print("He would never let you search him till his last breath.");
				break;
		}
	}

	public void look() {
		location.print(player.getXY());
		checkForPerson();
		checkForItems();
	}

	public void checkForPerson() {
		int xy = player.getXY();

		//target
		if (target.getXY() == xy) {
			//target state
			if (target.isDead()) {
				Commands.print("The target's body lies by the pool, as blood flows into the water coloring it deep red.");
			} else if (target.getLife()<4) {
				Commands.print("Wounded and furious, the target shoots at you.");
				player.getShot();
			} else if ((alert && !disguise) || (alertFull)) {
				Commands.print("Standing on the other side, the target pulls out his gun and starts shooting.");
				player.getShot();
			} else {
				Commands.print("The target is lying down on a chaise lounge, by the other side of the pool.");
			}
		}

		//guard
		if (guard1.getXY()==xy || guard2.getXY() ==xy || guard3.getXY()==xy) {
			if ((guard1.getXY()==xy && guard1.isDead()) || (guard2.getXY()==xy && guard2.isDead()) || (guard3.getXY()==xy && guard3.isDead())) {
				Commands.print("A dead guard lies here.");
			} else if (alert && !disguise) {
				Commands.print("The alert guard shoots at you.");
				player.getShot();
			} else if (!disguise) {
				Commands.print("There is a guard standing here.");
				Commands.print("Guard: \"This is a private property. You can not be around here.\"");
				alert();
			} else {
				Commands.print("There is a guard standing here.");
			}
		}

		//delivery guy
		if (delivery.getXY()==xy){
			if (delivery.isDead()) {
				Commands.print("Here lies the body of the dead delivery guy.");
			} else {
				if (alert) {
					Commands.print("The frightened delivery guy attempts to run away.");
				} else {
					Commands.print("The delivery guy walks towards you. He seems to be going back and forth between the kitchen and his truck, parked outside the south entrance, carrying groceries.");
				}
			}
		}

		if (player.getLife() == 0) {
			Commands.gameOver(0);
		}

	}

	public void alert() {
		if (alert) {
			alertFull = true;
			Commands.print("..........");
			Commands.print("Note: The guards are highly alerted. You will be most likely identified even if disguised. Proceed with caution.");
			Commands.print("..........");
		} else if (!alert) {
			alert = true;
			Commands.print("..........");
			Commands.print("Note: The guards are alerted. They will shoot at first sight if they find any intruders.");
			Commands.print("..........");
		} else {

		}	
	}

	public void takeClothes() {
		//if near delivery guy;
		if (player.getXY() == delivery.getXY()) {
			if (delivery.isDead()) {
				disguise=true;
				Commands.print ("You are now in disguise. Unless you alert the guards, they would not see through your disguise.");
			} else {
				alert();
				//run to alive guard
				if (!guard2.isDead()) {
					delivery.setX(guard2.getX());
					delivery.setY(guard2.getY());
				} else if (!guard1.isDead()) {
					delivery.setX(guard1.getX());
					delivery.setY(guard1.getY());
				} else {
					delivery.setX(guard3.getX());
					delivery.setY(guard3.getY());
				}
			//notify
			Commands.print("You scared the delivery guy. He ran away to notify the guards.");
			alert();

			}
		} else {
			Commands.print("There are no clothes for you to take. The delivery guy is not here.");
		}
	}

	public void deliver() {
		//move kitchen - south entrance
		if (delivery.getLife() != 0) {		
			double a = Math.random();
			//move delivery from and to kitchen
			if (a>0.2) {
				delivery.setX(0);
				delivery.setY(6);
			}else if (a>0.2 && a<0.4) {
				delivery.setX(2);
				delivery.setY(2);
			}else if (a>0.4 && a<0.6) {
				delivery.setX(3);
				delivery.setY(2);
			}else if (a>0.6 && a<0.8) {
				delivery.setX(3);
				delivery.setY(3);
			} else {
				delivery.setX(2);
				delivery.setY(3);
			}
		}
	}

	public void getInventoryOf(String person) {
		
		if (person.equals("player")) {
			player.printInventory("Your");
		} else if (person.equals("guard1")) {
				if (guard1.isDead()) {
				guard1.printInventory("Guard's");
				} else {
					prohibited(4);
				}
		} else if (person.equals("guard2")) {
				if (guard2.isDead()) {
				guard2.printInventory("Guard's");
				} else {
					prohibited(4);
		 		}
		} else if (person.equals("guard3")) {
				if (guard3.isDead()) {
				guard3.printInventory("Guard's");
				} else {
					prohibited(4);
				}
		} else if (person.equals("target")) {
				if (target.isDead()) {
				guard1.printInventory("Target's");
				} else {
					prohibited(4);
				}
		} else if (person.equals("delivery")) {
				if (delivery.isDead()) {
				guard1.printInventory("Delivery guy's");
				} else {
					prohibited(4);
				}
		} else {
			Commands.print("I don't know this person.");
		}
	}

	public void takeItem(String itemType) {
		//change string to true itemType
		if (itemType.equals("basement")) {
			itemType = "basementKey";
		} else if (itemType.equals("car")) {
			itemType = "carKey";
		}
		//get item
		Item item = convertStringToItem(itemType);
		//take item
		if (player.getXY()==item.getXY()) {
			if (!player.inventoryFull()) {
				player.takeItem(item);
				item.taken();
				Commands.print("Item taken.");
			} else {
				Commands.print("Inventory full.");
			}
		} else {
			Commands.print("You can not see that item here.");
		}
	}

	public void dropItem(String itemType) {
		Item item = convertStringToItem(itemType);
		if (player.hasItem(item)) {
			//drop
			player.dropItem(item);
			//set location to current location
			item.setLocation(player.getX(), player.getY());
			Commands.print(itemType+" dropped.");
		} else {
			Commands.print("You have no such item.");
		}
	}


	public Item convertStringToItem(String itemType) {
		
		if(basementKey.getType().equals(itemType)) {
			return basementKey;
		} else if (carKey.getType().equals(itemType)) {
			return carKey;
		} else if (crate.getType().equals(itemType)) {
			return crate;
		} else if (mail.getType().equals(itemType)) {
			return mail;
		} else if (gun.getType().equals(itemType)) {
			return gun;
		} else {	
			return knife;
		}
	}

	public void shoot(String person) {
		//decrease ammo
		gun.shoot();
		Commands.print("Ammo:" + gun.getAmmo());

		if (gun.getAmmo()>0) {
			if (person.equals("guard")) {
				if (player.getXY() == guard1.getXY()) {
					//shoot and check
					guard1.shootEnemy();
					//alerted
					alert();
					player.getShot();
					if (guard1.getLife()==0){
						Commands.print("You killed your target.");
						gun.setAmmo(gun.getAmmo()+5);
						Commands.print("(picked up ammo of dead enemy)");
					}

				} else if (player.getXY() == guard2.getXY()) {
					int life = guard2.getLife();
					//shoot and check
					guard2.shootEnemy();
					//alerted
					alert();
					player.getShot();
					if (guard2.getLife()==0){
						Commands.print("You killed your target.");
						gun.setAmmo(gun.getAmmo()+5);
						Commands.print("(picked up ammo of dead enemy)");
					}
				} else if (player.getXY() == guard3.getXY()) {
					int life = guard3.getLife();
					//shoot and check
					guard3.shootEnemy();
					//alerted
					alert();
					player.getShot();
					if (guard3.getLife()==0){
						Commands.print("You killed your target.");
						gun.setAmmo(gun.getAmmo()+5);
						Commands.print("(picked up ammo of dead enemy)");
					}
				} else {
					Commands.print("You can not shoot a guard from here. You need to get closer.");
				}
			} else if (person.equals("target")) {
				if (player.getXY() == target.getXY()) {
					target.shootEnemy();
					//alerted
					alert();
					player.getShot();
					if (target.getLife()==0){
						targetKilled = true;
						Commands.print("You killed your target.");
						gun.setAmmo(gun.getAmmo()+5);
						Commands.print("(picked up ammo of dead enemy)");
					}
				} else {
					Commands.print("You can not shoot the target from here. You need to get closer.");
				}
			} else if (person.equals("delivery")) {
				if (player.getXY() == delivery.getXY()) {
					delivery.shootEnemy();
					//alerted
					alert();
					if (delivery.getLife()==0){
						Commands.print("You killed your target.");
					}
				} else {
					Commands.print("You can not shoot the delivery guy from here. You need to get closer.");
				}

			} 
			else {
				Commands.print("Who do you want to shoot?");
			}
		} else {
			Commands.print("Oops... you ran out of bullets.");
		}

		if (player.getLife()==0){
			Commands.gameOver(0);
		}
		
	}

	public void enterDoor() {
		int xy = player.getXY();
		if (xy==12 || xy==26 || xy==36 || xy==18) {
			move("north");
		} else if (xy==31 || xy==15) {
			move("west");
		} else if (xy==25 || xy==35 || xy==37 || xy==8 || xy==28 || xy==17) {
			move("south");
		} else if (xy==21) {
			move("east");
		} else if (xy==22) {
			player.setX(0);
			player.setY(6);
			look();
		} else if (xy==05 || xy==06 || xy==36) {
			Commands.print("There is more than one door here. The door in which direction are you referring to?");
		} else {
			Commands.print("There are no doors around here.");
		}
	}

	public void enterDoor(String direction) {
		int xy = player.getXY();
		if (xy==05 && !direction.equals("north") || (xy==06 && (!direction.equals("east") || !direction.equals("west"))) || (xy==36 && (direction.equals("north") || direction.equals("east")))) {
			move(direction);
		} else {
			Commands.print("There is no door in that direction.");
		}
	}

	public void unlockDoor() {
		int xy = player.getXY();
		//check if has key
		if (xy==17 && player.hasItem(basementKey)) {
			//unlock door
			Commands.print("Using key to unlock . . . ");
			door.unlock();
			Commands.print("The door is unlocked.");
		} else {
			Commands.print("There is no locked door around here.");
		}
	}

	public void checkForItems() {
		int xy = player.getXY();
		if (xy==basementKey.getXY()) {
			Commands.print("There is a basement key lying around here. Was it the clumsy guards?");
		} else if (xy==carKey.getXY()) {
			Commands.print("There is a car key on the ground. Hmm...");
		} else if (xy==knife.getXY()) {
			Commands.print("Oooo... a shiny knife.");
		} else if (xy==crate.getXY()) {
			Commands.print("A crate filled with groceries is lying around on the ground.");
		} else if (xy==mail.getXY()) {
			Commands.print("Is that a mail?");
		} else if (xy==gun.getXY()) {
			Commands.print("There is a gun lying around on the ground.");
		}
	}

	public void examine(String item) {
		switch (item) {
			case "basement":
				Commands.print("It's a rather large key made of iron, that adds to it some weight. There is rust developing all around the handle.");
				break;
			case "car":
				Commands.print("It's an old boring car key. It has two sets of teeth, an upper level standard set of teeth and a lower, less defined set of teeth beside it.");
				break;
			case "crate":
				Commands.print("It looks like it was made in a rush. The planks, which make the body, are uneven and nails portrude out of the wood. There are groceries inside it. Who puts food in there?");
				break;
			case "knife":
				Commands.print("It's long and narrow with a blunt end. The handles are cracked and the blade rattles with every swing. It's barely functional.");
				break;
			case "mail":
				Commands.print("It appears to be writted in a different language. It might be some kind of coded message.");
				break;
			case "gun":
				Commands.print("It is a semi-automatic pistol, made entirely out of staineless steel, with polymer grips and high-capacity magazine. The grip is comfortable.");
				break;
		}
	}

	public void takeStairs() {
		//up/down stairs
		int xy = player.getXY();
		if (xy==32) {
			player.setX(2);
			player.setY(8);
			//print location
			location.print(player.getXY());
		} else if (xy==28) {
			player.setX(3);
			player.setY(2);
			//print location
			location.print(player.getXY());
		} else if (xy==06) {
			player.setX(0);
			player.setY(8);
			//print location
			location.print(player.getXY());
		} else if (xy==8) {
			player.setX(0);
			player.setY(6);
			//print location
			location.print(player.getXY());
		} else {
			Commands.print("There are no stairs here.");
		}
	}

	public void escape() {
		//if has car keys
		if (targetKilled && basementVisited) {
			if (player.getXY()==37) {
				if (player.hasItem(carKey)){
					Commands.print("(getting in car....)");
					Commands.print("VROOM!");
					Commands.gameOver(1);
				} else {
					Commands.print("The car is locked. You need to find its keys.");
				}
			} else {
				Commands.print("You need to find a gataway car.");
			}	
		} else if (targetKilled && !basementVisited) {
			Commands.print("Did you go through all this to leave without your friend?");
		} else if (!targetKilled && basementVisited) {
			Commands.print("You are thirsty for revenge. You can't leave without eliminating your target.");
		} else {
			Commands.print("You should probably focus on completing your mission first.");
		}
		
	}
}