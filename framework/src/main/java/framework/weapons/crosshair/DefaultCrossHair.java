package framework.weapons.crosshair;

import javax.media.opengl.*;

public class DefaultCrossHair extends AbstractCrossHair {
	
	@Override
	public void draw(GL gl) {
		gl.glBegin(GL.GL_LINES);
	        gl.glVertex3f( -0.0004f , 0.0f , 0.0f );
	        gl.glVertex3f( -0.00015f , 0.0f , 0.0f );
	        gl.glVertex3f(  0.00015f , 0.0f , 0.0f );
	        gl.glVertex3f(  0.0004f , 0.0f , 0.0f );
	        gl.glVertex3f( 0.0f , -0.0004f , 0.0f );
	        gl.glVertex3f( 0.0f , -0.00015f , 0.0f );
	        gl.glVertex3f( 0.0f ,  0.00015f , 0.0f );
	        gl.glVertex3f( 0.0f ,  0.0004f , 0.0f );
	    gl.glEnd();
	}
}
