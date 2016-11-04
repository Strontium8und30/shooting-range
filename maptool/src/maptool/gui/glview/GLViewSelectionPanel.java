package maptool.gui.glview;

import javax.swing.*;

import net.miginfocom.swing.*;
import utilities.log.*;
import utilities.mvc.*;

public class GLViewSelectionPanel extends DefaultView {

	/** Logging */
	public static Log log = LogFactory.getLog(GLViewSelectionPanel.class);
	
	/** Das Sammelpanel für die 4 GLViews */
	private JPanel pane = null;
	
	
	GLView glView3D;
	GLView glViewFront;
	GLView glViewTop;
	GLView glViewSide;
	
	JPanel glView3DPanel;
	JPanel glViewFrontPanel;
	JPanel glViewTopPanel;
	JPanel glViewSidePanel;

	
	public GLViewSelectionPanel(Model model) {
		super(model);
		
		pane = new JPanel();
		pane.setLayout(new MigLayout("", "[grow, fill]10[grow, fill]", "[grow, fill]10[grow, fill]"));

		glView3D = new GLView3D(model);
		glViewFront = new GLViewFront(model);
		glViewTop = new GLViewTop(model);
		glViewSide = new GLViewSide(model);

		glView3DPanel = glView3D.getComponent();
		glViewFrontPanel = glViewFront.getComponent();
		glViewTopPanel = glViewTop.getComponent();
		glViewSidePanel = glViewSide.getComponent();
		
		pane.add(glView3DPanel, "cell 0 0, grow");
		pane.add(glViewFrontPanel, "cell 1 0, grow");
		pane.add(glViewTopPanel, "cell 1 1, grow");
		pane.add(glViewSidePanel, "cell 0 1, grow");
	}
	
	@Override
	public JPanel getComponent() {
		return this.pane;
	}

	@Override
	public void update(EventAction event, Object arg) {
		
	}
}
