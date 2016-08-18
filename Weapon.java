public class Weapon extends Item {
	private int ammo;

	public Weapon(String itemType) {
		super(itemType);
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