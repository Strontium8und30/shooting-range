package framework.types;

import java.util.*;

public abstract class ContainerObject implements ObjectMetaData {
	
	/** contains a list of ContainerObject's */
	private List<ContainerObject> containerObjects = null;
	
	/** contains a list of GraphicalObject's */
	private List<GraphicalObject> graphicalObjekts = null;
	
	/** Die eindeutige Id des Objekts */
	private Long id = ObjectMetaDataDefaults.ID;
	
	/** Reference to the Parent */
	private ContainerObject parent = null;
	
	/** Massenschwerpunkt */
	private Vector3D centerOfGravity = null;
		
	/** Ausmaﬂe des Objektes (Radius) */
	private float size;
	
	/** Kann sich dieses Objekt bewegen */
	private boolean dynamic = ObjectMetaDataDefaults.DYNAMIC;
	
	/** Can you walk through the MapObject or not */
	private boolean hard = ObjectMetaDataDefaults.HARD;
	
	/** Is it shoot through the MapObject or not */
	private boolean shootable = ObjectMetaDataDefaults.SHOOTABLE;
	
	/** Is it possible to destroy the MapObject or not */
	private boolean destroyable = ObjectMetaDataDefaults.DESTROYABLE;
	
	/** Whitch mass has the MapObject */
	private int mass = ObjectMetaDataDefaults.MASS;
	
	/** Witch Power has the MapObject */
	private int power = ObjectMetaDataDefaults.POWER;
	
	
	public ContainerObject() {
		containerObjects = new ArrayList<ContainerObject>();
		graphicalObjekts = new ArrayList<GraphicalObject>();
	}
	
	public void clearContainerObjectLists() {
		containerObjects = new ArrayList<ContainerObject>();
		graphicalObjekts = new ArrayList<GraphicalObject>();
	}
	
	public void addGraphicalObject(GraphicalObject graphicalObject) {
		graphicalObjekts.add(graphicalObject);
		centerOfGravity = calcGeometricCenter();
		setSize(calcSize(centerOfGravity));
		graphicalObject.setOwner(this);
	}
	
	public void addAllGraphicalObject(List<GraphicalObject> graphicalObjects) {
		for (GraphicalObject graphicalObject : graphicalObjects) {
			addGraphicalObject(graphicalObject);
		}
	}
	
	public void removeChildGraphicalObject(int index) {
		removeChildGraphicalObject(graphicalObjekts.get(index));
	}
	
	public void removeChildGraphicalObject(GraphicalObject simpleObject) {
		graphicalObjekts.remove(simpleObject);
		centerOfGravity = calcGeometricCenter();
		setSize(calcSize(centerOfGravity));
	}
	
	public List<GraphicalObject> getGraphicalObjectList() {
		return graphicalObjekts;
	}
	
	public GraphicalObject getGraphicalObject(int index) {
		return graphicalObjekts.get(index);
	}
	
	public int getGraphicalIndex(GraphicalObject object) {
		return graphicalObjekts.indexOf(object);
	}
	
	public int getGraphicalObjectCount() {
		return graphicalObjekts.size();
	}
	
	public List<GraphicalObject> getAllSubGraphicalObjects() {
		List<GraphicalObject> graphicalObjects = new ArrayList<GraphicalObject>();
		for(ContainerObject cObj : getContainerObjectList()) {
			graphicalObjects.addAll(cObj.getAllSubGraphicalObjects());
		}
		if(getGraphicalObjectList() != null) {
			graphicalObjects.addAll(getGraphicalObjectList());
		}
		return graphicalObjects;
	}
	
	public void addContainerObject(ContainerObject containerObject) {
		containerObjects.add(containerObject);
		centerOfGravity = calcGeometricCenter();
		setSize(calcSize(centerOfGravity));
		containerObject.setParent(this);
	}
	
	public void removeChildContainerObject(int index) {
		removeContainerObject(containerObjects.get(index));
	}
	
