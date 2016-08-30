package client.gui.hud;

import static javax.media.opengl.GL.*;

import java.awt.*;

import javax.media.opengl.*;

import utilities.gl.*;
import client.player.*;
import framework.weapons.*;

public class AmmoDisplay extends HudObject {

	PlayerImpl player;
	
	public AmmoDisplay(PlayerImpl player) {
		this.player = player;
	}
	
	@Override
	public void draw(GL gl) {
		if (player.getWeapon().getAmmoType() != Ammo.NO_AMMO) {
			gl.glDisable(GL_ALPHA_TEST);
			TextHelper.drawText(gl, player.getWeapon().getBulletMagazine() + "/" + player.getBackpack().getAmmoCount(player.getWeapon().getAmmoType()), new Color(1.0f, 1.0f, 0.0f, 0.8f), 650, 25, Font.BOLD, 36);
		    gl.glColor3f(1.0f, 1.0f, 1.0f);
		    gl.glEnable(GL_ALPHA_TEST);
		}
	}
}
