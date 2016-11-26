package framework.types;


/**
 * Die Klasse Triangle bietet alle möblichkeiten ein Dreieck zu beschreiben 
 * und eine vielzahl von Methoden um bestimmte berechnungen durchzuführen
 * @author drosteth
 *
 */
public class Triangle {
	
	/** Holds the three vertex of the Triangle */
	private Vector3D vertex[] = new Vector3D[3];
	
	/** Normalvektor der Ebene */
	private Vector3D normalVector;
	
	/** Lage der Ebene */
	private float position;
	
	public Triangle() {
		vertex[0] = new Vector3D();
		vertex[1] = new Vector3D();
		vertex[2] = new Vector3D();
	}
	
	public Triangle(Vector3D vector1, Vector3D vector2, Vector3D vector3) {
		vertex[0] = vector1;
		vertex[1] = vector2;
		vertex[2] = vector3;
		calcComplete();
	}
	
	public Triangle(Triangle triangle) {
		this.vertex[0] = new Vector3D(triangle.getVertex(0));
		this.vertex[1] = new Vector3D(triangle.getVertex(1));
		this.vertex[2] = new Vector3D(triangle.getVertex(2));
		this.normalVector = new Vector3D(triangle.getNormalVector());
		this.position = triangle.getPosition();
	}
	
	public Vector3D getVertex(int index) {
		return vertex[index];
	}

	public void setVertex(int index, Vector3D vector) {
		vertex[index] = vector;
		calcComplete();
	}
	
	public Line getLine(int index) {
		return new Line(vertex[index], vertex[index >= 2 ? 0 : index + 1]);
	}
	
	public Vector3D getNormalVector() {
		return normalVector;
	}

	public float getPosition() {
		return position;
	}
	
	public void calcComplete() {		
		calcNormalVector();
		calcPosition();
	}
	
	/** 
	 * Berechnet den Normalvektor 
	 */
	private void calcNormalVector() {
		normalVector = Vector3D.crossproduct(
							Vector3D.subtract(getVertex(1), getVertex(0)),
							Vector3D.subtract(getVertex(2), getVertex(0)));
		normalVector.normalize();
	}
	
	/** 
	 * Berechnet die Lage der Ebene 
	 */
	private void calcPosition() {
		position = -(normalVector.getX() * getVertex(0).getX() +	
			     	 normalVector.getY() * getVertex(0).getY() +
			     	 normalVector.getZ() * getVertex(0).getZ());
	}
	
	public Triangle transfare(Vector3D vector) {
		vertex[0].add(vector);
		vertex[1].add(vector);
		vertex[2].add(vector);
		calcPosition();
		return this;
	}
	
	public static Triangle transfare(Triangle triangle, Vector3D vector) {
		return new Triangle(triangle).transfare(vector);
	}
	
	public Triangle rotate(float angX, float angY, float angZ) {
		vertex[0].rotate(angX, angY, angZ);
		vertex[1].rotate(angX, angY, angZ);
		vertex[2].rotate(angX, angY, angZ);
		calcComplete();
		return this;
	}
	
	public static Triangle rotate(Triangle triangle, float angX, float angY, float angZ) {
		return new Triangle(triangle).rotate(angX, angY, angZ);
	}
	
	public Triangle resize(float factor) {
		vertex[0].multiplyBy(factor);
		vertex[1].multiplyBy(factor);
		vertex[2].multiplyBy(factor);
		calcPosition();
		return this;
	}
	
	public Vector3D getMax() {
		Vector3D max = new Vector3D(getVertex(0));
		for(int i_vec = 1; i_vec < 3; i_vec++) {
			if(getVertex(i_vec).x > max.x) max.x = getVertex(i_vec).x;
			if(getVertex(i_vec).y > max.y) max.y = getVertex(i_vec).y;
			if(getVertex(i_vec).z > max.z) max.z = getVertex(i_vec).z;
		}
		return max;
	}
	
	public Vector3D getMin() {
		Vector3D min = new Vector3D(getVertex(0));
		for(int i_vec = 1; i_vec < 3; i_vec++) {
			if(getVertex(i_vec).x < min.x) min.x = getVertex(i_vec).x;
			if(getVertex(i_vec).y < min.y) min.y = getVertex(i_vec).y;
			if(getVertex(i_vec).z < min.z) min.z = getVertex(i_vec).z;
		}
		return min;
	}
	
	public boolean checkTriangleTriangleCollision(Triangle triangle) {
		if (checkLineTriangleCollision(triangle.getLine(0)) != null || 
			checkLineTriangleCollision(triangle.getLine(1)) != null ||
			checkLineTriangleCollision(triangle.getLine(2)) != null ||
			triangle.checkLineTriangleCollision(getLine(0)) != null || 
			triangle.checkLineTriangleCollision(getLine(1)) != null ||
			triangle.checkLineTriangleCollision(getLine(2)) != null) {
			return true;
		}
		return false;
	}
	
	public Vector3D checkLineTriangleCollision(Line line) {
		
		Vector3D dirVec = Vector3D.subtract(line.getEndPoint(), line.getStartPoint());
		
		float tmp = Vector3D.produkt(normalVector, dirVec);
		
		if(Math.abs(tmp) > 0) {
			float t = Vector3D.produkt(normalVector, line.getStartPoint()) + position;
			float s = t / -tmp;
			if (line.isOnLine(s) || Math.abs(t) <= 0.0001) {
				Vector3D intersectionPoint = Vector3D.add(line.getStartPoint(), Vector3D.multiplyBy(dirVec, s));
				if(isPointInTriangle(intersectionPoint)) {
					return intersectionPoint;
				}
			}
		}
		return null;
	}
	
	public boolean isPointInTriangle(Vector3D vector) {
		Vector3D temp;

		temp = Vector3D.crossproduct(Vector3D.subtract(vertex[0], vertex[1]), normalVector);
		if (Vector3D.produkt(vector, temp) - Vector3D.produkt(vertex[0], temp) < 0) {
			return false;
		}
		
		temp = Vector3D.crossproduct(Vector3D.subtract(vertex[1], vertex[2]), normalVector);
		if (Vector3D.produkt(vector, temp) - Vector3D.produkt(vertex[1], temp) < 0) {
			return false;
		}
		
		temp = Vector3D.crossproduct(Vector3D.subtract(vertex[2], vertex[0]), normalVector);
		if (Vector3D.produkt(vector, temp) - Vector3D.produkt(vertex[2], temp) < 0) {
			return false;
		}
		return true;
	}
	
	public boolean isInFrontOf(Vector3D vector) {
		return Vector3D.produkt(getNormalVector(), vector) + getPosition() > 0;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (object instanceof Triangle) {
			Triangle triangle = (Triangle)object;
			return vertex[0].equals(triangle.getVertex(0)) &&
				   vertex[1].equals(triangle.getVertex(1)) && 
				   vertex[2].equals(triangle.getVertex(2));
		}
		return false;
	}
	
	@Override
	public String toString() {
		return vertex[0] + " || " + vertex[1] + " || " + vertex[2];
	}
	
	public String getDescription() {
		return toString();
	}
}
