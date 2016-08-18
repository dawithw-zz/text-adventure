import java.util.*;
import java.io.*;

public class Hitman {

	private static boolean mainScreen;
	private static boolean quit = false;

	public static void main (String[] args) {

		//note
		Commands.print("*Inspired by the 2002 video Game Hitman 2: Silent Assasin. This game contains adapted features.");		
		
		//load main menu
		mainMenu();
		//load input field
		inputField();


	}

	public static void inputField() {

		while (!(Commands.quit())) {
			Commands.print("");
			Commands.print("___________________");
			Scanner sc = new Scanner(System.in);
			String input = sc.nextLine();
			Commands.print("___________________");
			Commands.print("");
			Commands.parseText(input);
							
		}
	}

	public static void mainMenu() {
		//print file
		Commands.readFile("main_menu.txt");
	}

	public static void help() {
		Commands.readFile("help.txt");
	}

	
}