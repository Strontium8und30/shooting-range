package maptool.gui.glview;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.VERTICAL;
import static javax.media.opengl.GL.GL_ALPHA_TEST;
import static javax.media.opengl.GL.GL_BLEND;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_GREATER;
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_MODELVIEW;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL.GL_ONE_MINUS_DST_ALPHA;
import static javax.media.opengl.GL.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.media.opengl.GL.GL_PROJECTION;
import static javax.media.opengl.GL.GL_SMOOTH;
import static javax.media.opengl.GL.GL_SRC_ALPHA;
import static javax.media.opengl.GL.GL_TEXTURE_2D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.border.LineBorder;

import maptool.control.Controller;
import maptool.gui.mainFrame.*;
import maptool.object.ComplexContainerObject;
import utilities.log.Log;
import utilities.log.LogFactory;
import utilities.mvc.DefaultView;
import utilities.mvc.EventAction;
import utilities.mvc.Model;
import framework.Camera;

public abstract class GLView extends DefaultView implements GLEventListener {

	/** Logging */
	public static Log log = LogFactory.getLog(GLView.class);
	
	/** Das Panel */
	private JPanel glViewPanel = null;
	
	/** Horizontale Scrollbar */
	private JScrollBar scrollBarHorizontal;
	
	/** Vertikale Scrollbar */
	private JScrollBar scrollBarVertical;
	
	/** Das GLPanel */
	private GLCanvas glPanel = null;
	
	protected GL gl = null;
	
	protected GLU glu = null;
	
	/** Die Kamera */
	protected Camera cam = Controller.getCamera();
	
	/** Ist STRG gedrückt */
	private static boolean strgPressed;
	
	public GLView(Model model) {
		super(model);
		
		glViewPanel = new JPanel();
		
		glPanel = new GLCanvas();
		
		glViewPanel.setLayout(new GridBagLayout());
		
		glPanel.addGLEventListener(this);
		glPanel.addKeyListener(new KeyControl());
		glPanel.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
				glViewPanel.setBorder(new LineBorder(Color.GREEN));
			}

