package framework.object.interactive;

import framework.*;
import framework.weapons.*;

public class EquipmentBox extends InteractiveObjectImpl {
	
	@Override
	public void use(Object value) {
		((Player) value).getBackpack().addAmmo(Ammo.PISTOL, 100);
	}

}
