package client.gui;

import java.awt.*;

import javax.swing.*;

import utilities.log.*;
import utilities.mvc.*;

public abstract class WindowView extends DefaultView {
	
	public static Log log = LogFactory.getLog(WindowView.class);
	
	JFrame frame;
	
	public WindowView(Model model) {
		this(model, new Point(0,0));
	}
	
	public WindowView(Model model, Point location) {
		super(model);
		
		frame = new JFrame("Spiel");
		frame.setLocation(location);
	}
	
	public void setFullscreen() {		
		dispose();
		
		frame.setUndecorated(true);
		frame.setResizable(false);
		frame.setSize(getScreenSize());
		
		frame.setVisible(true);
	}
		
	public void setWindow() {
		dispose();
		
		frame.setUndecorated(false);
		frame.setResizable(false);
		frame.setSize(1024, 768);
		
		frame.setVisible(true);			
	}
	
	public void dispose() {
		frame.setVisible(false);
		frame.dispose();
	}
	
	private Dimension getScreenSize() {
		Toolkit toolkit = frame.getToolkit();
		return toolkit.getScreenSize();
	}
	
}
