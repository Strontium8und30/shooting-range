package client.gui.hud;

import javax.media.opengl.*;

import client.player.*;

public class CrossHairHud extends HudObject {
	
	PlayerImpl player;
	
	public CrossHairHud(PlayerImpl player) {
		this.player = player;
	}
		
	@Override
	public void draw(GL gl) {		
		gl.glLoadIdentity();
		gl.glDisable(GL.GL_TEXTURE_2D);
		
		gl.glTranslatef(0.0f, 0.0f, -0.02f + player.getWeapon().getCrossHair().getCrossHairSize() / 100);
		gl.glColor3f(player.getWeapon().getCrossHair().getCrossHairSize(), 1-player.getWeapon().getCrossHair().getCrossHairSize(), 0.0f);
		player.getWeapon().getCrossHair().removeCrossHairSize();
		player.getWeapon().getCrossHair().draw(gl);
        
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glColor3f(1.0f, 1.0f, 1.0f);
	}
}