			@Override
			public void focusLost(FocusEvent e) {
				glViewPanel.setBorder(new LineBorder(Color.GRAY));
			}			
		});
		
		scrollBarHorizontal = new JScrollBar();
		scrollBarHorizontal.setMinimum(-50);
		scrollBarHorizontal.setMaximum(60);
		scrollBarHorizontal.setOrientation(JScrollBar.HORIZONTAL);
		
		scrollBarVertical = new JScrollBar();
		scrollBarVertical.setMinimum(-50);
		scrollBarVertical.setMaximum(60);
		scrollBarVertical.setOrientation(JScrollBar.VERTICAL);
		
		scrollBarHorizontal.addAdjustmentListener(new AdjustmentListener(){
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				setFielOfViewHorizontal(e.getValue());
				glPanel.repaint();
				getMainModel().notifyViews(MainModel.GLVIEW_REPAINT, null, GLView.this);
			}
		});
		
		scrollBarVertical.addAdjustmentListener(new AdjustmentListener(){
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				setFielOfViewVertical(e.getValue());
				glPanel.repaint();
				getMainModel().notifyViews(MainModel.GLVIEW_REPAINT, null, GLView.this);
			}
		});	
		
		glViewPanel.add(glPanel,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, CENTER, BOTH, new Insets(1, 1, 0, 0), 0, 0));
		glViewPanel.add(scrollBarHorizontal,
				new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, CENTER, HORIZONTAL, new Insets(0, 1, 1, 0), 0, 0));
		glViewPanel.add(scrollBarVertical,
				new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0, CENTER, VERTICAL, new Insets(1, 0, 0, 1), 0, 0));
	}	
		
	public void setFieldOfView() {}
	
	public abstract void setFielOfViewVertical(int value);
	
	public abstract void setFielOfViewHorizontal(int value);
	
	public MainModel getMainModel() {
		return (MainModel)getModel();
	}
	
	@Override
	public JPanel getComponent() {
		return this.glViewPanel;
	}
	
	public GLCanvas getGLComponent() {
		return this.glPanel;
	}
	
	@Override
	public void update(EventAction event, Object arg) {
		if(event.equals(MainModel.GLVIEW_REPAINT)) {
			glPanel.repaint();
		} else if(event.equals(MainModel.GLVIEW_CHANGED)) {
			setFieldOfView();
		} 
	}
	
	@Override
	public void init(GLAutoDrawable arg) {
		gl = arg.getGL();
		glu = new GLU();

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

		getComponent().requestFocus();
		getMainModel().getMap().fetchTextures();
	}
	
	@Override
	public void display(GLAutoDrawable arg0) {
		if (!getMainModel().getMap().isFetched()) {
			getMainModel().getMap().fetchTextures();
		}
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
		glPanel.repaint();
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		gl.glViewport(0, 0, (int) glPanel.getSize().getWidth(), 
				(int) glPanel.getSize().getHeight());
		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(45.0f, glPanel.getSize().getWidth()
							/ glPanel.getSize().getHeight(), 0.01f, 200.0f);
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();
		glPanel.setMinimumSize(new Dimension(0,0));
		glPanel.repaint();
	}	
	
	public class KeyControl implements KeyListener {
		
		@Override
		public void keyPressed(KeyEvent event) {
			if(event.getKeyCode() == 17) {
				strgPressed = true;
			}
			GLView.this.keyPressed(event);
			glPanel.repaint();
			getMainModel().notifyViews(MainModel.GLVIEW_REPAINT, null, GLView.this);
		}

		@Override
		public void keyReleased(KeyEvent event) {
			if(event.getKeyCode() == 17) {
				strgPressed = false;
			}
			GLView.this.keyReleased(event);
			glPanel.repaint();
		}

		@Override
		public void keyTyped(KeyEvent event) {
			GLView.this.keyTyped(event);
			glPanel.repaint();
		}		
	}
	
	public void keyPressed(KeyEvent event) {
		if(isStrgPressed()) {
			keyPressedAndStrg(event);
		} else {
			keyPressedNotStrg(event);
		}
	}
	
	public void keyPressedAndStrg(KeyEvent event) {
		switch(event.getKeyCode()) {
		case 38: //Oben
			getMainModel().getModelObject().rotate(5.0f, 0.0f, 0.0f);
			break;
		case 40: //Unten
			getMainModel().getModelObject().rotate(-5.0f, 0.0f, 0.0f);
			break;
		case 37: //Links
			getMainModel().getModelObject().rotate(0.0f, 5.0f, 0.0f);
			break;
		case 39: //Rechts
			getMainModel().getModelObject().rotate(0.0f, -5.0f, 0.0f);
			break;
		case '-': //Links
			getMainModel().getModelObject().rotate(0.0f, 0.0f, 5.0f);
			break;
		case '+': //Rechts
			getMainModel().getModelObject().rotate(0.0f, 0.0f, -5.0f);
			break;
		}
	}
	
	public void keyPressedNotStrg(KeyEvent event) {
		switch(event.getKeyCode()) {
		case 109: //-
			getMainModel().getModelObject().resizeModel(-1);
			break;		
		case 107: //+
			getMainModel().getModelObject().resizeModel(1);
			break;		
		case KeyEvent.VK_ENTER:
			getMainModel().placeModelObject();
			break;			
		case 27: //ESC
			if(getMainModel().isMoveSelectedObjects()) {
				getMainModel().setMoveSelectedObjects(false);
				getMainModel().setModelObject(null);
				ComplexContainerObject selectedObject = (ComplexContainerObject)getMainModel().getSelectedObjects().get(0).getUserObject();
				selectedObject.setVisible(true);
			}
			getMainModel().setModelObject(null);
			break;	
		}
	}
	
	public void keyReleased(KeyEvent event) {}

	public void keyTyped(KeyEvent event) {}
	
	public boolean isStrgPressed() {
		return strgPressed;
	}
}
