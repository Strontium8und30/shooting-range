package maptool.util;

import javax.swing.JPanel;

import utilities.mvc.*;

public abstract class PanelView extends JPanel implements View {

	Model model;
	
	public PanelView(Model model) {
		this.model = model;
	}
	
	@Override
	public Model getModel() {
		return model;
	}
}