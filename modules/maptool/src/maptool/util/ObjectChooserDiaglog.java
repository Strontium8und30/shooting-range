package maptool.util;

import static java.awt.GridBagConstraints.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.*;


public class ObjectChooserDiaglog<T> extends JDialog {

	/** Eine Liste aus der ein Object ausgewählt werden muss */
	JList objectList = null;
	
	/** Bestätigungs Button */
	JButton okButton = null;
	
	/** Abbrechen Button */
	JButton cancleButton = null;
	
	/** das aus der Liste ausgewählte Objekt */
	private T selectedObject = null;
	
	public ObjectChooserDiaglog() {
		this(null, null);
	}
	
	public ObjectChooserDiaglog(String infoMessage) {
		this(null, infoMessage);
	}
	
	public ObjectChooserDiaglog(List<T> objects) {
		this(objects, null);
	}
	
	public ObjectChooserDiaglog(List<T> objects, String infoMessage) {
		
		setTitle("Bitte wählen Sie ein Objekt:");
		setLayout(new GridBagLayout());
		setModal(true);
		
		objectList = new JList(new DefaultListModel());
		initListModel(objects);
		
		objectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		objectList.setBorder(new EtchedBorder());
		objectList.setPreferredSize(new Dimension(250,400));
		
		okButton = new JButton("Ok");		
		cancleButton = new JButton("Abbrechen");
		
		okButton.addActionListener(new ActionListener() {
			@Override
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				selectedObject = (T)objectList.getSelectedValue();
				dispose();
			}			
		});
		
		cancleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}			
		});
		
		if (infoMessage != null) {
			JPanel infoPanel = new JPanel();
			infoPanel.setBorder(new EtchedBorder());
			infoMessage = "<html>" + infoMessage.replaceAll("\n", "<br>") + "</html>";
			infoPanel.add(new JLabel(infoMessage));
			
			this.add(infoPanel, 
					new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0, 
					NORTH, BOTH, new Insets(10, 10, 0, 10), 0, 0));
		}
		this.add(objectList, 
				new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, 
				NORTH, BOTH, new Insets(10, 10, 10, 10), 0, 0));
		
		this.add(okButton, 
				new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, 
				SOUTH, HORIZONTAL, new Insets(0, 10, 10, 10), 0, 0));
		
		this.add(cancleButton, 
				new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, 
				SOUTH, HORIZONTAL, new Insets(0, 0, 10, 10), 0, 0));	
	}
	
	public void initListModel(List<T> objects) {
		if(objects != null) {
			for(T cObj : objects) {
				((DefaultListModel)objectList.getModel()).addElement(cObj);
			}
		}
	}
	
	public void addObject(T obj) {
		((DefaultListModel)objectList.getModel()).addElement(obj);
	}
	
	public T getComplexObject() {
		pack();
		setVisible(true);
		return selectedObject;
	}
}
