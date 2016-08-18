public class Door extends Item {
	private boolean isLocked;

	public Door(String itemType) {
		super(itemType);
		isLocked = true;
	}

	public void unlock() {
		isLocked = false;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public String getProperties() {
		String properties = type+" "+x+" "+y+" "+Commands.booleanToString(isLocked);
		return properties;
	}

	public void setProperties(String property) {
		String[] properties = property.split(" ");
		type = properties[0];
		x = Integer.parseInt(properties[1]);
		y = Integer.parseInt(properties[2]);
		isLocked = Commands.stringToBoolean(properties[3]);
	}
}