package utilities.gl;

import java.awt.*;

import javax.media.opengl.*;

import com.sun.opengl.util.j2d.TextRenderer;

public class TextHelper {

	public static void drawText(GL gl, String text, Color color, int x, int y, int type, int size){
		TextRenderer renderer = new TextRenderer(new Font("Courier New", type, size));
    	renderer.beginRendering(800, 600);
		renderer.setColor(color);
		renderer.draw(text, x, y);
		renderer.endRendering();
		renderer.dispose();
	}
}
