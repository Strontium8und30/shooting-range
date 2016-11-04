package maptool.gui.mainFrame;

import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;

import maptool.control.*;
import maptool.export.gui.*;
import maptool.gui.mainFrame.ObjectTree.*;
import maptool.gui.switchs.*;
import maptool.object.*;
import utilities.mvc.*;
import utilities.swing.*;
import framework.types.*;

public class ObjectPopupMenu extends JPopupMenu {

	/** Alle Selektierten Knoten Aus dem ObjectTree */
	List<ObjectTreeNode> selectedNodes;
	
	/** Gibt es markierte SimpleObject's */
	boolean contains_sObj = false;
	
	/** Gibt es markierte ComplexObject's */
	boolean contains_cObj = false;
	
	JCheckBoxMenuItem item_show;
	JCheckBoxMenuItem item_walk;
	JCheckBoxMenuItem item_shoot;
	JCheckBoxMenuItem item_autoAlignment;
	JMenuItem item_exportToModel;
	JMenuItem item_copyToModel;
	JMenuItem item_moveObject;
	JMenuItem item_rename;
	JMenuItem item_delete;
	JMenuItem item_TexturBearbeiten;
	JMenuItem item_EditSwitch;
	JMenuItem item_AddComplexObject;
	
	Model model = null;

