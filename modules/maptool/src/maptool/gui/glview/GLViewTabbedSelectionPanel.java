package maptool.gui.glview;

import javax.swing.*;

import utilities.log.*;
import utilities.mvc.*;

public class GLViewTabbedSelectionPanel extends DefaultView {

	/** Logging */
	public static Log log = LogFactory.getLog(GLViewTabbedSelectionPanel.class);
	
	/** Das TabPane zur Auswahl der benötigten GL Ansicht */
	private JTabbedPane tabPane = null;
	
	
	GLView glView3D;
	GLView glViewFront;
	GLView glViewTop;
	GLView glViewSide;
	
	JPanel glView3DPanel;
	JPanel glViewFrontPanel;
	JPanel glViewTopPanel;
	JPanel glViewSidePanel;

	
	public GLViewTabbedSelectionPanel(Model model) {
		super(model);
		
		tabPane = new JTabbedPane();
		
		glView3D = new GLView3D(model);
		glViewFront = new GLViewFront(model);
		glViewTop = new GLViewTop(model);
		glViewSide = new GLViewSide(model);
		
		glView3DPanel = glView3D.getComponent();
		glViewFrontPanel = glViewFront.getComponent();
		glViewTopPanel = glViewTop.getComponent();
		glViewSidePanel = glViewSide.getComponent();

		tabPane.addTab("3D Ansicht", glView3DPanel);
		tabPane.addTab("Front Ansicht", glViewFrontPanel);
		tabPane.addTab("Top Ansicht", glViewTopPanel);
		tabPane.addTab("Side Ansicht", glViewSidePanel);
	}
	
	@Override
	public JTabbedPane getComponent() {
		return this.tabPane;
	}
}
