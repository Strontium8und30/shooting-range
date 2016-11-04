package maptool.gui.mainFrame;

import static java.awt.GridBagConstraints.*;

import java.awt.*;
import java.io.*;

import javax.swing.*;

import utilities.resource.ResourceLoader;

public class TextureDetailPanel extends JPanel {

	private ImageViewer imageViewer = null;
	
	private Integer textureId = null;
	
	public TextureDetailPanel(File textureFile, Integer textureId) {
		this.textureId = textureId;
		
		setLayout(new GridBagLayout());
		
		imageViewer = new ImageViewer();
		imageViewer.setImage(ResourceLoader.getFile(textureFile.toString()));
		
		add(imageViewer, 
				new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0, 
				WEST, NONE, new Insets(10, 10, 10, 10), 0, 0));
		add(new JLabel("Name: " + imageViewer.getImageName()), 
				new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, 
				WEST, VERTICAL, new Insets(10, 10, 10, 10), 0, 0));
		add(new JLabel("Größe: 256 x 256"), 
				new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, 
				WEST, VERTICAL, new Insets(10, 10, 10, 10), 0, 0));		
	}
	
	public Integer getTextureId() {
		return textureId;
	}
	
	public class ImageViewer extends JComponent {
		
		/** Das Bild */
		private Image image;
		
		/** Der Name des Bildes */
		private String imageName;
		
		
		public ImageViewer() {
			setPreferredSize(new Dimension(100, 100));
		}
		
		public void setImage(File file) {
			String filePath = file.getPath();
			image = Toolkit.getDefaultToolkit().getImage(filePath);
			imageName = filePath.substring(filePath.lastIndexOf("/") + 1);
			if (image != null) {
				repaint();
			}
		}
		
		public Image getImage() {
			return image;
		}
		
		public String getImageName() {
			return imageName;
		}
		
		@Override
		public void paintComponent(Graphics g) {
			if(image != null) {
				g.drawImage(image, 0, 0, 100, 100, this);
			}
		}
	}
}
