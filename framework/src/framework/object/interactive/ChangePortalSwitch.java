package framework.object.interactive;

import java.util.*;

import utilities.*;

import framework.types.*;

public class ChangePortalSwitch extends Switch {
	
	@Override
	@SuppressWarnings("unchecked")
	public void execute(Object value) {
		if (value instanceof List<?>) {
			List<PortalInf> portals = (List<PortalInf>)value;
			ContainerObject portalContainer = portals.get(0).getPortalB();
			portals.get(0).setPortalB(portals.get(1).getPortalB());
			portals.get(1).setPortalB(portalContainer);
			getContainerObject().setTextureID(getState() ? 1 : 0);
			Utilities.playSound("fx/boing.wav");
		}
	}
}
