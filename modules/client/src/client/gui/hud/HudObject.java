package client.gui.hud;

import javax.media.opengl.*;

/**
 * Ein HUDObejct ist ein Teil der GUI des Spiels.
 * Beispiele: Gesundheitsanzeige, Fadenkreutz, Waffen, Munition 
 */
public abstract class HudObject {

	/** Wird das HudObject angezeigt */
	private boolean visible;
	
	public abstract void draw(GL gl);
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
