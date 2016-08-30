package maptool.gui.infoview;

import static java.awt.GridBagConstraints.*;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import maptool.gui.mainFrame.*;
import utilities.log.*;
import utilities.mvc.*;

public class InfoView extends DefaultView {

	/** Logging */
	public static Log log = LogFactory.getLog(InfoView.class);
	
	/** TabPane zur Auswahl der gewünschten InfoView */
	private JTabbedPane infoTabPane = null;
	
	/** Das Anzeige Panel */
	private JPanel infoPanel = null;
	
	
	public InfoView(Model model) {
		super(model);
		
		infoTabPane = new JTabbedPane();
		infoTabPane.addTab("Übersicht", new JLabel("Übersicht"));
		infoTabPane.addTab("Kamera", new CameraInfoView(model).getComponent());
		infoTabPane.addTab("Objekt", new ObjectInfoView(model).getComponent());
		infoTabPane.addTab("View Options", new ViewOptionsView(model).getComponent());
		
		infoPanel = new JPanel();
		infoPanel.setLayout(new GridBagLayout());
		infoPanel.setBorder(new EtchedBorder());
		
		infoPanel.add(infoTabPane, 
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 
				CENTER, BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}

	@Override
	public JPanel getComponent() {
		return infoPanel;
	}

	@Override
	public void update(EventAction event, Object arg) {
		if(event.equals(MainModel.INFOVIEW_SHOW)) {
			infoPanel.setVisible((Boolean)arg);
			infoPanel.invalidate();
		}
	}
}
