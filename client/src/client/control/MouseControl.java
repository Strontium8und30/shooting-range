package client.control;

import java.awt.event.*;

import utilities.*;
import utilities.control.*;
import utilities.log.*;
import client.*;
import client.Const;
import client.player.*;
import framework.*;
import framework.types.*;

public class MouseControl extends MouseAdapter implements MouseMotionListener {
	
	/** Logging */
	public static Log log = LogFactory.getLog(MouseControl.class);
	
	private static MouseControl instance; 
	
	/** Die Position an der die Maus versteckt wird */
	private Vector2D HideMousePos = new Vector2D(Utilities.getScreenCenter());
	
	/** Die Kamera */
	private Camera camera = null;
	
	/** Der Spieler */
	private PlayerImpl player = null;
	
	/** Winkel horizontal */
	private float ang_h = 0;
	
	/** Winkel vertikal */
	private float ang_v = 0;
	
	private MouseControl(GameController gameController) {
		this.player = gameController.getPlayer();
		this.camera = player.getCamera();
		this.ang_h = camera.getAngleHorizontal();
		this.ang_v = camera.getAngleVertical();
	}
	
	public static MouseControl getInstance(GameController gameController) {
		return instance == null ? instance = new MouseControl(gameController) : instance;
	}

	@Override
	public void mousePressed(MouseEvent event) {
		if (event.getButton() == MouseEvent.BUTTON1) {
			player.setShooting(true);
		}
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if (event.getButton() == MouseEvent.BUTTON1) {
			player.setShooting(false);
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		int mx = e.getXOnScreen(); 
		int my = e.getYOnScreen();

		ang_h = camera.getAngleHorizontal() - (float)(HideMousePos.x - mx) * Const.MOUSE_SENSETIV;
		if(ang_h <   0) ang_h += 360;
		if(ang_h > 360) ang_h -= 360;
		camera.setAngleHorizontal(ang_h);

		ang_v = camera.getAngleVertical() - (float)(HideMousePos.y - my) * Const.MOUSE_SENSETIV;
		if(ang_v < -90) ang_v = -90;
		if(ang_v >  90) ang_v =  90;
		camera.setAngleVertical(ang_v);
			
		Mouse.setPosition(HideMousePos.x, HideMousePos.y);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}
}
