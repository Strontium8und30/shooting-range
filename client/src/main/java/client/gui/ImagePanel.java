package client.gui;

import java.awt.*;

import javax.swing.*;

import utilities.resource.*;

public class ImagePanel extends JPanel {

	/** Das Hintergrundbild */
	Image bgImage;
	
	public ImagePanel(String file/*, int width, int height*/) {
		bgImage = Toolkit.getDefaultToolkit().getImage(ResourceLoader.getFile(file).getAbsolutePath());
		setFocusable(true);
		setOpaque(false);
		setVisible(true);
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				while(true) {
					repaint();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {}
				}
			}
			
		}).start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
		super.paintComponent(g);
	}	
}
