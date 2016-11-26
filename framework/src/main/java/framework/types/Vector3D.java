package framework.types;


public class Vector3D {

	public float x;
	public float y;
	public float z;
	
	public Vector3D() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public Vector3D(float vecXYZ) {
		this(vecXYZ, vecXYZ, vecXYZ);
	}
	
	public Vector3D(Vector3D vec) {
		if (vec != null) {
			this.x = vec.x;
			this.y = vec.y;
			this.z = vec.z;
		}
	}
	
	public Vector3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3D copyVector(Vector3D vec) {	
		return new Vector3D(vec);
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float getZ() {
		return z;
	}
	
	public void setZ(float z) {
		this.z = z;
	}
	
	public void setVector(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setVector(Vector3D vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}
	
	public boolean isNull() {
		return this.x == 0 && this.y == 0 && this.z == 0;
	}
	
	public Vector3D invert() {
		x = x * -1;
		y = y * -1;
		z = z * -1;
		return this;
	}
	
	public static Vector3D add(Vector3D vector1, Vector3D vector2) {
		return new Vector3D(vector1.x + vector2.x,
							vector1.y + vector2.y,
							vector1.z + vector2.z);		
	}
	
	public Vector3D add(Vector3D vec) {		
		this.x += vec.x;
		this.y += vec.y;
		this.z += vec.z;
		return this;
	}
	
	public static Vector3D subtract(Vector3D vector1, Vector3D vector2) {
		return new Vector3D(vector1.x - vector2.x,
							vector1.y - vector2.y,
							vector1.z - vector2.z);		
	}
	
	public Vector3D subtract(Vector3D vec) {
		this.x -= vec.x;
		this.y -= vec.y;
		this.z -= vec.z;
		return this;
	}
	
	public static Vector3D multiplyBy(Vector3D vector, float factor) {
		return new Vector3D(vector.x * factor,
							vector.y * factor,
							vector.z * factor);	
	}
	
	public Vector3D multiplyBy(float factor) {
		this.x *= factor;
		this.y *= factor;
		this.z *= factor;
		return this;
	}
	
	public static float produkt(Vector3D vector1, Vector3D vector2) {
		return vector1.x * vector2.x + vector1.y * vector2.y + vector1.z * vector2.z;
	}
	
	public static Vector3D crossproduct(Vector3D vector1, Vector3D vector2) {
		Vector3D vecResult = new Vector3D();
		
		vecResult.x = (vector1.y * vector2.z - vector1.z * vector2.y) * -1;
		vecResult.y = (vector1.z * vector2.x - vector1.x * vector2.z) * -1;
		vecResult.z = (vector1.x * vector2.y - vector1.y * vector2.x) * -1;

		return vecResult;
	}
	
	public Vector3D normalize() {		
		float vecLength = (float)Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z); 
		
		this.x = this.x / vecLength;
		this.y = this.y / vecLength;
		this.z = this.z / vecLength;
		return this;
	}
	
	public void rotate(float angX, float angY, float angZ) {
		Vector3D rotateCenter = new Vector3D();
		Vector3D newPos = new Vector3D();
		
		subtract(rotateCenter);
		
		//Drehung um die Z-Achse
		if(angZ != 0) {
			float angleRad = (float)(angZ / 180 * Math.PI);
			newPos.x = (float)(this.x * Math.cos(angleRad) - this.y * Math.sin(angleRad));
			newPos.y = (float)(this.x * Math.sin(angleRad) + this.y * Math.cos(angleRad));
			newPos.z = (float)(this.z);
			this.setVector(newPos);
		}
		
		//Drehung um die Y-Achse
		if(angY != 0) {
			double angleRad = -(angY / 180 * Math.PI);
			newPos.x = (float)(this.x * Math.cos(angleRad) - this.z * Math.sin(angleRad));
			newPos.y = this.y;
			newPos.z = (float)(this.x * Math.sin(angleRad) + this.z * Math.cos(angleRad));
			this.setVector(newPos);
		}
		
		//Drehung um die X-Achse
		if(angX != 0) {
			double angleRad = (angX / 180 * Math.PI);
			newPos.x = (float)(this.x);
			newPos.y = (float)(this.y * Math.cos(angleRad) - this.z * Math.sin(angleRad));
			newPos.z = (float)(this.y * Math.sin(angleRad) + this.z * Math.cos(angleRad)); 
			this.setVector(newPos);
		}		
		this.add(rotateCenter);
	}
	
	public void autoGridAlignment() {
		x = autoGridAlignment(x);
		y = autoGridAlignment(y);
		z = autoGridAlignment(z);
	}
	
	private float autoGridAlignment(float vertex) {
		
		float vertex_copy = vertex;
		float grid_precision = 0.5f;
//		float moreThanXperCentWarning = 50;
		float rest = vertex % grid_precision;
		if (Math.abs(rest) > (grid_precision / 2)) {
			if (rest < 0) {
				vertex_copy = vertex_copy - grid_precision - rest;
			} else {
				vertex_copy = vertex_copy + grid_precision - rest;
			}
			
		} else {
			vertex_copy = vertex_copy - rest;
		}
		return vertex_copy;
	}

	public float betrag() {
		return (float)Math.pow(this.x * this.x + this.y * this.y + this.z * this.z, 0.5f);
	}
	
	public float dinstance(Vector3D vector) {
		return (float)Math.pow(Math.pow(getX() - vector.getX(), 2) + 
			                   Math.pow(getY() - vector.getY(), 2) +
			                   Math.pow(getZ() - vector.getZ(), 2), 0.5);
	}
	
	public static float dinstance(Vector3D vector1, Vector3D vector2) {
		return (float)Math.pow(Math.pow(vector1.getX() - vector2.getX(), 2) + 
			                   Math.pow(vector1.getY() - vector2.getY(), 2) +
			                   Math.pow(vector1.getZ() - vector2.getZ(), 2), 0.5);
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (object instanceof Vector3D) {
			Vector3D vector = (Vector3D)object;
			if(this.x == vector.x && 
			   this.y == vector.y && 
			   this.z == vector.z) return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "x = " + x + " | y = " + y + " | z = " + z;	
	}
}
