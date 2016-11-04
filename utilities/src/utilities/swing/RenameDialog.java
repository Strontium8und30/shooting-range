package utilities.swing;

import static java.awt.GridBagConstraints.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class RenameDialog extends JDialog {
	
	/** Der Name des Objekts das umbenannt werden soll*/
	final JTextField nameEdit;
	
	/** Bearbeitung abschlieﬂen */
	final JButton uebernehmen;
	
	/** Bearbeitung abrechen */
	final JButton abbrechen;
	
	String name;
	
	public RenameDialog(String name) {
		this.name = name;
		setTitle("Umbenennen");
		setModal(true);
		setLayout(new GridBagLayout());
		
		nameEdit = new JTextField(name);
		
		uebernehmen = new JButton("‹bernehmen");
		uebernehmen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {				
				setNewName(nameEdit.getText());
				dispose();
			}			
		});
		
		abbrechen = new JButton("Abbrechen");
		abbrechen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}			
		});
		
		this.getContentPane().add(nameEdit, 
				new GridBagConstraints(0, 0, 2, 1, 0.1, 0.6, NORTH, BOTH, new Insets(10, 10, 5, 0), 0, 0));
		this.getContentPane().add(uebernehmen, 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.6, CENTER, BOTH, new Insets(10, 10, 5, 0), 0, 0));
		this.getContentPane().add(abbrechen, 
				new GridBagConstraints(1, 1, 1, 1, 0.1, 0.6, CENTER, BOTH, new Insets(10, 10, 5, 0), 0, 0));
	}
	
	private void setNewName(String name) {
		this.name = name;
	}

	public String getNewName() {
		pack();
		setVisible(true);
		return name;
	}
}
