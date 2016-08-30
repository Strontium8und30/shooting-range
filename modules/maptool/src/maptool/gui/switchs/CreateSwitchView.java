package maptool.gui.switchs;

import java.awt.*;

import javax.swing.*;

import framework.object.interactive.*;
import framework.object.interactive.lift.*;
import net.miginfocom.swing.*;
import maptool.gui.mainFrame.*;
import maptool.object.*;
import utilities.mvc.*;

public class CreateSwitchView extends DefaultView  {

	private Switch switchi;
	
	private JDialog dialog = null;
	
	public CreateSwitchView(MainModel model, ComplexContainerObject cObj) {
		super(model);
		
		this.switchi = getModel().getSwitch(cObj).orElse(new LiftSwitch(cObj));
		
		dialog = new JDialog();
		dialog.setModal(false);
		dialog.setTitle("Texture bearbeiten");
		dialog.setSize(400, 500);
		dialog.setLayout(new MigLayout("","[][]"));
		
		dialog.add(new JLabel("Typ"));
		
		JComboBox<SwitchType> comboBox = new JComboBox<SwitchType>(SwitchType.values());
		comboBox.setSelectedItem(SwitchType.getSwitchType(switchi.getClass()));
		dialog.add(comboBox);
				
		final JPanel content = new JPanel();
		dialog.add(content, "span 2");
		
		comboBox.addActionListener(e -> {
			SwitchType seleSwitchType = (SwitchType) comboBox.getSelectedItem();
			switch (seleSwitchType) {
			case REQEUST_LIFT:
				content.removeAll();
				break;
			case USE_LIFT:
				content.removeAll();
				break;
			default:
				break;
			}
		});
		
		dialog.setVisible(true);
	}

	@Override
	public Component getComponent() {
		return dialog;
	}
	
	@Override
	public MainModel getModel() {
		return (MainModel) super.getModel();
	}
}
