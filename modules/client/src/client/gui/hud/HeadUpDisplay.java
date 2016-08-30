package client.gui.hud;

import java.util.*;

import javax.media.opengl.*;

/** 
 * Das HeadUpDisplay hält alle HudObejcts und verwaltet sie Global. 
 */
public class HeadUpDisplay {

	/** Liste mit HudObject's */
	private Set<HudObject> hudObjects = new HashSet<HudObject>();

	private GL gl;
	
	public HeadUpDisplay(GL gl) {
		this.gl = gl;
	}
	
	public void addHudObject(HudObject hudObject) {
		hudObjects.add(hudObject);
	}
	
	public void draw() {
		for (HudObject hudObject : hudObjects) {
			hudObject.draw(gl);
		}
	}
}
