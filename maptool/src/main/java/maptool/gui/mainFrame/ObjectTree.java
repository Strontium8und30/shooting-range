package maptool.gui.mainFrame;

import java.awt.*;
import java.awt.dnd.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.tree.*;

import maptool.object.*;
import utilities.*;
import framework.types.*;

/**
 * Erstellt einen grafischen Objektbaum (JTree) durch Rekursives durchgehen
 * der Objektstruktur der das Auswählen und Bearbeiten von Objekten erlaubt.
 * Er hält unteranderem einen CellRenderer der die unterschiedlichen Objekte
 * verschieden darstellen soll und einen MouseListener der dafür sorgt das ein
 * PopupMenü (JPopupMenu) verwendbar ist.
 * 
 * @author Thorben
 */
public class ObjectTree extends JTree {
		
	public ObjectTree(TreeModel model) {
		super(model);
		setDragEnabled(true);
		setDropMode(DropMode.USE_SELECTION);
		setDropTarget(new DropTarget(this, TransferHandler.MOVE, new ObjectDropTarget()));
		setCellRenderer(new ComplexObjectCellRender());
	}
			
	public List<ObjectTreeNode> getSelectedNodes() {
		List<ObjectTreeNode> selectedNodes = new ArrayList<ObjectTreeNode>();
		if (getSelectionPaths() == null) return null;
		for (TreePath path : getSelectionPaths()) {
			selectedNodes.add((ObjectTreeNode)path.getLastPathComponent());
		}
		return selectedNodes;
	}
	
	public DefaultTreeModel getTreeModel() {
		return (DefaultTreeModel)getModel();
	}
	

	public static class ObjectTreeNode extends DefaultMutableTreeNode {
		
		public ObjectTreeNode(ComplexGraphicalObject sObj) {
			super(sObj);
		}
		
		public ObjectTreeNode(ComplexContainerObject complexObject) {
			super(complexObject);
			if (complexObject == null) {
				return;
			}
			
			for(ContainerObject cObj : complexObject.getContainerObjectList()) {
				add(new ObjectTreeNode((ComplexContainerObject)cObj));
			}
			
			for (GraphicalObject sObj : complexObject.getGraphicalObjectList()){
				add(new ObjectTreeNode((ComplexGraphicalObject)sObj));
			}
		}
	}
	
	private class ComplexObjectCellRender extends DefaultTreeCellRenderer {
	    @Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
					boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

	    	if (((ObjectTreeNode)value).getUserObject() instanceof ComplexContainerObject) {
	    		
		    	ComplexContainerObject complexObject = (ComplexContainerObject)
		    										((ObjectTreeNode) value).getUserObject();
		    	
		    	super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		    	setText(complexObject.getName());
		    	setIcon(Utilities.getIcon("icons/complexObject.gif"));
	    	} else if (((ObjectTreeNode)value).getUserObject() instanceof GraphicalObject){
	    		ComplexGraphicalObject simpleObject = (ComplexGraphicalObject)((ObjectTreeNode)value).getUserObject();
	    		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
	    		setIcon(Utilities.getIcon("icons/simpleObject.gif"));
	    		setText(simpleObject.getName());
	    	} else {
	    		setText("Objekt Unbekannt");
	    	}
	    	return this;
	    }
	}

	public class ObjectDropTarget extends DropTargetAdapter {

		List<ObjectTreeNode> sourceNodes = null;
		
		TreePath targetPath = null;
		
		ObjectTreeNode targetNode = null;
		
		@Override
		public void dragOver(DropTargetDragEvent dtde) {
			if (sourceNodes == null) sourceNodes = new ArrayList<ObjectTreeNode>(getSelectedNodes());
			targetPath = getClosestPathForLocation(dtde.getLocation().x, dtde.getLocation().y);
			targetNode = (ObjectTreeNode)targetPath.getLastPathComponent();
			if(!targetPath.equals(getSelectionPath()) && isDropAlowed(sourceNodes, targetNode)) {
				setSelectionPath(targetPath);
			}
		}
		
		@Override
		public void drop(DropTargetDropEvent dtde) {
			targetNode = (ObjectTreeNode)targetPath.getLastPathComponent();
				
			if(isDropAlowed(sourceNodes, targetNode)) {
				ComplexContainerObject targetObject = (ComplexContainerObject)targetNode.getUserObject();
				for(ObjectTreeNode sourceNode : sourceNodes) {
					if(sourceNode.getUserObject() instanceof ComplexContainerObject) {
						ComplexContainerObject sourceObject = (ComplexContainerObject)sourceNode.getUserObject();
						getTreeModel().removeNodeFromParent(sourceNode);
						sourceObject.removeFromParent();
						targetNode.insert(sourceNode, 0);	
						targetObject.addContainerObject(sourceObject);
					} else if (sourceNode.getUserObject() instanceof ComplexGraphicalObject) {
						ComplexGraphicalObject sourceObject = (ComplexGraphicalObject)sourceNode.getUserObject();
						getTreeModel().removeNodeFromParent(sourceNode);
						sourceObject.remove();
						targetNode.add(sourceNode);	
						targetObject.addGraphicalObject(sourceObject);
					}
				}
				dtde.dropComplete(true);
				expandPath(new TreePath(targetNode.getPath()));
				updateUI();
			} else {
				dtde.rejectDrop();
				dtde.dropComplete(false);
			}
			TreePath[] treePaths = new TreePath[sourceNodes.size()];
			for(int i_path = 0; i_path < sourceNodes.size(); i_path++) {
				treePaths[i_path] = new TreePath(sourceNodes.get(i_path).getPath());
			}
			setSelectionPaths(treePaths);
			sourceNodes =  null;
		}
				
		private boolean isDropAlowed(List<ObjectTreeNode> sourceNode, ObjectTreeNode targetNode) {
			for(int i = 0; i < targetNode.getPath().length; i++) {
				if (sourceNode.contains((ObjectTreeNode)targetNode.getPath()[i])) {
					return false; 
				}
			}
			return targetNode.getUserObject() instanceof ComplexContainerObject;						
		}
	}
}
