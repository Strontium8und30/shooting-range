package framework.legacy;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import framework.types.SimpleContainerObject;
import framework.types.SimpleGraphicalObject;
import framework.types.Vector2D;
import framework.types.Vector3D;
import utilities.gl.TextureHelper;
import utilities.resource.ResourceLoader;

public class ImportDAO {

	/** ObjectHelper Instanz */
	private static ImportDAO instance;
	
	
	protected ImportDAO getInstance() {
		return instance == null ? instance = new ImportDAO() : instance;
	}
	
	protected SimpleContainerObject importToModel(TextureHelper textureHelper, File importFile) throws JDOMException, IOException {
		if (importFile == null) return null;
		Document doc = new SAXBuilder().build(ResourceLoader.getFile(importFile.toString()));	
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
	
	private SimpleContainerObject importToModel(Element tag, Map<Integer, Integer> compareTextureIDs, boolean flat) throws DataConversionException {	
		SimpleContainerObject complexObject = new SimpleContainerObject();
		/*
		 * weiter Attribute...		
		 */
		
		for(Object tagg : tag.getChildren("c_obj")) {
			SimpleContainerObject cObj = importToModel((Element)tagg, compareTextureIDs, flat);
			if (flat) {
				complexObject.addAllGraphicalObject(cObj.getGraphicalObjectList());
			} else {
				complexObject.addContainerObject(cObj);
			}
		}
		
		for(Object tagg : tag.getChildren("s_obj")) {
			SimpleGraphicalObject graphicalObject = new SimpleGraphicalObject();
			Attribute attr = ((Element)tagg).getAttribute("texture");
			if(attr != null) {		
				graphicalObject.setTextureID(compareTextureIDs.get(attr.getIntValue()));
			}
			int i_vec = 0;
			for (Object vertexTag : ((Element)tagg).getChildren("vertex")) {
				String[] vertexValue = ((Element)vertexTag).getText().split(";");
									
				graphicalObject.setVertex(i_vec++, new Vector3D(
														Float.valueOf(vertexValue[0]),
														Float.valueOf(vertexValue[1]),
														Float.valueOf(vertexValue[2])));
			}
			i_vec = 0;
			for (Object textureVertexTag : ((Element)tagg).getChildren("tex_coord")) {
				String[] textureVertexValue = ((Element)textureVertexTag).getText().split(";");
				graphicalObject.setTextureVector(i_vec++, new Vector2D(
						Float.valueOf(textureVertexValue[0]),
						Float.valueOf(textureVertexValue[1])));
			}
			complexObject.addGraphicalObject(graphicalObject);
		}		
		return complexObject;
	}
}
