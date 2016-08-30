package framework;

import framework.types.*;
import framework.weapons.*;

public interface Player {
	
	public Vector3D getPosition();
	
	public Camera getCamera();

	public Backpack getBackpack();
	
	public Weapon getWeapon();
	
	public void shoot();
}
