package maptool.object;

import maptool.*;
import framework.types.*;

public class ModelBuildObject extends ComplexContainerObject {
	
	/** Die Kontainerbasis wird nicht verändert */
	private ComplexContainerObject containerObjectBase;
	
	/** Eigenrotation des zu setzenden Objekts */
	private Vector3D rotation = new Vector3D();
	
	private float resizeFactor = 1.0f;
	
	
	public ModelBuildObject(ComplexContainerObject containerObject) {
		super(containerObject);
		this.containerObjectBase = new ComplexContainerObject(containerObject);
	}
	
	public ContainerObject rotate(float angX, float angY, float angZ) {
		rotation.x += angX;
		rotation.y += angY;
		rotation.z += angZ;
		setContainerObject(containerObjectBase.getResizedObject(resizeFactor).rotate(rotation));
		return this;
	}
	
	public ModelBuildObject resizeModel(float factor) {
		resizeFactor += Const.GRID_PRECISION * factor;
		setContainerObject(containerObjectBase.getResizedObject(resizeFactor).rotate(rotation));
		return this;
	}
	
	private void setContainerObject(ContainerObject containerObject) {
		clearContainerObjectLists();
		setContainerObjectRe(containerObject);
	}
	
	private void setContainerObjectRe(ContainerObject containerObject) {
		for(ContainerObject cObj : containerObject.getContainerObjectList()) {
			addContainerObject(new ComplexContainerObject((ComplexContainerObject)cObj));
		}
		for(GraphicalObject sObj : containerObject.getGraphicalObjectList()) {
			addGraphicalObject(new ComplexGraphicalObject((ComplexGraphicalObject)sObj));
		}
		setMetaData(containerObject);		
	}
	
	
}
