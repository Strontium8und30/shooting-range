package utilities.gl;

import static javax.media.opengl.GL.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;

public class OpenGLPanel extends GLCanvas {
	
	public GL gl;
	
	public GLU glu;
	
	
	public OpenGLPanel() {
//		addGLEventListener(new GLEventListener() {
//
//			@Override
//			public void init(GLAutoDrawable arg) {
//				initOpenGL(arg);
//			}
//
//			@Override
//			public void display(GLAutoDrawable arg) {
//				render(arg);
//			}
//
//			@Override
//			public void displayChanged(GLAutoDrawable arg0, boolean arg1,
//					boolean arg2) {
//				repaint();
//				//displayChanged(arg0, arg1, arg2);
//			}
//
//			@Override
//			public void reshape(GLAutoDrawable arg0, int arg1, int arg2,
//					int arg3, int arg4) {
//				repaint();
//				//reshape(arg0, arg1, arg2, arg3, arg4);
//			}
//		});
		setVisible(true);		
	}

	public void initOpenGL(GLAutoDrawable arg) {
		gl = arg.getGL();
		glu = new GLU();

		gl.glViewport(0, 0, (int) this.getSize().getWidth(), 
							(int) this.getSize().getHeight());
		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(45.0f, this.getSize().getWidth()
								/ this.getSize().getHeight(), 0.01f, 200.0f);
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();

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
	}

	public void render(GLAutoDrawable arg0) {} 
	
	public void displayChanged(GLAutoDrawable arg0, boolean arg1,
			boolean arg2) {}

	public void reshape(GLAutoDrawable arg0, int arg1, int arg2,
			int arg3, int arg4) {}
}
