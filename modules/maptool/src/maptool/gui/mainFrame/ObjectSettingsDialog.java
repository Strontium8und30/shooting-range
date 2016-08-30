package maptool.gui.mainFrame;

import javax.swing.*;

public class ObjectSettingsDialog extends JDialog {
	
	/** Auswahl für die Verschiedenen Einstellungen */
	private JTabbedPane tabPane = null;
	
	/** Das Texture Einstellungs Panel */
	private JPanel texturePanel = null;
	
	public ObjectSettingsDialog() {
		
		texturePanel = getTextureSettingsPanel();
		/**
		 * 
		 */
		
		tabPane = new JTabbedPane();
		tabPane.add(texturePanel);
	}
	
	private JPanel getTextureSettingsPanel() {
		
		return new JPanel();
	}
}
