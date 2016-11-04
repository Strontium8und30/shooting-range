package framework.weapons.crosshair;


public abstract class AbstractCrossHair implements CrossHair {

	/** Die maximale größe des Fadenkreuzes */
	private static float MAX_CROSSHAIRSIZE = 1.0f;
	
	/** Die minimale größe des Fadenkreuzes */
	private static float MIN_CROSSHAIRSIZE = 0.0f;
	
	/** Die größe des Fadenkreuzes */
	private static float crossHairSize = 0.0f;
	
	
	@Override
	public float getCrossHairSize() {
		return crossHairSize;
	}
	
	@Override
	public void addCrossHairSize(float value) {
		if (crossHairSize >= MAX_CROSSHAIRSIZE) {
			crossHairSize = MAX_CROSSHAIRSIZE;
		} else {
			crossHairSize += value;
		}
	}
	
	@Override
	public void removeCrossHairSize() {
		if (crossHairSize <= MIN_CROSSHAIRSIZE) {
			crossHairSize -= MIN_CROSSHAIRSIZE;
		} else {
			crossHairSize -= 0.1f;
		}
	}
}
