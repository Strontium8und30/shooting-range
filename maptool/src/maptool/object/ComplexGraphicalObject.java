package maptool.object;

import framework.types.*;

public class ComplexGraphicalObject extends GraphicalObject implements ComplexObjectMetaData {

	/** Contains the name of the Object */
	private String name = ObjectMetaDataDefaults.GRAPHICAL_NAME;
	
	/** Is the Object selected? */
	private boolean selected = false;
		
	
	public ComplexGraphicalObject() {}
		
	public ComplexGraphicalObject(ComplexGraphicalObject graphicalObject) {
		super(graphicalObject);
		this.name = graphicalObject.getName();
		this.selected = graphicalObject.isSelected();
	}
	
	public boolean similar(Triangle triangle) {	
		if (triangle == null) return false;
		calcComplete();
		triangle.calcComplete();
			return (Math.abs(Vector3D.produkt(triangle.getVertex(0), getNormalVector()) - Vector3D.produkt(getVertex(0), getNormalVector())) < 0.01 &&
				    Math.abs(Vector3D.produkt(triangle.getVertex(1), getNormalVector()) - Vector3D.produkt(getVertex(0), getNormalVector())) < 0.01 &&
				    Math.abs(Vector3D.produkt(triangle.getVertex(2), getNormalVector()) - Vector3D.produkt(getVertex(0), getNormalVector())) < 0.01 &&
				    isPointInTriangle(triangle.getVertex(0)) &&
				    isPointInTriangle(triangle.getVertex(1)) &&
				    isPointInTriangle(triangle.getVertex(2)));
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String objName) {
		if(objName == null) {
			name = "NoName";
			return;
		} 
		name = objName;
	}
	
	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void setMetaData(ObjectMetaData metaData) {
		super.setMetaData(metaData);
		this.name = ((ComplexGraphicalObject)metaData).getName();
		this.selected = ((ComplexGraphicalObject)metaData).isSelected();
	}
}