	public void removeContainerObject(ContainerObject containerObject) {
		containerObjects.remove(containerObject);
		centerOfGravity = calcGeometricCenter();
		setSize(calcSize(centerOfGravity));
	}
	
	public void remove() {
		ContainerObject parent = getParent();
		if(parent != null) {
			parent.removeContainerObject(this);
		} 
	}
	
	public List<ContainerObject> getContainerObjectList() {
		return containerObjects;
	}
	
	public ContainerObject getContainerObject(int index) {
		return containerObjects.get(index);
	}
	
	public int getContainerIndex(ContainerObject containerObject) {
		return containerObjects.indexOf(containerObject);
	}	

	public int getContainerObjectCount() {
		return containerObjects.size();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setParent(ContainerObject parent) {
		this.parent = parent;
	}
	
	public ContainerObject getParent() {
		return parent;
	}
	
	public Vector3D calcGeometricCenter() {
		Vector3D max = getMax();
		Vector3D min = getMin();
		if (max == null || min == null) return null;
		Vector3D center = new Vector3D((max.x + min.x) / 2,
				                       (max.y + min.y) / 2,
				                       (max.z + min.z) / 2);
		return center;
	}
	
	public Vector3D getGeometricCenter() {
		return centerOfGravity;
	}
	
	public Vector3D getMax() {
		Vector3D max = null;
		for(int index = 0; index < getContainerObjectCount(); index++) {
			ContainerObject cObj = getContainerObject(index);
			if(max == null) max = cObj.getMax();
			Vector3D tmpMax = cObj.getMax();
			if(max == null || tmpMax == null) continue;
			if(tmpMax.x > max.x) max.x = tmpMax.x;
			if(tmpMax.y > max.y) max.y = tmpMax.y;
			if(tmpMax.z > max.z) max.z = tmpMax.z;
		}
		
		for(int index = 0; index < getGraphicalObjectCount(); index++) {
			GraphicalObject sObj = getGraphicalObject(index);
			if(max == null) max = new Vector3D(sObj.getVertex(0));
			if(sObj.getMax().x > max.x) max.x = sObj.getMax().x;
			if(sObj.getMax().y > max.y) max.y = sObj.getMax().y;
			if(sObj.getMax().z > max.z) max.z = sObj.getMax().z;
		}
		return max;
	}
	
	public Vector3D getMin() {
		Vector3D min = null;
		for(ContainerObject cObj : getContainerObjectList()) {
			if(min == null) min = cObj.getMin();
			Vector3D tmpMin = cObj.getMin();
			if(min == null || tmpMin == null) continue;
			if(tmpMin.x < min.x) min.x = tmpMin.x;
			if(tmpMin.y < min.y) min.y = tmpMin.y;
			if(tmpMin.z < min.z) min.z = tmpMin.z;
		}
		for(GraphicalObject sObj : getGraphicalObjectList()) {
			if(min == null) min = new Vector3D(sObj.getVertex(0));
			if(sObj.getMin().x < min.x) min.x = sObj.getMin().x;
			if(sObj.getMin().y < min.y) min.y = sObj.getMin().y;
			if(sObj.getMin().z < min.z) min.z = sObj.getMin().z;
		}
		return min;
	}
	
	public float calcSize(Vector3D center) {
		float size = 0.0f;
		for (ContainerObject cObj : getContainerObjectList()) {
			float childSize = cObj.calcSize(center);
			if(childSize > size) {
				size = childSize;
			}
		}
		for (GraphicalObject sObj : getGraphicalObjectList()) {
			for (int i = 0; i < 3; i++) {
				float distance = Vector3D.dinstance(center, sObj.getVertex(i));
				if (distance > size) {
					size = distance;
				}
			}	
		}
		return size;
	}
	
	public ContainerObject flatten() {
		for(ContainerObject cObj : getContainerObjectList()) {
			cObj.flatten();
			removeContainerObject(cObj);
		}
		for(GraphicalObject sObj : getGraphicalObjectList()) {
			if (parent != null) {
				parent.addGraphicalObject(sObj);
			}
		}
		return this;
	}
	
	public ContainerObject transfareToZero() {
		return transfareTo(new Vector3D(0,0,0));
	}
	
	public ContainerObject transfareTo(Vector3D vector) {
		return transfare(new Vector3D(vector).subtract(centerOfGravity));
	}
	
	public void setTextureID(int textureID) {
		for(ContainerObject cObj : getContainerObjectList()) {
			cObj.setTextureID(textureID);
		}
		for(GraphicalObject sObj : getGraphicalObjectList()) {
			sObj.setTextureID(textureID);
		}
	}
	
	public ContainerObject transfare(Vector3D vector) {
		for(int index = 0; index < getContainerObjectCount(); index++) {
			getContainerObject(index).transfare(vector);
		}
		
		for(int index = 0; index < getGraphicalObjectCount(); index++) {
			getGraphicalObject(index).transfare(vector);
		}
		centerOfGravity = calcGeometricCenter();
		return this;
	}
		
	public ContainerObject rotate(Vector3D angle) {
		rotateRe(angle.getX(), angle.getY(), angle.getZ());
		return this;
	}
	
	private void rotateRe(float angX, float angY, float angZ) {	
		for(ContainerObject cObj : getContainerObjectList()) {
			cObj.rotateRe(angX, angY, angZ);
		}
		for(GraphicalObject sObj : getGraphicalObjectList()) {
			sObj.rotate(angX, angY, angZ);
		}
	}	
	
	public ContainerObject resize(float factor) {
		transfare(Vector3D.multiplyBy(centerOfGravity, -1));
		resizeRe(factor);
		transfare(centerOfGravity);
		return this;
	}
	
	private void resizeRe(float factor) {
		for(ContainerObject cObj : getContainerObjectList()) {
			cObj.resizeRe(factor);
		}
		for(GraphicalObject sObj : getGraphicalObjectList()) {
			sObj.resize(factor);
		}
	}
	
	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}
	
