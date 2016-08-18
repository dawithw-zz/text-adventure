public class Item {
	protected int x;
	protected int y;
	protected String type;

	//for gun;
	private int ammo;

	public Item(String itemType) {
		type = itemType;
	}

	public void setLocation(int valueX, int valueY) {
		x = valueX;
		y = valueY;
	}

	public int getXY() {
		return (x*10)+y;
	}

	public void taken() {
			x=3;
			y=8;
	}

	public void dropped(int xLocation, int yLocation) {
		x = xLocation;
		y = yLocation;
	}

	public String getType() {
		return type;
	}

	public int getAmmo() {
		return ammo;
	}

	public void setAmmo(int value) {
		ammo = value;
	}

	public void shoot() {
		ammo--;
	}

	public String getProperties() {
		String properties = type+" "+x+" "+y+" "+ammo;
		return properties;
	}

	public void setProperties(String property) {
		String[] properties = property.split(" ");
		type = properties[0];
		x = Integer.parseInt(properties[1]);
		y = Integer.parseInt(properties[2]);
		ammo = Integer.parseInt(properties[3]);
	}
}