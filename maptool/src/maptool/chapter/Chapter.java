package maptool.chapter;

import java.util.Map;

import maptool.object.ComplexContainerObject;
import utilities.gl.TextureHelper;
import framework.types.ContainerObject;
import framework.types.GraphicalObject;

public class Chapter extends chapter.Chapter {
	
	public Chapter(ContainerObject cObj) {
		super(cObj);
	}
	
	@Override
	public ComplexContainerObject getMainContainerObject() {
		return (ComplexContainerObject) containerObject;
	}
	
	public String getTexturePath(int id) {
		return textureHelper.getTexturePath(id);
	}
	
	public boolean containsTextureID(int id) {
		return textureHelper.containsTextureID(id);
	}
	
	public int generateTextureID() {
		return textureHelper.generateTextureID();
	}

	public void fetchTextures() {
		textureHelper.fetchTextures();
	}
	
	public Map<String, Integer> getTextureFileMap() {
		return textureHelper.getTextureFileMap();
	}
	
	public TextureHelper getTextureHelper() {
		return textureHelper;
	}
	
	public boolean isFetched() {
		return textureHelper.isFetched();
	}
	
	public void autoGridAlignment() {
		autoGridAlignment(getMainContainerObject());
	}
	
	private void autoGridAlignment(ComplexContainerObject complexObject) {
		if(!complexObject.isAutoAlignment()) {
			return;
		}
		for(ContainerObject cObj : complexObject.getContainerObjectList()) {
			autoGridAlignment((ComplexContainerObject)cObj);
		}
		for(GraphicalObject sObj : complexObject.getGraphicalObjectList()) {
			for(int i_vec = 0; i_vec < 3; i_vec++) {
				sObj.getVertex(i_vec).autoGridAlignment();
			}
		}
	}
}

