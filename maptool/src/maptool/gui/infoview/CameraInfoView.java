package maptool.gui.infoview;

import java.awt.*;

import javax.swing.*;

import maptool.control.*;
import maptool.gui.mainFrame.*;
import net.miginfocom.swing.*;

import utilities.mvc.*;

public class CameraInfoView extends DefaultView {
	
	/** Das Hauptpanel der CameraInfoView */
	private JPanel mainPanel = null;
	
	private JLabel xAchse = null;
	
	private JLabel yAchse = null;
	
	private JLabel zAchse = null;
	
	private JLabel xAchseAng = null;
	
	private JLabel zAchseAng = null;

	
	public CameraInfoView(Model model) {
		super(model);
		
		mainPanel = new JPanel(new MigLayout("","[]","[][][][][]"));
		xAchse = new JLabel("X-Achse: " + new Float(Controller.getCamera().getPosition().x).toString());
		yAchse = new JLabel("Y-Achse: " + new Float(Controller.getCamera().getPosition().y).toString());
		zAchse = new JLabel("Z-Achse: " + new Float(Controller.getCamera().getPosition().z).toString());
		xAchseAng = new JLabel("Vertikal-Dreh: " + new Float(Controller.getCamera().getAngleVertical()).toString());
		zAchseAng = new JLabel("Horizontal-Dreh: " + new Float(Controller.getCamera().getAngleHorizontal()).toString());
		
		mainPanel.add(xAchse, "cell 0 0");
		mainPanel.add(yAchse, "cell 0 1");
		mainPanel.add(zAchse, "cell 0 2");
		mainPanel.add(xAchseAng, "cell 0 3");
		mainPanel.add(zAchseAng, "cell 0 4");
	}

	@Override
	public Component getComponent() {
		return mainPanel;
	}
	
	@Override
	public void update(EventAction event, Object arg) {
		if(event.equals(MainModel.GLVIEW_REPAINT)) {
			xAchse.setText("X-Achse: " + new Float(Controller.getCamera().getPosition().x).toString());
			yAchse.setText("Y-Achse: " + new Float(Controller.getCamera().getPosition().y).toString());
			zAchse.setText("Z-Achse: " + new Float(Controller.getCamera().getPosition().z).toString());
			xAchseAng.setText("Vertikal-Dreh: " + new Float(Controller.getCamera().getAngleVertical()).toString());
			zAchseAng.setText("Horizontal-Dreh: " + new Float(Controller.getCamera().getAngleHorizontal()).toString());
		}
	}
}
