package framework.types;



public class SimpleContainerObject extends ContainerObject {

	/** Is the MapObject visible or not */
	private boolean visible = true;
	
	public SimpleContainerObject() {}
	
	public SimpleContainerObject(ContainerObject containerObject) {		
		for(ContainerObject cObj : containerObject.getContainerObjectList()) {
			addContainerObject(new SimpleContainerObject((ContainerObject)cObj));
		}
		for(GraphicalObject sObj : containerObject.getGraphicalObjectList()) {
			addGraphicalObject(new SimpleGraphicalObject((GraphicalObject)sObj));
		}
		setMetaData(containerObject);	
	}
	
	
	@Override
	public boolean isVisible() {
		return visible;
	}
	
	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isPointInRange(Vector3D point) {
		return (getGeometricCenter().dinstance(point) < getSize());
	}
	
	public boolean isPointInRange(Vector3D point, float tolerance) {
		return (getGeometricCenter().dinstance(point) < getSize() + tolerance);
	}
	
	@Override
	public String toString() {
		return "Einfaches Kontainer Objekt: " + getId();
	}
}
