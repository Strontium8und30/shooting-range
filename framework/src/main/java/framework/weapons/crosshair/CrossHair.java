package framework.weapons.crosshair;

import javax.media.opengl.*;

public interface CrossHair {
	
	public float getCrossHairSize();
	
	public void addCrossHairSize(float value);
	
	public void removeCrossHairSize();
	
	public void draw(GL gl);

}
