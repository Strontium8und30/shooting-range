package client.player;

import java.util.*;

import framework.*;
import framework.weapons.*;

public class BackpackImpl implements Backpack {

	/** Welche Waffen */
	private List<Weapon> weapons = new ArrayList<Weapon>();
	
	/** Wie viel Munition von welchem Typ */
	private Map<Ammo, Integer> ammo = new HashMap<Ammo, Integer>();
	
	public BackpackImpl() {
		weapons.add(Weapon.PISTOL);
		weapons.add(Weapon.NO_WEAPON);
		weapons.add(Weapon.MP5);
		
		addAmmo(Ammo.PISTOL, 100);
	}
	
	
	public void addWeapon(Weapon weapon) {
		weapons.add(weapon);
	}
	
	public void removeWeapon(Weapon weapon) {
		weapons.remove(weapon);
	}
	
	public List<Weapon> getWeapons() {
		return weapons;
	}
	

	public void addAmmo(Ammo ammoType, int bullets) {
		Integer ammoCount = ammo.get(ammoType);
		if (ammoCount == null) {
			ammo.put(ammoType, bullets);
		} else {
			ammo.put(ammoType, ammoCount + bullets);	
		}
	}
	
	public void removeAmmo(Ammo ammoType, int bullets) {
		Integer ammoCount = ammo.get(ammoType);
		if (ammoCount != null) {
			ammo.put(ammoType, ammoCount - bullets);
		}
		
	}
	
	public int getAmmoCount(Ammo ammoType) {
		Integer ammoCount = ammo.get(ammoType);
		if (ammoCount != null) {
			return ammoCount;
		} else {
			return 0;
		}
	}
}
