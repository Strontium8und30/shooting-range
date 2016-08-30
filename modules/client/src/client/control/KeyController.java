package client.control;

import java.awt.event.*;

import common.*;

import client.*;
import framework.*;
import framework.Const;
import framework.types.*;
import framework.weapons.*;

public class KeyController extends KeyAdapter {

	/** Spieler Bewegungskonstanten */
	public final static int MOVE_FORWARD = 0;
	public final static int MOVE_BACKWARD = 1;
	public final static int MOVE_LEFT = 2;
	public final static int MOVE_RIGHT = 3;
	public final static int MOVE_JUMP = 4;
	
	/** Der Spielkontroller */
	GameController gameController = null;
	
	/** Die Kamera */
	Camera camera = null;
	
	/** Eine array welches die aktuellen bewegungen hält*/
	public boolean movements[] = new boolean[5];
	

	public KeyController(GameController gameController) {
		this.gameController = gameController;
		this.camera = gameController.getPlayer().getCamera();
	}
		
	@Override
	public void keyPressed(KeyEvent event) {
		switch(event.getKeyCode()) {
		case 87: //Oben
			move(MOVE_FORWARD, true);
			break;
		case 83: //Unten
			move(MOVE_BACKWARD, true);
			break;
		case 65: //Links
			move(MOVE_LEFT, true);
			break;
		case 68: //Rechts
			move(MOVE_RIGHT, true);
			break;		
		case 32:
			gameController.getPlayer().setJumping(true);
			move(MOVE_JUMP, true);
			break;
		case 'R':
			gameController.getPlayer().reload();
			break;
		case 27: //ESC
			gameController.quit();
			break;
		}
		if (isKeyPressed()) {
			gameController.getPlayer().setMoving(true);
		}
		for (Weapon weapon : gameController.getPlayer().getBackpack().getWeapons()) {
			if (event.getKeyCode() == weapon.getShortcut()) {
				gameController.getPlayer().setWeapon(weapon);
				gameController.getClientModel().sendData(DataType.WEAPON_CHANGE, weapon);
			}
		}
	}	
	
	@Override
	public void keyReleased(KeyEvent event) {
	
		switch(event.getKeyCode()) {
		case 87: //Oben
			move(MOVE_FORWARD, false);
			break;
		case 83: //Unten
			move(MOVE_BACKWARD, false);
			break;
		case 65: //Links
			move(MOVE_LEFT, false);
			break;
		case 68: //Rechts
			move(MOVE_RIGHT, false);
			break;
		case 'E': // Schalter benutzen
			gameController.useSwitchs();
			break;
		}
		if (!isKeyPressed()) {
			gameController.getPlayer().setMoving(false);
		}
	}
	
	public void move(int direction, boolean ismoving){
		movements[direction]=ismoving;	
	}
	
	public boolean isMove(int direction) {
		return movements[direction];
	}
	
	public boolean isKeyPressed() {
		for (boolean move : movements) {
			if (move) return true;
		}
		return false;
	}
	
	public void executeMovements() {
		float ang_h = camera.getAngleHorizontal() * utilities.Const.degToBog;
		Vector3D pos = gameController.getPlayer().getPosition();
		gameController.getPlayer().setLastPosition(pos);
		
		if(movements[MOVE_FORWARD]) {
			pos.x += Math.sin(ang_h) / 12; 
			pos.z -= Math.cos(ang_h) / 12;
		}
		if(movements[MOVE_BACKWARD]) {
			pos.x -= Math.sin(ang_h) / 12; 
			pos.z += Math.cos(ang_h) / 12;
		} 
		if(movements[MOVE_LEFT]) {
			pos.x -= Math.cos(ang_h) / 25; 
			pos.z -= Math.sin(ang_h) / 25; 
		} 	
		if(movements[MOVE_RIGHT]) {
			pos.x += Math.cos(ang_h) / 25; 
			pos.z += Math.sin(ang_h) / 25; 
		} 
		if(movements[MOVE_JUMP]) {
			if (!gameController.getPlayer().isJumping()) {
				movements[MOVE_JUMP] = false;
				if (!isKeyPressed()) {
					gameController.getPlayer().setMoving(false);
				}
			}
			camera.jumpFactor.add(camera.jumpStep);
			pos.add(camera.jumpFactor);
		} 
		pos.add(Const.PHYSICS_GRAVITY);
		gameController.getPlayer().setPosition(pos);
	}
}
