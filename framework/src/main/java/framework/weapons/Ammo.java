package framework.weapons;

public enum Ammo {
	NO_AMMO(-1, "Keine Munition"),
	DEFAULT(0, "Standard Munition"),
	PISTOL(1, "Pistolen Munition"),
	GEWEHR(2, "Gewehrmunition");
	
	private Ammo(int id, String description) {
		
	}	
}