	@Override
	public boolean isVisible() {
		for(ContainerObject cObj : getContainerObjectList()) {
			if (!cObj.isVisible()) return false;
		}
		for(GraphicalObject sObj : getGraphicalObjectList()) {
			if (!sObj.isVisible()) return false;
		}
		return true;
	}
	
	@Override
	public void setVisible(boolean visible) {
		for(ContainerObject cObj : getContainerObjectList()) {
			cObj.setVisible(visible);
		}
		for(GraphicalObject sObj : getGraphicalObjectList()) {
			sObj.setVisible(visible);
		}
	}
	
	@Override
	public boolean isDynamic() {
		return dynamic;
	}

	@Override
	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

	@Override
	public boolean isHard() {
		return hard;
	}
	
	@Override
	public void setHard(boolean hard) {
		this.hard = hard;
	}

	@Override
	public boolean isShootable() {
		return shootable;
	}
	
	@Override
	public void setShootable(boolean shootable) {
		this.shootable = shootable;
	}

	@Override
	public boolean isDestroyable() {
		return destroyable;
	}
	
	@Override
	public void setDestroyable(boolean destroyable) {
		this.destroyable = destroyable;
	}

	@Override
	public int getMass() {
		return mass;
	}

	@Override
	public void setMass(int mass) {
		this.mass = mass;
	}
	
	@Override
	public int getPower() {
		return power;
	}
	
	@Override
	public void setPower(int power) {
		this.power = power;
	}
	
	@Override
	public void setMetaData(ObjectMetaData metaData) {
		id = metaData.getId();
		dynamic = metaData.isDynamic();
		hard = metaData.isHard();
		shootable = metaData.isShootable();		
		destroyable = metaData.isDestroyable();
		mass = metaData.getMass();
		power = metaData.getPower(); 
	}
	
	public ContainerObject getContainerObjectById(Long id) {
		if (getId().equals(id)) return this;
		for (ContainerObject containerObject : getContainerObjectList()) {
			ContainerObject cObject = containerObject.getContainerObjectById(id);
			if (cObject != null) return cObject;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "Object: " + id;
	}
}
