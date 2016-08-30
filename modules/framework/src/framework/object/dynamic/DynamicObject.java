package framework.object.dynamic;

import framework.types.*;

public interface DynamicObject {
	
	public void addPower(Vector3D power);
	
	public void calculate(ContainerObject chapterContainerObject);
	
	public void reset();
	
	public boolean isActive();
}
