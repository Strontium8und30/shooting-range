package client.control;

import java.awt.*;
import java.awt.event.*;

import client.*;
import client.Const;
import client.player.*;
import framework.*;
import framework.types.*;
import utilities.control.*;
import utilities.log.*;

public class MouseControl extends MouseAdapter implements MouseMotionListener {
	
	/** Logging */
	public static Log log = LogFactory.getLog(MouseControl.class);
		
	/** Die Position an der die Maus versteckt wird */
	private Vector2D hideMousePos;
	
	private GameController gameController;
	
	/** Die Kamera */
	private Camera camera = null;
	
	/** Der Spieler */
	private PlayerImpl player = null;
	
	/** Winkel horizontal */
	private float ang_h = 0;
	
	/** Winkel vertikal */
	private float ang_v = 0;
	
	
	public MouseControl(GameController gameController) {		
		this.gameController = gameController;
		this.player = gameController.getPlayer();
		this.camera = player.getCamera();
		this.ang_h = camera.getAngleHorizontal();
		this.ang_v = camera.getAngleVertical();
		this.hideMousePos = getWindowCenter();		
	}
	
	private Vector2D getWindowCenter() {
		Component component = gameController.getGameView().getComponent();
		return new Vector2D(component.getLocation()).add(new Vector2D(component.getSize()).multiplyBy(0.5f));
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
		if (gameController.isRunning()) {
			int mx = e.getXOnScreen(); 
			int my = e.getYOnScreen();

			ang_h = camera.getAngleHorizontal() - (float)(hideMousePos.x - mx) * Const.MOUSE_SENSETIV;
			if(ang_h <   0) ang_h += 360;
			if(ang_h > 360) ang_h -= 360;
			camera.setAngleHorizontal(ang_h);

			ang_v = camera.getAngleVertical() - (float)(hideMousePos.y - my) * Const.MOUSE_SENSETIV;
			if(ang_v < -90) ang_v = -90;
			if(ang_v >  90) ang_v =  90;
			camera.setAngleVertical(ang_v);
				
			Mouse.setPosition(hideMousePos.x, hideMousePos.y);
		}		
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}
}
