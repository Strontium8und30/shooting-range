package maptool.chapter;

import java.io.File;

import maptool.object.ComplexContainerObject;
import maptool.object.ComplexGraphicalObject;

import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;

import utilities.log.Log;
import utilities.log.LogFactory;
import framework.types.ContainerObject;
import framework.types.GraphicalObject;

public class MapLoader extends chapter.MapLoader {

	public static Log log = LogFactory.getLog(MapLoader.class);
	
	@Override
	public Chapter loadMap(File mapFile) {
		return (Chapter) super.loadMap(mapFile);
	}
	
	@Override
	public Chapter createEmptyChapter() {
		return new Chapter(new ComplexContainerObject("Object"));
	}
	
	@Override
	protected ContainerObject createContainerObject() {
		return new ComplexContainerObject();
	}
	
	@Override
	protected GraphicalObject createGraphicalObject() {
		return new ComplexGraphicalObject();
	}
	
	@Override
	protected void setAttributes(Element tag, ContainerObject containerObject, ContainerObject parent) throws DataConversionException {
		super.setAttributes(tag, containerObject, parent);
		ComplexContainerObject cObj = (ComplexContainerObject) containerObject;
		Attribute objectName;
		if ((objectName = tag.getAttribute("name")) != null) {
			cObj.setName(objectName.getValue());
		} else {
			cObj.setName("no Name");
		}
	}	
	
	@Override
	protected void setAttributes(Element tag, GraphicalObject gObj) throws DataConversionException {
		super.setAttributes(tag, gObj);
		ComplexGraphicalObject cgObj = (ComplexGraphicalObject) gObj;
		cgObj.setName("Dreieck");
	}
}
