package maptool.gui.glview;

import static javax.media.opengl.GL.*;

import java.awt.event.*;
import java.util.*;

import javax.media.opengl.*;

import maptool.Const;
import maptool.chapter.Chapter;
import maptool.control.*;
import maptool.gui.glview.forms.*;
import maptool.object.*;
import utilities.log.*;
import utilities.mvc.*;
import framework.*;
import framework.object.interactive.lift.Lift;
import framework.object.interactive.lift.LiftPoint;
import framework.types.*;

public class GLView3D extends GLView {
	
	/** Logging */
	public static Log log = LogFactory.getLog(GLView3D.class);

	
	public GLView3D(Model model) {		
		super(model);
	}
		
	@Override
	public void display(GLAutoDrawable arg0) {
		super.display(arg0);
		Camera cam = Controller.getCamera();		
		Vector3D pos = cam.getPosition();

		gl.glTranslatef(0.0f, 0.0f, getMainModel().getModelObject() == null ? 0.0f : -5.0f);
		
		gl.glRotatef(cam.getAngleVertical(), 1.0f, 0.0f, 0.0f);
		gl.glRotatef(cam.getAngleHorizontal(), 0.0f, 1.0f, 0.0f);
		
		//*********************************
		if (getMainModel().getModelObject() != null) {
			gl.glPushMatrix();
		    gl.glLoadIdentity();
		    gl.glTranslatef(0.0f, 0.0f, getMainModel().getModelObject() == null ? 0.0f : -5.0f);
		    
		    gl.glRotatef(cam.getAngleVertical(), 1.0f, 0.0f, 0.0f);
			gl.glRotatef(cam.getAngleHorizontal(), 0.0f, 1.0f, 0.0f);
			
			showBuildObject();
			
			gl.glPopMatrix();
		}
		//*********************************
		
		gl.glTranslatef(-pos.x, -pos.y, -pos.z);

		gl.glDisable(GL_TEXTURE_2D);
		for(float r = -100; r < 100 ; r++) {
			gl.glColor3fv(r == 0 ? Const.COLOR_NULL_GRID : Const.COLOR_GRID, 0);
			gl.glBegin( GL_LINES );
	           gl.glVertex3f( r * getMainModel().getGridPrecision().getX(), 0 , -100 ); 
	           gl.glVertex3f( r * getMainModel().getGridPrecision().getX(), 0 ,  100 );
	           
	           gl.glVertex3f( -100 , 0 , r * getMainModel().getGridPrecision().getZ());
	           gl.glVertex3f(  100 , 0 , r * getMainModel().getGridPrecision().getZ());
	        gl.glEnd();
		}
		gl.glEnable(GL_TEXTURE_2D);
		drawMap(gl, getMainModel().getMap());
	}
	
	public void showBuildObject() {
		ComplexContainerObject complexObject = getMainModel().getModelObject();
		if(complexObject != null) {
			List<GraphicalObject> complexObjects = complexObject.getAllSubGraphicalObjects();
			for(GraphicalObject sObj : complexObjects) {
				getMainModel().getMap().getTexture(sObj.getTextureID()).bind();
				if(getMainModel().isMoveSelectedObjects()) {
					gl.glColor3fv(Const.COLOR_SELECTED_MOVE, 0);
				} else {
					gl.glColor3fv(Const.COLOR_TEXTURED_VIEW, 0);
				}
				gl.glBegin(GL_TRIANGLES);
					gl.glTexCoord2f(sObj.getTextureVector(0).x, sObj.getTextureVector(0).y);
					gl.glVertex3f(sObj.getVertex(0).x, sObj.getVertex(0).y, sObj.getVertex(0).z);
					gl.glTexCoord2f(sObj.getTextureVector(1).x, sObj.getTextureVector(1).y);
					gl.glVertex3f(sObj.getVertex(1).x, sObj.getVertex(1).y, sObj.getVertex(1).z);
					gl.glTexCoord2f(sObj.getTextureVector(2).x, sObj.getTextureVector(2).y);
					gl.glVertex3f(sObj.getVertex(2).x, sObj.getVertex(2).y, sObj.getVertex(2).z);
				gl.glEnd();	
			}
		}
	}
	
