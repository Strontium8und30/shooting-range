package maptool.gui.glview.forms;

import static javax.media.opengl.GL.*;

import javax.media.opengl.*;

import maptool.*;
import framework.types.*;

public class Cross {
	
	private Vector3D position;
	
	public Cross(Vector3D position) {
		this.position = position;
	}
	
	public void draw(GL gl) {
		gl.glColor3fv(Const.COLOR_NULL_GRID, 0);
		gl.glBegin(GL_LINES);
           gl.glVertex3f(position.x - 0.5f, position.y, position.z); 
           gl.glVertex3f(position.x + 0.5f, position.y, position.z); 	
           
           gl.glVertex3f(position.x, position.y - 0.5f, position.z); 
           gl.glVertex3f(position.x, position.y + 0.5f, position.z); 
           
           gl.glVertex3f(position.x, position.y, position.z - 0.5f);
           gl.glVertex3f(position.x, position.y, position.z + 0.5f);
        gl.glEnd();
	}
}
