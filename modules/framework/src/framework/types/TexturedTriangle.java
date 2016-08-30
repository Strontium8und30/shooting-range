package framework.types;


public class TexturedTriangle extends Triangle {

	/** Holds the three vertex of the Texture */
	protected Vector2D textureVertex[] = new Vector2D[3];
	
	/** Holds the id of a texture */
	protected int textureID = 0;
	
	
	public TexturedTriangle() {
		textureVertex[0] = new Vector2D();
		textureVertex[1] = new Vector2D();
		textureVertex[2] = new Vector2D();
	}
	
	public TexturedTriangle(TexturedTriangle texturedTriangle) {
		super(texturedTriangle);
		this.textureVertex[0] = new Vector2D(texturedTriangle.getTextureVector(0));
		this.textureVertex[1] = new Vector2D(texturedTriangle.getTextureVector(1));
		this.textureVertex[2] = new Vector2D(texturedTriangle.getTextureVector(2));
		this.textureID = texturedTriangle.getTextureID();
	}

	public Vector2D getTextureVector(int index) {
		return textureVertex[index];
	}
	
	public void setTextureVector(int index, Vector2D vertex) {
		textureVertex[index].x = vertex.x;
		textureVertex[index].y = vertex.y;
	}
	
	public int getTextureID() {
		return textureID;
	}
	
	public void setTextureID(int textureID) {
		this.textureID = textureID;
	}	
	
	@Override
	public TexturedTriangle resize(float factor) {
		super.resize(factor);
		getTextureVector(0).multiplyBy(factor);
		getTextureVector(1).multiplyBy(factor);
		getTextureVector(2).multiplyBy(factor);
		return this;
	}
}
