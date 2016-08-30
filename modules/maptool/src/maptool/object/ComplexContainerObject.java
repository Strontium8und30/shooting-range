package maptool.object;

import java.util.*;

import utilities.log.*;
import framework.types.*;

/**
 * Ein ContainerObject besteht aus den am häufigsten verwendeten Meta Daten, 
 * wie z.B. Physikalische (Masse) und Objektbezogene(Sichtbarkeit) Eingenschaften, 
 * sowie aus ein oder mehreren SimpleObject.
 * 
 * @author Thorben
 *
 */
public class ComplexContainerObject extends ContainerObject implements ComplexObjectMetaData {

	/** Logging */
	public static Log log = LogFactory.getLog(ComplexContainerObject.class);
	
	/** Contains the name of the Object */
	private String name = ObjectMetaDataDefaults.NAME;

	/** Bestimmt ob das Objekt automatisch am Raster ausgerichtet wird */
	private boolean autoAlignment = ObjectMetaDataDefaults.AUTOALIGNMENT;
	

	public ComplexContainerObject() {
		this("NoName");
	}
	
	public ComplexContainerObject(String objName) {
		setName(objName);
	}
	
	public ComplexContainerObject(ComplexContainerObject containerObject) {			
		for(ContainerObject cObj : containerObject.getContainerObjectList()) {
			addContainerObject(new ComplexContainerObject((ComplexContainerObject)cObj));
		}
		for(GraphicalObject sObj : containerObject.getGraphicalObjectList()) {
			addGraphicalObject(new ComplexGraphicalObject((ComplexGraphicalObject)sObj));
		}
		setMetaData(containerObject);	
		name = containerObject.getName();
		autoAlignment = containerObject.isAutoAlignment();
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String objName) {
		this.name = objName;
	}
	
	public boolean isAutoAlignment() {
		return autoAlignment;
	}

	public void setAutoAlignment(boolean autoAlignment) {
		this.autoAlignment = autoAlignment;
	}
	
	public Set<Integer> getTextureIDs() {
		Set<Integer> textureIDs = new HashSet<Integer>();
		for(ContainerObject cObj : getContainerObjectList()) {
			textureIDs.addAll(((ComplexContainerObject)cObj).getTextureIDs());
		}
		for(GraphicalObject sObj : getGraphicalObjectList()) {
			textureIDs.add(sObj.getTextureID());
		}
		return textureIDs;
	}
	
	@Override
	public boolean isSelected() {
		for(ContainerObject cObj : getContainerObjectList()) {
			if (!((ComplexContainerObject)cObj).isSelected()) return false;
		}
		for(GraphicalObject sObj : getGraphicalObjectList()) {
			if (!((ComplexGraphicalObject)sObj).isSelected()) return false;
		}
		return true;
	}
	
	@Override
	public void setSelected(boolean selected) {
		for(ContainerObject cObj : getContainerObjectList()) {
			((ComplexContainerObject)cObj).setSelected(selected);
		}
		for(GraphicalObject sObj : getGraphicalObjectList()) {
			((ComplexGraphicalObject)sObj).setSelected(selected);
		}
	}
	
	public ComplexContainerObject getResizedObject(float factor) {
		ComplexContainerObject tmpContainerObject = new ComplexContainerObject(this);
		for(ContainerObject cObj : tmpContainerObject.getContainerObjectList()) {
			cObj.resize(factor);
		}
		for(GraphicalObject sObj : tmpContainerObject.getGraphicalObjectList()) {
			sObj.resize(factor);
		}
		return tmpContainerObject;
	}
	
	
	
	public void removeFromParent() {
		ContainerObject parent = getParent();
		if(parent != null) {
			parent.removeContainerObject(this);
			setParent(null);
		} 
	}
	
	public void setMetaData(ComplexContainerObject containerObject) {
		super.setMetaData(containerObject);
		name = containerObject.getName();
		autoAlignment = containerObject.isAutoAlignment();
	}
	
	@Override
	public String toString() {
		return name;
	}
}
