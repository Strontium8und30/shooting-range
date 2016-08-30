package framework.object.interactive;

import framework.types.*;

public interface InteractiveObject {
	
	public ContainerObject getContainerObject();
	
	public void setContainerObject(ContainerObject containerObject);
	
	public boolean canUse();
	
	public void use(Object value);

}
