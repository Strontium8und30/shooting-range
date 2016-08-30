package maptool.legacy;

import java.io.*;
import java.util.*;

import maptool.object.*;

import org.jdom.*;
import org.jdom.input.*;

import utilities.gl.*;
import utilities.log.*;

public class ImportDAO {

	/** Logging */
	public static Log log = LogFactory.getLog(ImportDAO.class);
	
	
	public ComplexContainerObject importToModel(TextureHelper textureHelper, File importFile) throws JDOMException, IOException {
		if (importFile == null) return null;
		Document doc = new SAXBuilder().build(importFile);	
		Map<Integer, Integer> compareTextureIDs = new HashMap<Integer, Integer>();
		Element root = doc.getRootElement();
		for(Object textureElement : root.getChildren("texture")) {
			String file = ((Element)textureElement).getAttribute("file").getValue();
			int newTextureID = ((Element)textureElement).getAttribute("id").getIntValue();
			Integer oldTextureID = textureHelper.getTextureID(file);
			if (oldTextureID == null) {
				oldTextureID = newTextureID;
				if (textureHelper.containsTextureID(oldTextureID)) {
					oldTextureID = textureHelper.generateTextureID();
				}
				textureHelper.prepareTexture(oldTextureID, file);
			}
			compareTextureIDs.put(newTextureID, oldTextureID);
		}
		
		return importToModel(root.getChild("c_obj"), compareTextureIDs, true);
	}
	
	private ComplexContainerObject importToModel(Element tag, Map<Integer, Integer> compareTextureIDs, boolean flat) throws DataConversionException {	
		ComplexContainerObject complexObject = new ComplexContainerObject();
		
		Attribute tmpAttribute;
		if ((tmpAttribute = tag.getAttribute("name")) != null) {
			complexObject.setName(tmpAttribute.getValue());
		}
		/*
		 * weiter Attribute...		
		 */
		
		for(Object tagg : tag.getChildren("c_obj")) {
			ComplexContainerObject cObj = importToModel((Element)tagg, compareTextureIDs, flat);
			if (flat) {
				complexObject.addAllGraphicalObject(cObj.getGraphicalObjectList());
			} else {
				complexObject.addContainerObject(cObj);
			}
		}
		
		for(Object tagg : tag.getChildren("s_obj")) {
			ComplexGraphicalObject simpleObject = new ComplexGraphicalObject();
			simpleObject.setName("Dreieck");
			Attribute attr = ((Element)tagg).getAttribute("texture");
			if(attr != null) {		
				simpleObject.setTextureID(compareTextureIDs.get(attr.getIntValue()));
			}
			int i_vec = 0;
			for (Object vertexTag : ((Element)tagg).getChildren("vertex")) {
				String[] vertexValue = ((Element)vertexTag).getText().split(";");
									
				simpleObject.getVertex(i_vec++).setVector(
						Float.valueOf(vertexValue[0]),
						Float.valueOf(vertexValue[1]),
						Float.valueOf(vertexValue[2]));
			}
			i_vec = 0;
			for (Object textureVertexTag : ((Element)tagg).getChildren("tex_coord")) {
				String[] textureVertexValue = ((Element)textureVertexTag).getText().split(";");
				simpleObject.getTextureVector(i_vec++).setVector(
						Float.valueOf(textureVertexValue[0]),
						Float.valueOf(textureVertexValue[1]));
			}
			complexObject.addGraphicalObject(simpleObject);
		}		
		return complexObject;
	}
}
