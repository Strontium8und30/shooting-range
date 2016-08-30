package framework.types;

import framework.types.Portal.*;

public interface PortalInf {
	
	public ContainerObject getPortalA();

	public void setPortalA(ContainerObject portalA);

	public ContainerObject getPortalB();

	public void setPortalB(ContainerObject portalB);
	
	public PortalType getType();
	
	public void setType(PortalType portalType);
	
	public boolean isATOB();
	
	public boolean isBTOA();
	
	public boolean isUsed();
	
	public void setUsed(boolean used);
	
	public boolean isOpen();
}
