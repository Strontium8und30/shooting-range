package maptool.gui.mainFrame;

import static java.awt.GridBagConstraints.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import utilities.mvc.*;
import utilities.swing.*;
import utilities.swing.FileFilter;

public class TextureDialog extends DefaultView {

	/** Der Texture Dialog */
	JDialog dialog = null;
	
	/** Die Texturauswahlliste */
	PanelList panelList = null;
	
	/** Texture hinzufügen */
	JButton addTextureButton = null;
	
	/** Ok Button */
	JButton okButton = null;
	
	/** Abbrechen Button */
	JButton cancelButton = null;
	
	/** Selektierte Texture ID */
	int selectedTexture = -1;
	
	public TextureDialog(Model model) {
		super(model);
		dialog = new JDialog();
		dialog.setModal(true);
		dialog.setTitle("Texture bearbeiten");
		dialog.setSize(400, 500);
		dialog.setLayout(new GridBagLayout());
		
		addTextureButton = new JButton("Hinzufügen");
		okButton = new JButton("Ok");
		cancelButton = new JButton("Abbrechen");
		
		dialog.add(getTextureChooserPanel(), 
				new GridBagConstraints(0, 0, 2, 1, 1.0, 0.7, 
				CENTER, BOTH, new Insets(10, 10, 10, 10), 0, 0));
		
		dialog.add(getTextureSettingsPanel(), 
				new GridBagConstraints(0, 1, 2, 1, 1.0, 0.3, 
				CENTER, BOTH, new Insets(10, 10, 10, 10), 0, 0));
		
		dialog.add(addTextureButton, 
				new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0, 
				CENTER, BOTH, new Insets(10, 10, 10, 10), 0, 0));
		
		dialog.add(okButton, 
				new GridBagConstraints(0, 3, 1, 1, 0.5, 0.0, 
				CENTER, BOTH, new Insets(10, 10, 10, 10), 0, 0));
		
		dialog.add(cancelButton, 
				new GridBagConstraints(1, 3, 1, 1, 0.5, 0.0, 
				CENTER, BOTH, new Insets(10, 10, 10, 10), 0, 0));
		
		addTextureButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FilteredFileChooser importFileChooser = new FilteredFileChooser(FilteredFileChooser.LOAD);
				importFileChooser.setFileFilter(new FileFilter("", "Texturen"));
				importFileChooser.showOpenDialog(dialog);
				String textureFile = importFileChooser.getSelectedFile().toString();
				Integer texId = ((MainModel)getModel()).getMap().prepareTexture(textureFile);
				panelList.addPanel(new TextureDetailPanel(new File(textureFile), texId));
				dialog.setVisible(true);
			}			
		});
		
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TextureDetailPanel selectedPanel = (TextureDetailPanel) panelList.getSelectedPanel();
				if (selectedPanel != null) {
					selectedTexture = selectedPanel.getTextureId();
				}
				dialog.dispose();
			}			
		});
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();	
			}			
		});
	}	
	
	public JScrollPane getTextureChooserPanel() {
		panelList = new PanelList();

		Map<String, Integer> texture = ((MainModel)getModel()).getMap().getTextureFileMap();
		for(String texFile : texture.keySet()) {	
			panelList.addPanel(new TextureDetailPanel(new File(texFile), texture.get(texFile)));
		}

		return panelList.getPanelList();
	}
	
	public JPanel getTextureSettingsPanel() {
		JPanel panel = new JPanel();
		return panel;
	}

	public int getTexture() {
		//dialog.pack();
		dialog.setVisible(true);
		return selectedTexture;
	}
	
	@Override
	public Component getComponent() {
		return dialog;
	}
}
