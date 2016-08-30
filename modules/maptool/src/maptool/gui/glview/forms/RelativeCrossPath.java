package maptool.gui.glview.forms;

import static javax.media.opengl.GL.*;

import java.util.*;

import javax.media.opengl.*;

import maptool.*;
import framework.types.*;

public class RelativeCrossPath {

	private Vector3D origin;
	
	private List<? extends Vector3D> vectors;
	
	public RelativeCrossPath(Vector3D origin, List<? extends Vector3D> vectors) {
		this.origin = origin;
		this.vectors = vectors;
	}
	
	public void draw(GL gl) {
		for (Vector3D v : vectors) {
			new Cross(Vector3D.add(origin, v)).draw(gl);
		}
		gl.glColor3fv(Const.COLOR_NULL_GRID, 0);
		gl.glBegin(GL_LINE_STRIP);		
		for (Vector3D v : vectors) {
			gl.glVertex3f(v.x + origin.x, v.y  + origin.y, v.z  + origin.z);
		}
		gl.glEnd();	
	}
}
