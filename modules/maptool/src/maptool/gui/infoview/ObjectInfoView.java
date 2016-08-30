package maptool.gui.infoview;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import framework.types.*;
import maptool.gui.mainFrame.*;
import maptool.gui.mainFrame.ObjectTree.*;
import maptool.object.*;
import net.miginfocom.swing.*;
import utilities.mvc.*;

public class ObjectInfoView extends DefaultView {

	/** Das Hauptpanel der ObjectView */
	private JPanel mainPanel = null;
	
	/** Die Id des Objekts */
	private JLabel id = null;
	
	/** Der Name des Objekts */
	private JLabel name = null;
	
	/** Sichtbarkeit des Objekts */
	private JLabel visible = null;
	
	private JLabel dynamic = null;
	
	private JLabel hard = null;
	
	private JLabel shootable = null;
	
	private JLabel lift = null;
	
	private JLabel switch_ = null;
	
	
	public ObjectInfoView(Model model) {
		super(model);
		
		mainPanel = new JPanel(new MigLayout());
		
		mainPanel.add(id = new JLabel("id:"), "wrap");
		mainPanel.add(name = new JLabel("name:"), "wrap");
		mainPanel.add(visible = new JLabel("visible:"), "wrap");
		mainPanel.add(dynamic = new JLabel("dynamic:"), "wrap");
		mainPanel.add(hard = new JLabel("hard:"), "wrap");
		mainPanel.add(shootable = new JLabel("shootable:"), "wrap");
		mainPanel.add(lift = new JLabel("lift:"), "wrap");
		mainPanel.add(switch_ = new JLabel("switch:"), "wrap");
	}
	
	@Override
	public Component getComponent() {
		return mainPanel;
	}
	
	@Override
	public MainModel getModel() {
		return (MainModel) super.getModel();
	}
	
	
	@Override
	public void update(EventAction event, Object arg) {
		if(event.equals(MainModel.OBJECTTREE_SELECTION_CHANGED)) {
			@SuppressWarnings("unchecked")
			List<ObjectTreeNode> selectedNodes = (List<ObjectTreeNode>)arg;
			if (selectedNodes != null && selectedNodes.size() == 1) {
				ComplexObjectMetaData metaData = (ComplexObjectMetaData) selectedNodes.get(0).getUserObject();
				id.setText("Id: " + ((ObjectMetaData) metaData).getId());
				name.setText("Name: " + metaData.getName());
				visible.setText("Visible: " + ((ObjectMetaData) metaData).isVisible());
				dynamic.setText("dynamic: " + ((ObjectMetaData) metaData).isDynamic());
				hard.setText("hard: " + ((ObjectMetaData) metaData).isHard());
				shootable.setText("shootable: " + ((ObjectMetaData) metaData).isShootable());
				lift.setText("lift: " + getModel().isLift(metaData));
				switch_.setText("switch: " + getModel().isSwitch(metaData));
			} else {
				id.setText("Id: Multiselektion");
				name.setText("Name: Multiselektion");
				visible.setText("visible: Multiselektion");
				dynamic.setText("dynamic: Multiselektion");
				hard.setText("hard: Multiselektion");
				shootable.setText("shootable: Multiselektion");
				lift.setText("lift: Multiselektion");
				switch_.setText("switch: Multiselektion");
			}
			mainPanel.validate();
		}
	}
}
