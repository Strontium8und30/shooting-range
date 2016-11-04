package maptool.gui.mainFrame;

import javax.swing.tree.*;

import maptool.gui.mainFrame.ObjectTree.*;
import maptool.object.*;

public class ObjectTreeModel extends DefaultTreeModel {
	
	public ObjectTreeModel(ComplexContainerObject complexObject) {
		super(createRootNode(complexObject));
	}
	
	private static ObjectTreeNode createRootNode(ComplexContainerObject complexObject) {
		return new ObjectTreeNode(complexObject);
	}
}
