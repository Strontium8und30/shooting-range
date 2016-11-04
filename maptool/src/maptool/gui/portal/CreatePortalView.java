package maptool.gui.portal;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import maptool.gui.mainFrame.*;
import net.miginfocom.swing.*;
import utilities.mvc.*;
import framework.types.*;
import framework.types.Portal.*;

public class CreatePortalView extends DefaultView {

	JDialog dialog = null;
	
	ObjectTree leftObjectTree = null;
	
	ObjectTree rightObjectTree = null;
	
	ObjectTreeModel objectTreeModel = null;
	
	JRadioButton radioAtob = null;
	
	JRadioButton radioBtoa = null;
	
	JRadioButton radioAtobBtoa = null;
	
	JButton cancle = null;
	
	JButton ok = null;
	
	
	public CreatePortalView(Model model) {
		super(model);
		dialog = new JDialog((JFrame)null, "Neues Protal hinzufügen", true);
		dialog.setLayout(new MigLayout("","[grow, fill][center][grow, fill]","[][]"));
		
		objectTreeModel = new ObjectTreeModel(((MainModel)model).getMap().getMainContainerObject());
		leftObjectTree = new ObjectTree(objectTreeModel);
		dialog.add(new JScrollPane(leftObjectTree), "cell 0 0 1 2, grow");
		
		JPanel radioPanel = new JPanel(new MigLayout());
		ButtonGroup radioGroup = new ButtonGroup();
		radioPanel.add(radioAtob = new JRadioButton("A nach B"), "wrap");
		radioGroup.add(radioAtob);
		radioPanel.add(radioBtoa = new JRadioButton("B nach A"), "wrap");
		radioGroup.add(radioBtoa);
		radioPanel.add(radioAtobBtoa = new JRadioButton("A nach B und B nach A", true));
		radioGroup.add(radioAtobBtoa);
		dialog.add(radioPanel, "wrap");
		
		JPanel buttonPanel = new JPanel(new MigLayout());
		buttonPanel.add(cancle = new JButton("Abbrechen"), "grow, wrap");
		buttonPanel.add(ok = new JButton("Ok"), "grow");
		dialog.add(buttonPanel);
		
		rightObjectTree = new ObjectTree(objectTreeModel);
		dialog.add(new JScrollPane(rightObjectTree), "cell 2 0 1 2, grow");

		init();
		dialog.pack();
		dialog.setVisible(true);
	}
	
	public void init() {
		cancle.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		ok.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				PortalInf portal = new Portal();
				portal.setPortalA((ContainerObject)leftObjectTree.getSelectedNodes().get(0).getUserObject());
				portal.setPortalB((ContainerObject)rightObjectTree.getSelectedNodes().get(0).getUserObject());
				portal.setType(radioAtob.isSelected() ? PortalType.ATOB :
							   radioBtoa.isSelected() ? PortalType.BTOA : 
														PortalType.ATOB_BTOA);
				((MainModel) getModel()).addPortal(portal);
				dialog.dispose();
			}
		});
	}

	@Override
	public Component getComponent() {
		return dialog;
	}

}
