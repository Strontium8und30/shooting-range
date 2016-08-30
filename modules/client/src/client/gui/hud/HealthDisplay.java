package client.gui.hud;

import static javax.media.opengl.GL.*;

import java.awt.*;

import javax.media.opengl.*;

import utilities.gl.*;
import client.player.*;

public class HealthDisplay extends HudObject {
	
	private PlayerImpl player;
	
	public HealthDisplay(PlayerImpl player) {
		this.player = player;
	}
	
	@Override
	public void draw(GL gl) {
		gl.glDisable(GL_ALPHA_TEST);
		TextHelper.drawText(gl, player.getEnergy() + "  " + player.getAmor(), new Color(1.0f, 1.0f, 0.0f, 0.8f), 25, 25, Font.BOLD, 36);
	    gl.glColor3f(1.0f, 1.0f, 1.0f);
	    gl.glEnable(GL_ALPHA_TEST);	
	}

}
