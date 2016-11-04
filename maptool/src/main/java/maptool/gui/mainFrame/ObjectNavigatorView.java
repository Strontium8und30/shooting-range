package maptool.gui.mainFrame;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import framework.types.*;

import maptool.gui.mainFrame.ObjectTree.*;
import maptool.object.*;
import maptool.util.*;
import utilities.log.*;
import utilities.mvc.*;

public class ObjectNavigatorView extends DefaultView {
	
	/** Logging */
	public static Log log = LogFactory.getLog(ObjectNavigatorView.class);
	
	/** Auswahl für die verschiedenen Objekte */
	JTabbedPane tabbedPane = null;
	
	/** Das SrcollPane auf dem der ObjectTree liegt */
	JScrollPane scrollPane = null;
	
	/** Der Baum zur Auswahl von Objekten */
	ObjectTree objectTree = null;
	
	/** Das TreeModel zum ObjectTree */
	DefaultTreeModel objectTreeModel = null;
	
	JTree portalTree = null;
	
	DefaultTreeModel portalTreeModel = null;
		
	
	public ObjectNavigatorView(Model model) {
		super(model);
		
		objectTreeModel = new ObjectTreeModel(getMainModel().getMap().getMainContainerObject());
		objectTree = new ObjectTree(objectTreeModel);		
		objectTree.addMouseListener(new ComplexObjectMouseListener());		
		objectTree.addTreeSelectionListener(new ObjectTreeSelectionListener());
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Objekte", new JScrollPane(objectTree));
		tabbedPane.addTab("Portale", new JScrollPane(initPortalTree()));
	}

	public JTree initPortalTree() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Portale");
		
		for (PortalInf portal : getMainModel().getMap().getPortals()) {
			DefaultMutableTreeNode portalNode = new DefaultMutableTreeNode(portal);
			portalNode.add(new DefaultMutableTreeNode(portal.getPortalA()));
			portalNode.add(new DefaultMutableTreeNode(portal.getPortalB()));
			root.add(portalNode);	
		}
			
