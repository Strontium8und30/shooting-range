package utilities.gl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.media.opengl.GL;
import javax.media.opengl.GLException;

import utilities.log.Log;
import utilities.log.LogFactory;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

public class TextureHelper {

	/** Logging */
	private Log log = LogFactory.getLog(TextureHelper.class);
	
	/** Die bereits geladenen Texturen */
	private Map<Integer, Texture> loadedTextures = new HashMap<Integer, Texture>();
	
	/** Die bereits geladenen Texturen */
	private Map<String, Integer> loadedTexturesIDs = new HashMap<String, Integer>();
	
	/** Sind alle vorbereiteten texturen auch geladen? */
	private boolean texturesFetched = false;
	
	private Texture createTexture(String file) {
		Texture texture = null;
		try {
			texture = TextureIO.newTexture(new File(file), true);
			texture.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			texture.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
			texture.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
			texture.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
		} catch (GLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return texture;
	}
	
	public Integer prepareTexture(int id, String path) {
		Integer ids = loadedTexturesIDs.get(path.toLowerCase());
		if (ids == null) {
			loadedTexturesIDs.put(path.toLowerCase(), id);
			if (texturesFetched == true) {
				log.warning("Eine Texture musste nachgeladen werden: " + path);
			}
			texturesFetched = false;
			return id;
		} else if (!ids.equals(id)) {
			log.warning("Eine Texture existiert doppelt mit verschiedenen ids: Id = " + ids + " Pfad: " + path);
		} else {
			log.warning("Diese Texture wurde bereits geladen: Id = " + ids + " Pfad: " + path);
		}
		return ids;
	}
	
	public Integer prepareTexture(String path) {
		return prepareTexture(generateTextureID(), path);
	}
	
	public Texture getTexture(int id) {
		Texture texture = loadedTextures.get(id);
		if (texture == null) {
			throw new NoSuchElementException("Es existiert keine Texture mit der ID: " + id);
		} else {
			return texture;
		}
	}
	
	public Map<String, Integer> getTextureFileMap() {
		return loadedTexturesIDs;
	}
	
	private boolean addTexture(int id, String path) {
		Texture texture = createTexture(path);
		loadedTextures.put(id, texture);
		return true;
	}
	
	public Integer getTextureID(String path) {
		Integer id = loadedTexturesIDs.get(path);
		if (id == null) {
			return prepareTexture(path);
		} else {
			return id;
		}
	}
	
	public String getTexturePath(int id) {
		for (String path : loadedTexturesIDs.keySet()) {
			if (loadedTexturesIDs.get(path).equals(id)) {
				return path;
			}
		}
		return null;
	}

	public boolean containsTextureID(int id) {
		return loadedTexturesIDs.containsValue(id);
	}
	
	public boolean containsTexture(String texturePath) {
		return loadedTexturesIDs.containsKey(texturePath);
	}
	
	public int generateTextureID() {
		int id = 0;
		while(loadedTexturesIDs.containsValue(id)) {
			id++;
		}
		return id;
	}
	
	public void fetchTextures() {
		for (String path : loadedTexturesIDs.keySet()) {
			addTexture(loadedTexturesIDs.get(path), path);
		}
		texturesFetched = true;
	}
	
	public boolean isFetched() {
		return texturesFetched;
	}
}