	public ObjectPopupMenu(Model model, TreeModel treeModel, MouseEvent event, List<ObjectTreeNode> selectedNodes) {
		this.model = model;
		this.selectedNodes = selectedNodes; 

		for (ObjectTreeNode selectedNode : selectedNodes) {
			if(selectedNode.getUserObject() instanceof GraphicalObject) {
				contains_sObj = true;
			} else if (selectedNode.getUserObject() instanceof ComplexContainerObject) {
				contains_cObj = true;
			}
		}
		
		item_show = new JCheckBoxMenuItem("Anzeigen");
		item_show.setEnabled(contains_sObj || contains_cObj);
		
		int visibles = 0;
		
		for (ObjectTreeNode selectedNode : selectedNodes) {			
			if (((ObjectMetaData)selectedNode.getUserObject()).isVisible()) {
				visibles++;
			} else {
				visibles--;
			}
		}	
		
		item_show.setSelected(visibles < 0 ? false : true);
		item_show.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				for (ObjectTreeNode selectedNode : ObjectPopupMenu.this.selectedNodes) {
					((ObjectMetaData)selectedNode.getUserObject()).setVisible(item_show.isSelected());
				}					
				getModel().glRepaint();
			}			
		});
		add(item_show);

		
		item_walk = new JCheckBoxMenuItem("Kollisionserkennung");
		item_walk.setEnabled(contains_sObj || contains_cObj);
		
		int hard = 0;
		
		for (ObjectTreeNode selectedNode : selectedNodes) {			
			if (((ObjectMetaData)selectedNode.getUserObject()).isHard()) {
				hard++;
			} else {
				hard--;
			}
		}	
		
		item_walk.setSelected(hard < 0 ? false : true);
		item_walk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				for (ObjectTreeNode selectedNode : ObjectPopupMenu.this.selectedNodes) {
					((ObjectMetaData)selectedNode.getUserObject()).setHard(item_walk.isSelected());
				}					
				getModel().glRepaint();
			}			
		});
		add(item_walk);
		
		
		item_shoot = new JCheckBoxMenuItem("Durchschießen");
		item_shoot.setEnabled(contains_sObj || contains_cObj);
		
		int shootable = 0;
		
		for (ObjectTreeNode selectedNode : selectedNodes) {			
			if (((ObjectMetaData)selectedNode.getUserObject()).isShootable()) {
				shootable++;
			} else {
				shootable--;
			}
		}	
		
		item_shoot.setSelected(shootable < 0 ? false : true);
		item_shoot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				for (ObjectTreeNode selectedNode : ObjectPopupMenu.this.selectedNodes) {
					((ObjectMetaData)selectedNode.getUserObject()).setShootable(item_show.isSelected());
				}					
				getModel().glRepaint();
			}			
		});
		add(item_shoot);
		
		
		int autoAlignment = 0;
		
		for (ObjectTreeNode selectedNode : selectedNodes) {			
			if (selectedNode.getUserObject() instanceof ComplexContainerObject && 
			   ((ComplexContainerObject)selectedNode.getUserObject()).isAutoAlignment()) {
				autoAlignment++;
			} else {
				autoAlignment--;
			}
		}
		item_autoAlignment = new JCheckBoxMenuItem("Automatisch ausrichten");	
		item_autoAlignment.setEnabled(!contains_sObj || contains_cObj);
		item_autoAlignment.setSelected(autoAlignment < 0 ? false : true);
		item_autoAlignment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {				
				for (ObjectTreeNode selectedNode : ObjectPopupMenu.this.selectedNodes) {
					((ComplexContainerObject)selectedNode.getUserObject()).setAutoAlignment(item_show.isSelected());
				}					
			}			
		});
		add(item_autoAlignment);
		
		item_exportToModel = new JMenuItem("Export als Model");	
		item_exportToModel.setEnabled(contains_cObj && !contains_sObj && selectedNodes.size() == 1);
		item_exportToModel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				new ModelExportView((JFrame)getModel().getMainView().getComponent(), getModel());
			}			
		});
		add(item_exportToModel);
		
		item_copyToModel = new JMenuItem("Als Model Übernhemen");	
		item_copyToModel.setEnabled(contains_cObj && !contains_sObj && selectedNodes.size() == 1);
		item_copyToModel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				ObjectTreeNode selectedNode = ObjectPopupMenu.this.selectedNodes.get(0);
				ComplexContainerObject cObj = new ComplexContainerObject((ComplexContainerObject)selectedNode.getUserObject());
				cObj.transfareToZero();
				getModel().setModelObject(cObj);
			}			
		});
		add(item_copyToModel);
		
		item_moveObject = new JMenuItem("Objekt bewegen");	
		item_moveObject.setEnabled(!contains_sObj && contains_cObj && selectedNodes.size() == 1);
		item_moveObject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				getModel().setMoveSelectedObjects(true);
				ObjectTreeNode selectedNode = ObjectPopupMenu.this.selectedNodes.get(0);
				ComplexContainerObject cObj = (ComplexContainerObject)selectedNode.getUserObject();
				getModel().setBackupObject(cObj);
				cObj.setVisible(false);
				ComplexContainerObject modelObjekt = new ComplexContainerObject(cObj);
				modelObjekt.transfareToZero();
				getModel().setModelObject(modelObjekt);
				Controller.getCamera().setPosition(cObj.calcGeometricCenter());
			}			
		});
		add(item_moveObject);
				
		
		item_rename = new JMenuItem("Umbenennen");
		item_rename.setEnabled(contains_sObj || contains_cObj && selectedNodes.size() == 1);
		item_rename.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {				
				RenameDialog renameDialog = new RenameDialog(((ComplexObjectMetaData)ObjectPopupMenu.this.selectedNodes.get(0).getUserObject()).getName());	
				((ComplexObjectMetaData)ObjectPopupMenu.this.selectedNodes.get(0).getUserObject()).setName(renameDialog.getNewName());		
			}			
		});
		add(item_rename);
		
		item_delete = new JMenuItem("Löschen");
		item_delete.setEnabled(contains_sObj || contains_cObj);
		item_delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getModel().removeObjectFromTree(ObjectPopupMenu.this.selectedNodes);
				getModel().glRepaint();
			}			
		});
		add(item_delete);
		
		item_TexturBearbeiten = new JMenuItem("Textur bearbeiten");
		item_TexturBearbeiten.setEnabled(!contains_sObj && contains_cObj && selectedNodes.size() == 1);
		item_TexturBearbeiten.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				TextureDialog textureDialog = new TextureDialog(getModel());
				ObjectTreeNode selectedNode = ObjectPopupMenu.this.selectedNodes.get(0);
				ComplexContainerObject cObj = (ComplexContainerObject)selectedNode.getUserObject();
				int textureID = textureDialog.getTexture();
				if (textureID != -1) {
					cObj.setTextureID(textureID);
				}
			}			
		});
		add(item_TexturBearbeiten);
		
		item_EditSwitch = new JMenuItem("Schalter bearbeiten");
		item_EditSwitch.setEnabled(selectedNodes.size() == 1 && getModel().isSwitch((ComplexContainerObject)selectedNodes.iterator().next().getUserObject()));
		item_EditSwitch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ObjectTreeNode selectedNode = ObjectPopupMenu.this.selectedNodes.get(0);
				ComplexContainerObject cObj = (ComplexContainerObject)selectedNode.getUserObject();
				CreateSwitchView switchView = new CreateSwitchView(getModel(), cObj);
			}			
		});
		add(item_EditSwitch);
		
		add(new JSeparator());
		
		item_AddComplexObject = new JMenuItem("Kontainer hinzufügen");
		item_AddComplexObject.setEnabled(!contains_sObj && contains_cObj && selectedNodes.size() == 1);
		item_AddComplexObject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ComplexContainerObject complexObject = new ComplexContainerObject();
				complexObject.setName("Neuer Kontainer");
				getModel().notifyViews(MainModel.ADD_COMPLEXOBJECT_TO_TREE, complexObject);
			}			
		});
		add(item_AddComplexObject);
		
		show(event.getComponent(), event.getX(), event.getY());
	}
	
	public MainModel getModel() {
		return (MainModel)model;
	}
}
