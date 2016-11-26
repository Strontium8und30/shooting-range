package utilities.control;

import java.awt.*;
import java.awt.image.*;

import utilities.log.Log;
import utilities.log.LogFactory;


public class Mouse {

	public static Log log = LogFactory.getLog(Mouse.class);
	
	public static void setPosition(int x, int y) {
		try {
			new Robot().mouseMove(x, y);
		} catch (AWTException e) {
			log.error("Fehler beim setzten der Position des Mauscursors", e);
		}
	}
	
	public static void setPosition(float x, float y) {
		setPosition((int)x, (int)y);
	}
	
	public static void setPosition(Dimension position) {
		setPosition((int)position.getWidth(), (int)position.getHeight());
	}
	
	public static Cursor getInvisibleCursor() {
		BufferedImage image = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
		image.getAlphaRaster().setPixel(0, 0, new double []{0,0,0,0});
		return Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(), "invisible"); 
	}
}
