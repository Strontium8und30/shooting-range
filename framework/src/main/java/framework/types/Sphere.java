package framework.types;


public class Sphere {
	
	/** Der Mittelpunkt der Kugel */
	private Vector3D centerPoint;
	
	/** Der Radius der Kugel */
	private float radius;
	
	
	public Sphere(Vector3D centerPoint, float radius) {
		this.centerPoint = centerPoint;
		this.radius = radius;
	}


	public Vector3D getCenterPoint() {
		return centerPoint;
	}

	public void setCenterPoint(Vector3D centerPoint) {
		this.centerPoint = centerPoint;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	public void checkLineSphereCollision(Line line) {
		throw new UnsupportedOperationException("Diese Funktionalität wird noch nicht unterstützt");
	}
}
