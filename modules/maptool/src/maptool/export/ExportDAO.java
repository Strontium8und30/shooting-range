package maptool.export;

import java.io.*;
import java.util.*;

import maptool.chapter.*;
import maptool.object.*;
import utilities.log.*;
import framework.types.*;

public class ExportDAO {

	public static Log log = LogFactory.getLog(ExportDAO.class);
	
	
	StringBuilder xmlParser = new StringBuilder();
	
	public void exportToMap(Chapter map, File file) throws IOException {
		if(file == null) {
			file = new File(map.getName() == null ? "NoNameMap.xml" : map.getName() + ".xml");
		}
		
		BufferedWriter hFile = new BufferedWriter(new FileWriter("maps/" + file, false));
		
		hFile.write("<map name=\"" + map.getName() + "\">\n");
		xmlParser.append("    ");
		
		java.util.Map<String, Integer> textures = map.getTextureFileMap();
		
		for(String texPath : textures.keySet()) {
			hFile.write(xmlParser + "<texture file=\"" + texPath + "\" id=\"" + textures.get(texPath).toString() + "\"/>\n");
		}
				
		for(ContainerObject cObj : map.getMainContainerObject().getContainerObjectList()) {
			exportToModel(hFile, (ComplexContainerObject)cObj, null, false);
		}
		
		for(PortalInf portal : map.getPortals()) {
			exportPortal(hFile, portal);
		}
				
		xmlParser.delete(0, 4);
		hFile.write("</map>");
		hFile.close();
		log.info("Die Map wurde Exportiert.");
	}
	
	public void exportToModel(Chapter chapter, ComplexContainerObject containerObject, File file) throws IOException {		
		if(file == null) {
			file = new File("models/" + containerObject.getName() + ".xml");
		} else if (file.isDirectory()) {
			file = new File(file.getPath() + "\\"+ containerObject.getName() + ".xml");
		}
		
		BufferedWriter hFile = new BufferedWriter(new FileWriter(file, false));		
		hFile.write("<model>\n");
		xmlParser.append("    ");
		
		for (Integer textureID : containerObject.getTextureIDs()) {
			hFile.write(xmlParser + "<texture file=\"" + chapter.getTexturePath(textureID) + "\" id=\"" + textureID.toString() + "\"/>\n");
		}
		
		exportToModel(hFile, containerObject, containerObject.calcGeometricCenter(), true);			
		xmlParser.delete(0, 4);
		hFile.write("</model>");
		hFile.close();
		
		createPropertieEntriesForModel(file, containerObject);
		
		log.info("Das Objekt \"" + containerObject.getName() + "\" wurde Exportiert");
	}
	
	private void exportToModel(BufferedWriter hFile, ComplexContainerObject containerObject, Vector3D geometricCenter, boolean center) throws IOException {
		hFile.write(xmlParser + "<c_obj " + createHeaderAttributes(containerObject) + ">\n");
		
		xmlParser.append("    ");
		for(ContainerObject cObj : containerObject.getContainerObjectList()) {
			exportToModel(hFile, (ComplexContainerObject)cObj, geometricCenter, center);
		}
		for(GraphicalObject sObj : containerObject.getGraphicalObjectList()) {
			hFile.write(xmlParser + "<s_obj " + createHeaderAttributes(sObj) + "texture=\"" + sObj.getTextureID() + "\">\n");
			xmlParser.append("    ");
			for(int i_vec = 0; i_vec < 3; i_vec++) {
				hFile.write(xmlParser + "<vertex>" + 
								String.valueOf(sObj.getVertex(i_vec).x - (center ? geometricCenter.x : 0)) + ";" +
								String.valueOf(sObj.getVertex(i_vec).y - (center ? geometricCenter.y : 0)) + ";" +
								String.valueOf(sObj.getVertex(i_vec).z - (center ? geometricCenter.z : 0)) + "</vertex>\n");
				hFile.write(xmlParser + "<tex_coord>" + 
								String.valueOf(sObj.getTextureVector(i_vec).x) + ";" +
								String.valueOf(sObj.getTextureVector(i_vec).y) + "</tex_coord>\n");
			}
			xmlParser.delete(0, 4);
			hFile.write(xmlParser + "</s_obj>\n");
		}
		xmlParser.delete(0, 4);
		hFile.write(xmlParser + "</c_obj>\n");
	}
	
	public void exportPortal(BufferedWriter hFile, PortalInf portal) throws IOException {
		hFile.write(xmlParser + "<portal type=\"" + portal.getType().name() + "\">\n");
		xmlParser.append("    ");
			hFile.write(xmlParser + "<port>" + portal.getPortalA().getId() + "</port>\n");
			hFile.write(xmlParser + "<port>" + portal.getPortalB().getId() + "</port>\n");
		xmlParser.delete(0, 4);
		hFile.write(xmlParser + "</portal>\n");
	}
	
	private String createHeaderAttributes(ObjectMetaData metaData) {
		StringBuilder header = new StringBuilder();
		if (metaData.getId() != null && !metaData.getId().equals(ObjectMetaDataDefaults.ID)) {
			header.append("id=\"" + metaData.getId() + "\" ");
		} 
		if (metaData.isDynamic() != ObjectMetaDataDefaults.DYNAMIC) {
			header.append("dynamic=\"" + metaData.isDynamic() + "\" ");
		}
		if (metaData.isVisible() != ObjectMetaDataDefaults.VISIBLE) {
			header.append("visible=\"" + metaData.isVisible() + "\" ");
		}
		if (metaData.isHard() != ObjectMetaDataDefaults.HARD) {
			header.append("hard=\"" + metaData.isHard() + "\" ");
		}
		if (metaData.isShootable() != ObjectMetaDataDefaults.SHOOTABLE) {
			header.append("shootable=\"" + metaData.isShootable() + "\" ");
		}
		if (metaData.isDestroyable() != ObjectMetaDataDefaults.DESTROYABLE) {
			header.append("destroyable=\"" + metaData.isDestroyable() + "\" ");
		}
		if (metaData.getMass() != ObjectMetaDataDefaults.MASS) {
			header.append("mass=\"" + metaData.getMass() + "\" ");
		}
		if (metaData.getPower() != ObjectMetaDataDefaults.POWER) {
			header.append("power=\"" + metaData.getPower() + "\" ");
		}
		
		if (metaData instanceof GraphicalObject) {
			if (!((ComplexObjectMetaData) metaData).getName().equals(ObjectMetaDataDefaults.NAME)) {
				header.append("name=\"" + ((ComplexObjectMetaData) metaData).getName() + "\" ");
			} 	
		} else {
			if (!((ComplexObjectMetaData) metaData).getName().equals(ObjectMetaDataDefaults.NAME)) {
				header.append("name=\"" + ((ComplexObjectMetaData) metaData).getName() + "\" ");
			} 
		}
		
		return header.toString();
	}
	
	private void createPropertieEntriesForModel(File file, ComplexContainerObject containerObject) {
		try {
			Properties properties = new Properties();
			properties.load(new FileReader(new File("models/models.properties")));
			properties.setProperty("model.list", properties.get("model.list") + "," + containerObject.getName());
			properties.put("model." + containerObject.getName() + ".file", "models/" + file.toString().substring(file.toString().lastIndexOf('\\')+1));
			properties.store(new FileOutputStream(new File("models/models.properties")), "");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
