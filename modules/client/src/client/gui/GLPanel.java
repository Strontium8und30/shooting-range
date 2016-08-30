package client.gui;

import static javax.media.opengl.GL.*;

import java.awt.event.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;

import utilities.log.*;
import client.*;
import client.control.*;
import client.gui.hud.*;
import client.player.*;

import common.*;

import framework.*;
import framework.object.dynamic.*;
import framework.types.*;

public class GLPanel implements GLEventListener {
	
	/** Logging */
	public static Log log = LogFactory.getLog(GLPanel.class);
	
	GL gl = null;
	
	GLU glu = null;
	
	public GLCanvas canvas = null;
	
	HeadUpDisplay headUpDisplay = null;
	
	Camera camera = null;
	
	GameController gameController = null;
	
	
	public GLPanel(GameController gameController) {
		this.gameController = gameController;
		canvas = new GLCanvas();
		camera = gameController.getPlayer().getCamera();
		
		canvas.addGLEventListener(this);
		canvas.addMouseMotionListener(MouseControl.getInstance(gameController));
		canvas.addMouseListener(MouseControl.getInstance(gameController));
		canvas.addKeyListener(gameController.getKeyController());
		
		canvas.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				GLPanel.this.gameController.maxi();
			}

			@Override
			public void focusLost(FocusEvent e) {
				GLPanel.this.gameController.mini();
			}			
		});
		gameController.startAllThreads();		
	}	

	@Override
	public void init(GLAutoDrawable arg) {
		log.info("GL Init");
		if (arg != null) {
			gl = arg.getGL();
			glu = new GLU();
		}
		
		gl.glEnable(GL_TEXTURE_2D);
		gl.glShadeModel(GL_SMOOTH);
		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepth(1.0f);
		
		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);
		
		gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		
		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_DST_ALPHA);
		gl.glEnable(GL_BLEND);
		gl.glAlphaFunc(GL_GREATER, 0.9f);
		gl.glEnable(GL_ALPHA_TEST);


		float fogColor[] = {0.0f, 0.0f, 0.0f, 0.0f};
		gl.glFogfv(GL_FOG_COLOR, fogColor, 0);   
	    gl.glFogi(GL_FOG_MODE, GL_LINEAR); 
	    gl.glFogf(GL_FOG_DENSITY, 0.5f);	
	    gl.glFogf(GL_FOG_START, 5); 
	    gl.glFogf(GL_FOG_END, 10); 
	    gl.glEnable(GL_FOG);
		
//		try {
//			new Shader(gl, glu).getShader("vertexshader.glsl", "fragmentshader.glsl");
//		} catch (GLException e) {
//			log.warning("Shader not Suported");
//		}
		gameController.getMap().fetchTextures();
		initHeadUpDisplay();
		canvas.requestFocus();
	}
	
	private void initHeadUpDisplay() {
		headUpDisplay = new HeadUpDisplay(gl);
		headUpDisplay.addHudObject(new CrossHairHud(gameController.getPlayer()));
		headUpDisplay.addHudObject(new HealthDisplay(gameController.getPlayer()));
		headUpDisplay.addHudObject(new AmmoDisplay(gameController.getPlayer()));
		headUpDisplay.addHudObject(new WeaponSelection(gameController.getPlayer()));
		headUpDisplay.addHudObject(new FPSHud(gameController));
	}

	@Override
	public void display(GLAutoDrawable arg0) {
		if (!gameController.getMap().getTextureHelper().isFetched()) {
			gameController.getMap().getTextureHelper().fetchTextures();
		}
		
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
						
		gl.glRotatef(camera.getAngleVertical(), 1.0f, 0.0f, 0.0f);
		gl.glRotatef(camera.getAngleHorizontal(), 0.0f, 1.0f, 0.0f);
		
		Vector3D pos = gameController.getPlayer().getAnimatedPosition();
		gl.glTranslatef(-pos.x, -pos.y, -pos.z);
		drawContainerObject(gl, gameController.getMap().getMainContainerObject());
		
		for (NetClient client : gameController.getClientModel().getClients().values()) {	
			if (client.getPlayerModel() == null) {
				ContainerObject cObj = new SimpleContainerObject(gameController.getPlayer().getPlayerModel()).transfareToZero();
				cObj.setVisible(true);
				cObj.setShootable(false);
				client.setPlayerModel((SimpleContainerObject)cObj);
			} 			
			drawContainerObject(gl, new SimpleContainerObject(client.getPlayerModel()).transfare(client.getPosition()));
		}
		
		headUpDisplay.draw();
	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
		canvas.repaint();
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2,
			int arg3, int arg4) {

		int viewWidth = canvas.getWidth();
		int viewHeight = canvas.getHeight();
		float viewProportion = (float)viewWidth / (float)viewHeight;
		
		gl.glViewport(0, 0, viewWidth, viewHeight);
		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		
		if(viewProportion > 1.24f && viewProportion < 1.34f) {
			glu.gluPerspective(52.75f, viewProportion, 0.01f, 100.0f);
			log.info("Es wurde ein Bildschirmverhältnis von 4:3 (" + viewWidth + ":" + viewHeight + ") erkannt. " + viewProportion);
		} else {
			glu.gluPerspective(45.0f, viewProportion, 0.01f, 100.0f);
			log.info("Es wurde ein Bildschirmverhältnis von 16:9 (" + viewWidth + ":" + viewHeight + ") erkannt. " + viewProportion);
		}
		
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();
	}			
	
	public void drawContainerObject(GL gl, ContainerObject complexObject) {
		if (!complexObject.isVisible()) return;
		int cObjectCount = complexObject.getContainerObjectCount();
		for(int cObjIndex = 0; cObjIndex < cObjectCount; cObjIndex++) {
			drawContainerObject(gl, complexObject.getContainerObject(cObjIndex));
		}
		
		for (PortalInf portal : gameController.getMap().getPortals()) {
			if (portal.getPortalA() == complexObject || portal.getPortalB() == complexObject) {
				if (portal.isOpen()) {
					gl.glColor3f(0.5f, 1.0f, 0.5f);
				} else {
					gl.glColor3f(1.0f, 0.5f, 0.5f);
				}
				break;
			} else {
				gl.glColor3f(1.0f, 1.0f, 1.0f);
			}
		}
		
		/** Dynamische Objekte farbig makieren wenn sie aktive sind */
		if(complexObject instanceof DynamicObject) {
			if (((DynamicObject) complexObject).isActive()) {
				gl.glColor3f(1.0f, 0.2f, 0.5f);
			} else {
				gl.glColor3f(1.0f, 1.0f, 1.0f);
			}
		}
		
		int gObjectCount = complexObject.getGraphicalObjectCount();
		for(int gObjIndex = 0; gObjIndex < gObjectCount; gObjIndex++) {
			GraphicalObject sObj = complexObject.getGraphicalObject(gObjIndex);			
			if(sObj.isVisible()) {
				gameController.getMap().getTexture(sObj.getTextureID()).bind();
				gl.glBegin(GL_TRIANGLES);
					gl.glNormal3f(sObj.getNormalVector().getX(), sObj.getNormalVector().getY(), sObj.getNormalVector().getZ());
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
	
	public GLCanvas getGLCanvas() {
		return canvas;
	}
	
	public void repaint() {
		canvas.repaint();
	}
}
