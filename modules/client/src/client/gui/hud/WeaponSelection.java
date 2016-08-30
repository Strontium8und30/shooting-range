package client.gui.hud;

import static javax.media.opengl.GL.*;

import java.awt.*;

import javax.media.opengl.*;

import utilities.gl.*;
import client.player.*;
import framework.weapons.*;

public class WeaponSelection extends HudObject {

	private PlayerImpl player;
	
	private int lastWeaponID;
	
	private long showNewWeapon;
	
	public WeaponSelection(PlayerImpl player) {
		this.player = player;
	}

	@Override
	public void draw(GL gl) {
		if (lastWeaponID != player.getWeapon().ordinal()) {
			lastWeaponID = player.getWeapon().ordinal();
			showNewWeapon = System.currentTimeMillis();
		}
		
		
		gl.glDisable(GL_TEXTURE_2D);
		gl.glDisable(GL_ALPHA_TEST);
		gl.glBlendFunc( GL_SRC_ALPHA, GL_ONE );
		gl.glLoadIdentity();
		gl.glTranslatef( -0.004f , 0.004f , -0.01f );
		for (Weapon weapon : player.getBackpack().getWeapons()) {
			gl.glTranslatef(0.0004f, 0.0f, 0.0f);
			if (weapon == player.getWeapon() && (System.currentTimeMillis() - showNewWeapon) < 5000) {
				gl.glBegin(GL_QUADS);
				gl.glColor4f(1.0f, 1.0f, 0.0f, 0.2f);
		         gl.glVertex3f(-0.00018f,  0.0002f, 0.0f);
		         gl.glVertex3f( 0.00018f,  0.0002f, 0.0f);
		         gl.glVertex3f( 0.00018f, -0.0003f, 0.0f);
		         gl.glVertex3f(-0.00018f, -0.0003f, 0.0f);
		      gl.glEnd();
		      
		      gl.glBegin(GL_QUADS);
		      gl.glColor4f(1.0f, 1.0f, 0.0f, 0.2f);
		         gl.glVertex3f(-0.00108f, -0.0003f, 0.0f);
		         gl.glVertex3f( 0.00108f, -0.0003f, 0.0f);
		         gl.glVertex3f( 0.00108f, -0.0007f, 0.0f);
		         gl.glVertex3f(-0.00108f, -0.0007f, 0.0f);
		      gl.glEnd();
		      
		      gl.glBegin(GL_LINE_STRIP);
		         gl.glColor4f(1.0f, 1.0f, 0.0f, 1.0f);
		         gl.glVertex3f(-0.00018f,  0.0002f, 0.0f);
		         gl.glVertex3f( 0.00018f,  0.0002f, 0.0f);
		         gl.glVertex3f( 0.00018f, -0.0003f, 0.0f);
	
		         gl.glVertex3f( 0.00108f, -0.0003f, 0.0f);
		         gl.glVertex3f( 0.00108f, -0.0007f, 0.0f);
		         gl.glVertex3f(-0.00108f, -0.0007f, 0.0f);
		         gl.glVertex3f(-0.00108f, -0.0003f, 0.0f);
		         
		         gl.glVertex3f(-0.00018f, -0.0003f, 0.0f);
		         gl.glVertex3f(-0.00018f,  0.0002f, 0.0f);
		      gl.glEnd();
		      
		      TextHelper.drawText(gl, String.valueOf(weapon.getShortcut()), new Color(1.0f, 0.5f, 0.0f, 1.0f), 195 + player.getBackpack().getWeapons().indexOf(weapon) * 23, 581, Font.PLAIN, 24);
		      TextHelper.drawText(gl, weapon.getName(), new Color(1.0f, 0.5f, 0.0f, 1.0f), 144 + player.getBackpack().getWeapons().indexOf(weapon) * 23, 547, Font.PLAIN, 24);
			} else {
				gl.glBegin(GL_QUADS);
					if (weapon == player.getWeapon()) {
						gl.glColor4f(1.0f, 1.0f, 0.0f, 0.2f);
					} else {
						gl.glColor4f(1.0f, 0.5f, 0.0f, 0.2f);
					}
					gl.glVertex3f(-0.00018f, 0.0002f, 0.0f);
					gl.glVertex3f(0.00018f, 0.0002f, 0.0f);
					gl.glVertex3f(0.00018f, -0.0002f, 0.0f);
					gl.glVertex3f(-0.00018f, -0.0002f, 0.0f);
				gl.glEnd();
	
				gl.glBegin(GL_LINE_STRIP);
					if (weapon == player.getWeapon()) {
						gl.glColor4f(1.0f, 1.0f, 0.0f, 1.0f);
					} else {
						gl.glColor4f(1.0f, 0.5f, 0.0f, 1.0f);
					}				
					gl.glVertex3f(-0.00018f, 0.0002f, 0.0f);
					gl.glVertex3f(0.00018f, 0.0002f, 0.0f);
					gl.glVertex3f(0.00018f, -0.0002f, 0.0f);
					gl.glVertex3f(-0.00018f, -0.0002f, 0.0f);
					gl.glVertex3f(-0.00018f, 0.0002f, 0.0f);
				gl.glEnd();
	
				TextHelper.drawText(gl, String.valueOf(weapon.getShortcut()), new Color(1.0f, 0.5f, 0.0f, 1.0f), 195 + player.getBackpack().getWeapons().indexOf(weapon) * 23, 581, Font.PLAIN, 24);
			}
		}
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		
		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_DST_ALPHA);
		gl.glEnable(GL_ALPHA_TEST);
		gl.glEnable(GL_TEXTURE_2D);
	}
};
