package framework.object.interactive;

import utilities.*;
import utilities.log.*;
import framework.types.*;

public abstract class Switch extends InteractiveObjectImpl {

	/** Logging */
	public static Log log = LogFactory.getLog(Switch.class);
	
	private String switchOnSound;
	
	private String switchOffSound;
	
	private boolean state = false;
	
	public Switch() {};
	
	public Switch(ContainerObject containerObject) {
		super(containerObject);
	}
	
	public void setSwitchOnSound(String switchOnSound) {
		this.switchOnSound = switchOnSound;
	}

	public void setSwitchOffSound(String switchOffSound) {
		this.switchOffSound = switchOffSound;
	}

	public boolean getState() {
		return state;
	}
	
	public abstract void execute(Object object);

	@Override
	public void use(Object object) {
		playSound();
		execute(object);
		state = !state;
	}
	
	private void playSound() {
		if (state = false) {
			if (switchOnSound == null) return;
			Utilities.playSound(switchOnSound);
			log.debug("Play Sound ON:" + switchOnSound);
		} else {
			if (switchOffSound == null) return;
			Utilities.playSound(switchOffSound);
			log.debug("Play Sound OFF:" + switchOffSound);
		}
	}
	
	@Override
	public String toString() {
		return "Switch objId=" + getContainerObject().getId() + " state=" + state;
	}
}
