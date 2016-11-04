package maptool.gui.glview;

import static javax.media.opengl.GL.*;

import java.awt.event.*;
import java.util.*;

import javax.media.opengl.*;

import maptool.Const;
import maptool.control.*;
import maptool.gui.mainFrame.*;
import maptool.object.*;
import utilities.log.*;
import utilities.mvc.*;
import framework.*;
import framework.types.*;

public class GLViewSide extends GLView {
	
	/** Logging */
	public static Log log = LogFactory.getLog(GLViewSide.class);
	
	public GLViewSide(Model model) {
		super(model);
	}
	
	public void setFieldOfView() {
		Controller.getCamera().setAngleHorizontal(270);
		Controller.getCamera().setAngleVertical(0);
	}
	
	@Override
	public void display(GLAutoDrawable arg0) {
		super.display(arg0);
		
		Camera cam = Controller.getCamera();
				
		gl.glRotatef(270.0f, 0.0f, 1.0f, 0.0f);
		
		Vector3D pos = cam.getPosition();
		
		//*********************************
		if (getMainModel().getModelObject() != null) {
			gl.glPushMatrix();
		    gl.glLoadIdentity();
		    gl.glRotatef(270.0f, 0.0f, 1.0f, 0.0f);
		    gl.glTranslatef(getMainModel().getModelObject() == null ? 0.0f : -15.0f, 0.0f, 0.0f);
			
			showBuildObject();
			
			gl.glPopMatrix();
		}
		//*********************************
		
		gl.glTranslatef(-15.0f -pos.x / 100, -pos.y, -pos.z);
		
		gl.glDisable(GL_TEXTURE_2D);
		for(float r = -100; r < 100 ; r += getMainModel().getGridPrecision().getX()) {	
			gl.glColor3fv(r == 0 ? Const.COLOR_NULL_GRID: Const.COLOR_GRID, 0);
			gl.glBegin( GL_LINES );
	           gl.glVertex3f(-0.01f, r, -100.0f); 
	           gl.glVertex3f(-0.01f, r,  100.0f);
	           
	           gl.glVertex3f(-0.01f, -100.0f , r);
	           gl.glVertex3f(-0.01f,  100.0f , r);
	        gl.glEnd();
		}
		gl.glEnable(GL_TEXTURE_2D);
		
		getMapDraw(gl, ((MainModel)getModel()).getMap().getMainContainerObject());
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
					gl.glVertex3f(sObj.getVertex(0).x / 100, sObj.getVertex(0).y, sObj.getVertex(0).z);
					gl.glTexCoord2f(sObj.getTextureVector(1).x, sObj.getTextureVector(1).y);
					gl.glVertex3f(sObj.getVertex(0).x / 100, sObj.getVertex(1).y, sObj.getVertex(1).z);
					gl.glTexCoord2f(sObj.getTextureVector(2).x, sObj.getTextureVector(2).y);
					gl.glVertex3f(sObj.getVertex(0).x / 100, sObj.getVertex(2).y, sObj.getVertex(2).z);
				gl.glEnd();	
			}
		}
	}
	
	public void getMapDraw(GL gl, ComplexContainerObject complexObject) {
		for(ContainerObject cObj : complexObject.getContainerObjectList()) {
			getMapDraw(gl, (ComplexContainerObject)cObj);
		}
		List<GraphicalObject> sObjs = complexObject.getGraphicalObjectList();
		for(GraphicalObject sObj : sObjs) {
			if(complexObject.isVisible()) {
				if(((MainModel)getModel()).getPerspective() == Const.TEXTURE_FACE) {
					if(((ComplexGraphicalObject)sObj).isSelected()) {
						gl.glColor3fv(Const.COLOR_SELECTED, 0);
					} else {
						gl.glColor3fv(Const.COLOR_TEXTURED_VIEW, 0);
					}
					getMainModel().getMap().getTexture(sObj.getTextureID()).bind();
					gl.glBegin(GL_TRIANGLES);
						gl.glTexCoord2f(sObj.getTextureVector(0).x, sObj.getTextureVector(0).y);
						gl.glVertex3f(sObj.getVertex(0).x / 100, sObj.getVertex(0).y, sObj.getVertex(0).z);
						gl.glTexCoord2f(sObj.getTextureVector(1).x, sObj.getTextureVector(1).y);
						gl.glVertex3f(sObj.getVertex(1).x / 100, sObj.getVertex(1).y, sObj.getVertex(1).z);
						gl.glTexCoord2f(sObj.getTextureVector(2).x, sObj.getTextureVector(2).y);
						gl.glVertex3f(sObj.getVertex(2).x / 100, sObj.getVertex(2).y, sObj.getVertex(2).z);
					gl.glEnd();	
				} else if(((MainModel)getModel()).getPerspective() == Const.GRID_FACE) {					
					if(complexObject.isSelected()) {
						gl.glBegin(GL_LINE_STRIP);
							gl.glColor3f(1.0f, 1.0f, 0.0f);
							gl.glVertex3f(0.0f, sObj.getVertex(0).y, sObj.getVertex(0).z);
							gl.glColor3f(1.0f, 1.0f, 0.0f);
							gl.glVertex3f(0.0f, sObj.getVertex(1).y, sObj.getVertex(1).z);
							gl.glColor3f(1.0f, 1.0f, 0.0f);
							gl.glVertex3f(0.0f, sObj.getVertex(2).y, sObj.getVertex(2).z);
							gl.glColor3f(1.0f, 1.0f, 0.0f);
							gl.glVertex3f(0.0f, sObj.getVertex(0).y, sObj.getVertex(0).z);
						gl.glEnd();	
					} else {
						gl.glBegin(GL_LINE_STRIP);
							gl.glColor3f(0.0f, 1.0f, 0.0f);
							gl.glVertex3f(0.0f, sObj.getVertex(0).y, sObj.getVertex(0).z);
							gl.glColor3f(0.0f, 1.0f, 0.0f);
							gl.glVertex3f(0.0f, sObj.getVertex(1).y, sObj.getVertex(1).z);
							gl.glColor3f(0.0f, 1.0f, 0.0f);
							gl.glVertex3f(0.0f, sObj.getVertex(2).y, sObj.getVertex(2).z);
							gl.glColor3f(0.0f, 1.0f, 0.0f);
							gl.glVertex3f(0.0f, sObj.getVertex(0).y, sObj.getVertex(0).z);
						gl.glEnd();	
					}
				}
			}
		}
	}
			
	@Override
	public void keyPressedNotStrg(KeyEvent event) { 
		super.keyPressedNotStrg(event);
		
		//Positionsveränderung
		Vector3D pos = cam.getPosition();

		switch(event.getKeyCode()) {
		case 87: //Oben(w)
			pos.y += Const.GRID_PRECISION;
			break;
		case 83: //Unten(s)
			pos.y -= Const.GRID_PRECISION;
			break;
		case 65: //Links(a)
			pos.z += Const.GRID_PRECISION; 
			break;
		case 68: //Rechts(d)
			pos.z -= Const.GRID_PRECISION; 
			break;
		case 33: //Bild ab
			pos.x -= Const.GRID_PRECISION;
			break;
		case 34: //Bild down
			pos.x += Const.GRID_PRECISION;
			break;		
		}
		cam.setPosition(pos);
	}
	
	public void setFielOfViewHorizontal(int horizontal) {
		Vector3D pos = cam.getPosition();
		pos.z = horizontal;
		cam.setPosition(pos);
	}
	
	public void setFielOfViewVertical(int vertical) {
		Vector3D pos = cam.getPosition();
		pos.y = vertical;
		cam.setPosition(pos);
	}
}
