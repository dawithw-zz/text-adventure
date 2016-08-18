import java.util.*;
import java.io.*;
import java.lang.ArrayIndexOutOfBoundsException;

public class Location {

	private static Map<Integer, String> map;
	private static String[][] location;
	

	public Location() {
		map = new HashMap<Integer, String>();
		location = new String [4][9];
		mapLocation();
		//set location 3,8 as inventory
		map.put(38,"inventory");
 	}

	public void mapLocation() {

		try {
			BufferedReader br = new BufferedReader(new FileReader("location.txt"));
			//iterate over columns, top to bottom
			for (int i=0; i<4; i++) {
				for (int j=0; j<9; j++) {
					String description = br.readLine();
					if (description!=null){						
						location[i][j] = description;
						map.put((i*10)+j,description);
					}
				}
			}
			//close file
			br.close();
		} catch (ArrayIndexOutOfBoundsException ex) {
			Commands.print("ArrayIndexOutOfBound @ Location.addOutside");
		} catch (IOException ex) {
			Commands.print("IOException @ Location.addOutside");
		}

	}

	public String getLocation(int xy) {
		return map.get(xy);
	}

	public boolean exists(int xy) {
		return map.containsKey(xy);
	}

	public void print(int xy) {
		Commands.print(getLocation(xy));
	}
}
