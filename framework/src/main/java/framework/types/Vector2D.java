package framework.types;

import java.awt.*;

public class Vector2D {
	
	public float x;
	public float y;
	
	public Vector2D() {}
	
	public Vector2D(Vector2D vector) {
		this(vector.getX(), vector.getY());
	}
	
	public Vector2D(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2D(Dimension dim) {
		this((float)dim.getWidth(), (float)dim.getHeight());
	}
	
	public Vector2D(Point dim) {
		this((float)dim.getX(), (float)dim.getY());
	}
		
	public void setVector(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setVector(Vector2D vec) {
		setVector(vec.x, vec.y);
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
	
	public Vector2D add(Vector2D vector) {
		this.x += vector.getX();
		this.y += vector.getY();
		return this;
	}
	
	public static Vector2D multiplyBy(Vector3D vector, float factor) {
		return new Vector2D(vector.x * factor,
							vector.y * factor);	
	}
	
	public Vector2D multiplyBy(float factor) {
		this.x *= factor;
		this.y *= factor;
		return this;
	}
}