	private void drawMap(GL gl, Chapter map) {
		for(GraphicalObject sObj : map.getMainContainerObject().getAllSubGraphicalObjects()) {
			if(sObj.isVisible()) {
				if(getMainModel().getPerspective() == Const.TEXTURE_FACE ||
				   getMainModel().getPerspective() == Const.TEXTURE_FACE_TRANS) {
					gl.glEnable(GL_TEXTURE_2D);
					if(((ComplexGraphicalObject)sObj).isSelected()) { 
						gl.glColor3fv(Const.COLOR_SELECTED, 0);
					} else {
						gl.glColor3fv(Const.COLOR_TEXTURED_VIEW, 0);
					}					
					getMainModel().getMap().getTexture(sObj.getTextureID()).bind();
					gl.glBegin(GL_TRIANGLES);
						gl.glTexCoord2f(sObj.getTextureVector(0).x, sObj.getTextureVector(0).y);
						gl.glVertex3f(sObj.getVertex(0).x, sObj.getVertex(0).y, sObj.getVertex(0).z);
						gl.glTexCoord2f(sObj.getTextureVector(1).x, sObj.getTextureVector(1).y);
						gl.glVertex3f(sObj.getVertex(1).x, sObj.getVertex(1).y, sObj.getVertex(1).z);
						gl.glTexCoord2f(sObj.getTextureVector(2).x, sObj.getTextureVector(2).y);
						gl.glVertex3f(sObj.getVertex(2).x, sObj.getVertex(2).y, sObj.getVertex(2).z);
					gl.glEnd();	
				} else if(getMainModel().getPerspective() == Const.GRID_FACE) {
					gl.glDisable(GL_TEXTURE_2D);
					if(((ComplexGraphicalObject)sObj).isSelected()) {
						gl.glColor3fv(Const.COLOR_SELECTED, 0);
					} else {
						gl.glColor3fv(Const.COLOR_NOT_TEXTURED_VIEW, 0);
					}
					gl.glBegin(GL_LINE_STRIP);		
						gl.glVertex3f(sObj.getVertex(0).x, sObj.getVertex(0).y, sObj.getVertex(0).z);
						gl.glVertex3f(sObj.getVertex(1).x, sObj.getVertex(1).y, sObj.getVertex(1).z);
						gl.glVertex3f(sObj.getVertex(2).x, sObj.getVertex(2).y, sObj.getVertex(2).z);
						gl.glVertex3f(sObj.getVertex(0).x, sObj.getVertex(0).y, sObj.getVertex(0).z);
					gl.glEnd();	
				} else if (getMainModel().getPerspective() == Const.COLOR_FACE) {
					gl.glDisable(GL_TEXTURE_2D);
					if(((ComplexGraphicalObject)sObj).isSelected()) {
						gl.glColor3fv(Const.COLOR_SELECTED, 0);
					} else {
						gl.glColor3d(Math.random(), Math.random(), Math.random());
					}
					gl.glBegin(GL_TRIANGLES);
						gl.glVertex3f(sObj.getVertex(0).x, sObj.getVertex(0).y, sObj.getVertex(0).z);
						gl.glVertex3f(sObj.getVertex(1).x, sObj.getVertex(1).y, sObj.getVertex(1).z);
						gl.glVertex3f(sObj.getVertex(2).x, sObj.getVertex(2).y, sObj.getVertex(2).z);
					gl.glEnd();
				}
			}
		}
		gl.glDisable(GL_TEXTURE_2D);
		for(Lift lift : map.getLifts()) {
			Vector3D center = lift.getContainerObject().getGeometricCenter();
			new RelativeCrossPath(center, lift.getLiftPoints()).draw(gl);
		}
	}	
		
	@Override
	public void setFielOfViewHorizontal(int horizontal) {
		Vector3D pos = cam.getPosition();
		pos.x = -horizontal;
		cam.setPosition(pos);
	}
	
	@Override
	public void setFielOfViewVertical(int vertical) {
		Vector3D pos = cam.getPosition();
		pos.z = -vertical;
		cam.setPosition(pos);
	}
	
	@Override
	public void keyPressedNotStrg(KeyEvent event) { 
		super.keyPressedNotStrg(event);
		
		//Richtungsveränderung
		float vang = cam.getAngleVertical();
		float hang = cam.getAngleHorizontal();
		
		//Positionsveränderung
		Vector3D pos = cam.getPosition();
		float ang_bog_h = cam.getAngleHorizontal() * Const.DEGREE_TO_RADIAN;
		
		switch(event.getKeyCode()) {
		case 38: //Oben
			if(vang >= -90) vang -= 5;
			break;
		case 40: //Unten
			if(vang <= 90) vang += 5;
			break;
		case 37: //Links
			hang -= 5;
			if(hang < 0) hang = 360.0f;
			break;
		case 39: //Rechts
			hang += 5;
			if(hang >= 360) hang = 0.0f;
			break;

		case 87: //Oben(w)
			if(Math.abs(Math.sin(ang_bog_h)) > Math.abs(Math.cos(ang_bog_h))) {
				pos.x += Math.sin(ang_bog_h) < 0 ? -getMainModel().getGridPrecision().getX() : getMainModel().getGridPrecision().getX();
			} else {
				pos.z -= Math.cos(ang_bog_h) < 0 ? -getMainModel().getGridPrecision().getZ() : getMainModel().getGridPrecision().getZ();
			}
			break;
		case 83: //Unten(s)
			if(Math.abs(Math.sin(ang_bog_h)) > Math.abs(Math.cos(ang_bog_h))) {
				pos.x -= Math.sin(ang_bog_h) < 0 ? -getMainModel().getGridPrecision().getX() : getMainModel().getGridPrecision().getX();
			} else {
				pos.z += Math.cos(ang_bog_h) < 0 ? -getMainModel().getGridPrecision().getZ() : getMainModel().getGridPrecision().getZ();
			}
			break;
		case 65: //Links(a)
			if(Math.abs(Math.cos(ang_bog_h)) > Math.abs(Math.sin(ang_bog_h))) {
				pos.x -= Math.cos(ang_bog_h) < 0 ? -getMainModel().getGridPrecision().getX() : getMainModel().getGridPrecision().getX();
			} else {
				pos.z -= Math.sin(ang_bog_h) < 0 ? -getMainModel().getGridPrecision().getZ() : getMainModel().getGridPrecision().getZ();
			} 
			break;
		case 68: //Rechts(d)
			if(Math.abs(Math.cos(ang_bog_h)) > Math.abs(Math.sin(ang_bog_h))) {
				pos.x += Math.cos(ang_bog_h) < 0 ? -getMainModel().getGridPrecision().getX() : getMainModel().getGridPrecision().getX();
			} else {
				pos.z += Math.sin(ang_bog_h) < 0 ? -getMainModel().getGridPrecision().getZ() : getMainModel().getGridPrecision().getZ();
			}
			break;
		
		case 33: //Bild ab
			pos.y += getMainModel().getGridPrecision().getY();
			break;
		
		case 34: //Bild down
			pos.y -= getMainModel().getGridPrecision().getY();
			break;			
		}
		cam.setPosition(pos);
		cam.setAngleHorizontal(hang);
		cam.setAngleVertical(vang);
	}
}
