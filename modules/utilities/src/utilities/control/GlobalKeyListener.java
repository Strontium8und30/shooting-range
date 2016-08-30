package utilities.control;

import java.awt.*;
import java.awt.event.*;

public abstract class GlobalKeyListener implements AWTEventListener {

	@Override
	public void eventDispatched(AWTEvent event) {
		if(event.getID() == KeyEvent.KEY_PRESSED) {
			keyPressed((KeyEvent)event);  					
		}
		if(event.getID() == KeyEvent.KEY_RELEASED) {
			keyReleased((KeyEvent)event);  					
		}
	}

	
	public abstract void keyPressed(KeyEvent event); 
	
	public abstract void keyReleased(KeyEvent event); 
}
