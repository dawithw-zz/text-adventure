import java.util.*;
import java.io.*;

public class Commands{

	private static boolean mainMenu = true;
	private static boolean quit = false;
	private static Action action = new Action();;

	public static void parseText(String input){

		// change input to lowecase
		input = input.toLowerCase();

		//quit
		if (input.equals("q") || input.equals("quit")){
			quit = true;
			quit();
		}
		//back to main menu
		else if (input.equals("m") || input.equals("main")) {
			//go to main menu
			if (!mainMenu){
				Hitman.mainMenu();
				mainMenu = true;
			}
			//already in main menu 
			else {
				Hitman.mainMenu();
				print("You're already in the main menu.");
			}
			
		}
		//not in main menu
		else if (!mainMenu){
			//save command
			if (input.equals("save")) {
				action.save();
			} 
			//restart command
			else if (input.equals("restart") || input.equals("reset")) {
				print("Game reset.");
				menuCommand("start");
			}
			//game commands
			else {
				//split sentence into words
				String[] words = input.split(" ");
				//process game commands
				gameCommand(words);	
			}
			
		}
		//main menu
		else {
			menuCommand(input);
		}

	}

	public static boolean quit() {
		return quit;
	}

	public static void menuCommand(String command) {

		//parse command
		if (command.equals("a") || command.equals("start")) {
			//create a new game
			action.start();
			mainMenu = false;
		} else if (command.equals("b") || command.equals("load")) {
			//load game
			action.load();
			mainMenu = false;
		} else if (command.equals("c") || command.equals("help")) {
			//open help
			Hitman.help();
		} else {
			//not valid option
			commandError(1);
		}

	}

	public static void gameCommand(String[] words) {
		
		//change abbreviation into word
		for (int i=0; i<words.length; i++) {
			if (words[i].equals("n")){
				words[i] = "north";
			} else if (words[i].equals("s")){
				words[i] = "south";
			} else if (words[i].equals("e")){
				words[i] = "east";
			} else if (words[i].equals("w")) {
				words[i] = "west";
			}
		}

		//single word commands

		if (words.length == 1) {

			if (words[0].equals("look")) {
				//get player location
				action.look();
			}

			else if (words[0].equals("inventory")) {
				//list player inventory
				action.getInventoryOf("player");
			} 
			
			else if (words[0].equals("escape")) {
				//escape
				action.escape();
			}

			else {
				//unrecognized command
				commandError(2);
			}

		}

		//interactive commands
		else if (words.length == 2 || (words.length==3 && (words[2].equals("guy")) || isDirection(words[2])) || words[2].equals("key")) {
			
			//go command
			if (words[0].equals("go")) {
				//direction indicated
				if (isDirection(words[1])) {
					// move player
					action.move(words[1]);
				} else {
					//unidentified direction
					commandError(3);
				}
			} 

			//take command
			else if (words[0].equals("take")) {
				
				//check if next word is an item
				if (isItem(words[1])) {
					//take item
					action.takeItem(words[1]);
				}
				//check if next word is clothes
				else if (words[1].equals("cloth") || words[1].equals("clothes")) {
					//take clothes
					action.takeClothes();
				} else if (words[1].equals("stairs")) {
					action.takeStairs();
				}
				else {
					//item error
					commandError(4);
				}
			}

			//enter command
			else if (words[0].equals("enter")) {
				//check if next item is openable
				if (words.length==3){	
					if ((words[1]).equals("door")) {
						//go through door in direction
						action.enterDoor(words[2]);
					} else {
						commandError(5);
					}
				} else {
					if ((words[1]).equals("door")) {
						//go through door
						action.enterDoor();
					} else {
						commandError(5);
					}
				}
			}

			//examine command
			else if (words[0].equals("examine")) {
				//check if item
				if (isItem(words[1])) {
					//get item description
					action.examine(words[1]);
				} else {
					commandError(4);
				}
			}

			//drop command
			else if (words[0].equals("drop")) {
				//check if item
				if (isItem(words[1])) {
					//drop item
					action.dropItem(words[1]);
				} else {
					commandError(4);
				}
			}

			//shoot command
			else if (words[0].equals("shoot")) {
				//check delivery
				if (isPerson(words[1])) {
					action.shoot(words[1]);
				} else {
					commandError(6);
				}
			}

			//unlock command
			else if (words[0].equals("unlock") && words[1].equals("door")) {
				action.unlockDoor();
			}

			else {
				//not a command
				commandError(2);
			}

		} 
		
		//misunderstanding 
		else {
			commandError(2);
		}

	}


	public static void commandError(int e) {

		switch(e) {
			case 1:
				print("That option does not exist here.");
				break;
			case 2:
				print("I don't understand that command.");
				break;
			case 3:
				print("I don't understand that direction.");
				break;
			case 4:
				print("There is no such item around here.");
				break;
			case 5:
				print("That is not something you can enter.");
				break;
			case 6:
				print("Who is this person you speak of?");
				break;
		}
	}

	public static boolean isDirection(String direction) {
		
		if (direction.equals("north") || direction.equals("south") || direction.equals("east") || direction.equals("west")) {
			return true;
		}
		return false;
	}

	public static boolean isItem(String item) {
		if (item.equals("basement") || item.equals("car") || item.equals("gun") || item.equals("knife") || item.equals("mail") || item.equals("crate")) {
			return true;
		}
		return false;
	}

	public static void print(String text) {
		System.out.println(text);
	}

	public static void print() {
		System.out.println();
	}

	public static void readFile(String file) {

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while(true){
			String line = br.readLine();
				if (line==null){
					break;
				}
				Commands.print(line);
			}

			br.close();

		} catch (IOException ex) {
			Commands.print("IOException @ Hitman.readFile" + " -- reading:"+file);
		}
	}

	public static void gameOver(int win) {
		
		switch (win){
			case 0:
				mainMenu = true;
				print();
				print("XXXXXXXXXXXXXXXXXXX");
				print("XX  You're dead! XX");
				print("XX   GAME OVER.  XX");
				print("XXXXXXXXXXXXXXXXXXX");
				print();
				print("Do you want to start again?");
				print("start: New Game  ||  main: Main menu");
				break;
			case 1:
				mainMenu = true;
				print();
				print("XXXXXXXXXXXXXXXXXXXXXXXXXXXX");
				print("XX  Mission Accomplished! XX");
				print("XX       Game Over.       XX");
				print("XXXXXXXXXXXXXXXXXXXXXXXXXXXX");
				print();
				print("Do you want to start again?");
				print("start: New Game  ||  main: Main menu");
				break;

		}
	}

	public static boolean isPerson(String person) {
		if (person.equals("guard") || person.equals("delivery") || person.equals("target")) {
			return true;
		}
		return false;
	}

	public static String booleanToString(boolean boo) {
		if (boo) {
			return "true";
		}
		return "false";
	}

	public static boolean stringToBoolean(String string) {
		if (string.equals("true")) {
			return true;
		}
		return false;
	}
}

