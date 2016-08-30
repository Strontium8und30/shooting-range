package framework.object.interactive;

import framework.types.*;

public abstract class InteractiveObjectImpl implements InteractiveObject {

	private ContainerObject containerObject;
	
	
	public InteractiveObjectImpl() {}
	
	public InteractiveObjectImpl(ContainerObject containerObject) {
		this.containerObject = containerObject;
	}
	
	@Override
	public boolean canUse() {
		return true;
	}

	@Override
	public ContainerObject getContainerObject() {
		return containerObject;
	}
	
	@Override
	public void setContainerObject(ContainerObject containerObject) {
		this.containerObject = containerObject;
	}
}
