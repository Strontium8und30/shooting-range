package framework.physics;

import framework.types.Vector3D;

/**
 * In dieser Klasse werden physikalische Konstaten definiert
 * 
 * @author Thorben 
 */
public class Physics{
	
	public static final int IMPULS = 0;
	public static final int ENERGY = 1;
	
	public enum Kraft{	
		SCHWERKRAFT(0.0f,-0.05f, 0.0f, ENERGY),
		SPRUNGKRAFT(0.0f,0.5f, 0.0f, IMPULS);	
		
		/**
		 * In welche Richtung richtet sich die Kraft
		 * Die länge des Vektors gibt an wie stark die Kraft ist
		 */ 
		Vector3D direction;
		
		/** Von welchem Typ ist die Kraft */
		int type;
		
		private Kraft(float drcX, float drcY, float drcZ, int typ) {
			direction = new Vector3D(drcX, drcY, drcZ);			
			this.type = typ;
		}
		
		public Vector3D getDirection() {
			return new Vector3D(direction);
		}
		
		public int getType() {
			return type;
		}
	}
}
