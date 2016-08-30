package maptool.export.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

import framework.types.*;

import maptool.gui.mainFrame.*;
import maptool.object.*;
import net.miginfocom.swing.*;
import utilities.mvc.*;
import utilities.swing.*;
import utilities.swing.FileFilter;

public class ModelExportView extends DefaultView {

	ContainerObject containerObject = null;
	
	JDialog dialog = null;

	FileSearchPanel fileSearchPanel = null;
	
	JButton cancleButton = null;
	
	JButton exportButton = null;
	
	public ModelExportView(Frame owner, Model model) {
		super(model);
		
		dialog = new JDialog(owner, "ModelExport", true);
		dialog.setLayout(new MigLayout());
		dialog.add(fileSearchPanel = new FileSearchPanel(new FileFilter(".xml", "Model XML"), new File("models\\").getAbsolutePath()), "span, wrap");
		dialog.add(cancleButton = new JButton("Abbrechen"), "grow");
		dialog.add(exportButton = new JButton("Exportieren"), "grow");
		
		init();
		
		dialog.pack();
		dialog.setVisible(true);
	}
	
	private void init() {
		cancleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File exportFile = fileSearchPanel.getFile();
				if (exportFile != null) {
					MainModel model = (MainModel) getModel();
					ComplexContainerObject cObj = 
						new ComplexContainerObject((ComplexContainerObject)model.getSelectedObjects().get(0).getUserObject());
					cObj.transfareToZero();
					model.exportToModel(cObj, exportFile);
					model.modelObjects.put(cObj.getName(), cObj);
					model.notifyViews(MainModel.MAINMENU_RELOAD);
				}
				dialog.dispose();
			}
		});
	}

	@Override
	public Component getComponent() {
		return dialog;
	}
}
