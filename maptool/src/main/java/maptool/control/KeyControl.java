package maptool.control;

import java.awt.event.KeyEvent;

import utilities.control.GlobalKeyListener;

import framework.Camera;
import framework.types.Vector3D;

public class KeyControl extends GlobalKeyListener {
	
	float degToBog = (float)(Math.PI/180);
	Camera cam = Controller.getCamera();
	
	@Override
	public void keyPressed(KeyEvent event) {
				
		//Richtungsveränderung
		float vang = cam.getAngleVertical();
		float hang = cam.getAngleHorizontal();
		
		//Positionsveränderung
		Vector3D pos = cam.getPosition();
		float ang_bog_h = cam.getAngleHorizontal() * degToBog;

		switch(event.getKeyCode()) {
		
		case 38: //Oben
			if(vang >= -90) vang -= 5;
			break;
		case 40: //Unten
			if(vang <= 90) vang += 5;
			break;
		case 37: //Links
			hang -= 5;
			if(hang < 0) hang = 360.0f;
			break;
		case 39: //Rechts
			hang += 5;
			if(hang >= 360) hang = 0.0f;
			break;

		case 87: //Oben(w)
			pos.x -= Math.sin(ang_bog_h); 
			pos.z += Math.cos(ang_bog_h); 
			break;
		case 83: //Unten(s)
			pos.x += Math.sin(ang_bog_h); 
			pos.z -= Math.cos(ang_bog_h);
			break;
		case 65: //Links(a)
			pos.x += Math.cos(ang_bog_h); 
			pos.z += Math.sin(ang_bog_h); 
			break;
		case 68: //Rechts(d)
			pos.x -= Math.cos(ang_bog_h); 
			pos.z -= Math.sin(ang_bog_h); 
			break;
		case 33: //Bild ab
			pos.y -= 0.5;
			break;
		case 34: //Bild down
			pos.y += 0.5;
			break;
							
		case 27: //ESC
			System.exit(0);
			break;			
		}
		
		cam.setPosition(pos);
		cam.setAngleHorizontal(hang);
		cam.setAngleVertical(vang);
	}

	@Override
	public void keyReleased(KeyEvent event) {
		
	}
}