		portalTreeModel = new DefaultTreeModel(root);
		if (portalTree == null) {
			portalTree = new JTree(portalTreeModel);
			portalTree.addMouseListener(new ComplexObjectMouseListener());
		} else {
			portalTree.setModel(portalTreeModel);
		}
		return portalTree;
	}
	
	public TreeModel getTreeModel() {
		return objectTreeModel;
	}
	
	public MainModel getMainModel() {
		return (MainModel)getModel();
	}
	
	@Override
	public Component getComponent() {
		return tabbedPane;
	}

	@Override
	public void update(EventAction event, Object arg) {
		if(event.equals(MainModel.ADD_COMPLEXOBJECT_TO_TREE)) {
			List<ObjectTreeNode> selectedNodes = objectTree.getSelectedNodes();
			if (selectedNodes == null || selectedNodes.isEmpty()) {
				ComplexContainerObject complexObject = (ComplexContainerObject)arg;
				ObjectTreeNode rootNode = (ObjectTreeNode)objectTree.getTreeModel().getRoot();
				ComplexContainerObject parentObject = (ComplexContainerObject)rootNode.getUserObject();
				parentObject.addContainerObject(complexObject);
				ObjectTreeNode treeNode = new ObjectTreeNode(complexObject);
				objectTree.getTreeModel().insertNodeInto(treeNode, rootNode, 0);
				objectTree.setSelectionPath(new TreePath(((ObjectTreeNode)treeNode.getParent()).getPath()));
				objectTree.expandPath(objectTree.getSelectionPath());
				objectTree.scrollPathToVisible(objectTree.getSelectionPath());
			} else {
				if(selectedNodes.size() == 1) {
					ComplexContainerObject complexObject = (ComplexContainerObject)arg;
					ComplexContainerObject parentObject = (ComplexContainerObject)selectedNodes.get(0).getUserObject();
					parentObject.addContainerObject(complexObject);
					ObjectTreeNode treeNode = new ObjectTreeNode(complexObject);
					objectTree.getTreeModel().insertNodeInto(treeNode, selectedNodes.get(0), 0);
					objectTree.setSelectionPath(new TreePath(((ObjectTreeNode)treeNode.getParent()).getPath()));
					objectTree.expandPath(objectTree.getSelectionPath());
					objectTree.scrollPathToVisible(objectTree.getSelectionPath());
				} else {
					String infoMessage = "Es ist mehr als ein Objekt ausgewählt.\n" +
										 "Sie müssen ein Objekt auswählen unter das\n" +
										 "das neue Objekt gehängt werden soll.";
					
					ObjectChooserDiaglog<ObjectTreeNode> objektChooser = 
									new ObjectChooserDiaglog<ObjectTreeNode>(infoMessage);
					
					for (ObjectTreeNode node : selectedNodes) {
						if (node.getUserObject() instanceof ComplexContainerObject) {
							objektChooser.addObject(node);
						}
					}
					
					ObjectTreeNode complexObject = objektChooser.getComplexObject();
					if(complexObject != null) {
						((ComplexContainerObject)complexObject.getUserObject()).addContainerObject((ComplexContainerObject)arg);
						complexObject.insert(new ObjectTreeNode((ComplexContainerObject)arg), 0);
					}
				}	
			}
		} else if (event.equals(MainModel.OBJECTTREE_DELETE_OBJECT)) {
			objectTreeModel.removeNodeFromParent((ObjectTreeNode)arg);
		} else if (event.equals(MainModel.OBJECTTREE_RELOAD)) {
			objectTreeModel = new ObjectTreeModel(getMainModel().getMap().getMainContainerObject());
			objectTree.setModel(objectTreeModel);
			objectTree.repaint();
			log.debug("ObjectTree reload");
		} else if (event.equals(MainModel.PORTALTREE_ADD_PORTAL)) {
			initPortalTree();
			tabbedPane.validate();
//			DefaultMutableTreeNode root = (DefaultMutableTreeNode)portalTree.getModel().getRoot();
//			Portal portal = (Portal)arg;
//			
//			DefaultMutableTreeNode portalNode = new DefaultMutableTreeNode(portal.getPortalA() + " <-> " + portal.getPortalB());
//			portalNode.add(new DefaultMutableTreeNode(portal.getPortalA()));
//			portalNode.add(new DefaultMutableTreeNode(portal.getPortalB()));
//			root.add(portalNode);
//			portalTree.validate();
//			portalTree.invalidate();
//			portalTree.repaint();
		}
		
	}
	
	/**
	 * MouseListener der den selektierten Knoten bestimmt und ein 
	 * passendes Kontextmenü aufruft.
	 * 
	 * @author DrosteTh
	 */
	private class ComplexObjectMouseListener extends MouseAdapter {

	    @Override
		public void mouseClicked(MouseEvent event) {
	    	List<ObjectTreeNode> selectedNodes = objectTree.getSelectedNodes();
	    	
	    	if (event.getClickCount() == 2 && selectedNodes.size() == 1) {
	    		getMainModel().moveToObject((ComplexContainerObject)selectedNodes.get(0).getUserObject());
	    	}
		    
	    	//KontexMenü
	    	if (event.getButton() == MouseEvent.BUTTON3 && selectedNodes != null && !selectedNodes.isEmpty()) {
	    		new ObjectPopupMenu(getModel(), getTreeModel(), event, selectedNodes);
	    	}
	    }
	}
	
	private class ObjectTreeSelectionListener implements TreeSelectionListener {
		
		List<ObjectTreeNode> oldSelectedNodes = new ArrayList<ObjectTreeNode>();
		
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			List<ObjectTreeNode> selectedNodes = objectTree.getSelectedNodes();
	    	    	
	    	if (selectedNodes != null && selectedNodes != oldSelectedNodes) {
	    		getMainModel().setSelectedObjects(selectedNodes);
	    		
		    	for (ObjectTreeNode treeNode: oldSelectedNodes) {
		    		if (!selectedNodes.contains(treeNode)) {
		    			((ComplexObjectMetaData)treeNode.getUserObject()).setSelected(false);
		    		}
		    	}
		    	
		    	if (!selectedNodes.equals(oldSelectedNodes)) {
		    		for (ObjectTreeNode treeNode: selectedNodes) {
		    			((ComplexObjectMetaData)treeNode.getUserObject()).setSelected(true);
		    		}	    		
		    	}
		    	oldSelectedNodes = selectedNodes;
		    	getMainModel().glRepaint();
	    	}

		}			
	}
}
