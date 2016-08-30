package client.gui.hud;

import static javax.media.opengl.GL.*;

import java.awt.*;

import javax.media.opengl.*;

import client.*;

import utilities.gl.*;

public class FPSHud extends HudObject {

	private GameController gameController;
	
	public FPSHud(GameController gameController) {
		this.gameController = gameController;
	}
	
	@Override
	public void draw(GL gl) {
		gl.glDisable(GL_ALPHA_TEST);
		TextHelper.drawText(gl, "FPS: " + gameController.getFPS(), new Color(1.0f, 1.0f, 0.0f, 0.8f), 15, 580, Font.PLAIN, 14);
	    gl.glColor3f(1.0f, 1.0f, 1.0f);
	    gl.glEnable(GL_ALPHA_TEST);	
	}
}
