package framework.physics;

import framework.types.*;

public class PhysicsObject {

	/** Resultierende Kraft richtung */
	public Vector3D kraftResultDirection = new Vector3D();
	
	
	public Vector3D getKraftResultDirection() {
		return kraftResultDirection;
	}

	public void setKraftResultDirection(Vector3D kraftResultDirection) {
		this.kraftResultDirection = kraftResultDirection;
	}
	
	public void addKraft(Physics.Kraft kraft) {
		kraftResultDirection.add(kraft.getDirection());
	}

	public void executePower() {
		
	}
}
