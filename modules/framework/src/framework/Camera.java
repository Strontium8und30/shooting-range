package framework;

import framework.types.*;

public class Camera {

	/** Postion der Kamera */
	private Vector3D position;

	/** letzte Postion der Kamera */
	private Vector3D lastPosition;

	/** Blickwinkel der Kamera Horizontal*/
	private float angleHorizontal = 0;

	/** Blickwinkel der Kamera Vertical*/
	private float angleVertical = 0;

	/** Der aktuelle Sprungfaktor */
	public Vector3D jumpStep = new Vector3D(0.0f, -0.006f, 0.0f);
	
	/** Der aktuelle Sprungfaktor */
	public Vector3D jumpHeight = new Vector3D(0.0f, 0.4f, 0.0f);
	
	/** Der aktuelle Sprungfaktor */
	public Vector3D jumpFactor = new Vector3D(jumpHeight);
	
	
	public Camera() {
		this(new Vector3D(1,1,5));
	}
	
	public Camera(Vector3D startPosition) {
		position = startPosition;
		lastPosition = startPosition;
	}
	
	/**
	 * Gibt die Blickwinkel zurück x(horizontal) bzw. y(vertikal) 
	 */
	public float getAngleHorizontal() {
		return angleHorizontal;
	}
	
	public void setAngleHorizontal(float angle) {
		this.angleHorizontal = angle;
	}
	
	public float getAngleVertical() {
		return angleVertical;
	}
	
	public void setAngleVertical(float angle) {
		this.angleVertical = angle;
	}
	
	public Vector3D getLastPosition() {
		return new Vector3D(lastPosition);
	}
	
	public void setLastPosition(Vector3D lastPosition) {
		this.lastPosition = new Vector3D(lastPosition);
	}
	
	public Vector3D getPosition() {
		return new Vector3D(position);
	}
	
	public void setPosition(Vector3D position) {
		this.position = new Vector3D(position);
	}
	
	/**
	 * Gibt einen Blickpunkt als 3D Vektor zurück 
	 */
	public Vector3D getAngleAsVertex() {
		Vector3D angleVector = new Vector3D();
		angleVector.x = (float)Math.sin(-getAngleHorizontal() / 180 * Math.PI); 
	    angleVector.y = (float)Math.tan(getAngleVertical() / 180 * Math.PI); 
	    angleVector.z = (float)Math.cos(getAngleHorizontal() / 180 * Math.PI);
	    return angleVector.normalize();
	}
	
	/**
	 * Gibt einen Blickpunkt als 3D Vektor zurück 
	 */
	public static Vector3D getAngleAsVertex(float angHor, float angVer) {
		Vector3D angleVector = new Vector3D();
		angleVector.x = (float)Math.sin(-angHor / 180 * Math.PI); 
	    angleVector.y = (float)Math.tan(angVer / 180 * Math.PI); 
	    angleVector.z = (float)Math.cos(angHor / 180 * Math.PI);
	    return angleVector.normalize();
	}
}
