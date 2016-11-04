package maptool.gui.mainFrame;

import javax.swing.*;

import maptool.gui.glview.*;
import maptool.gui.infoview.*;
import net.miginfocom.swing.*;
import utilities.log.*;
import utilities.mvc.*;

public class MainFrame extends DefaultView {
	
	public static Log log = LogFactory.getLog(MainFrame.class);
	
	/** Der Hauptframe */
	JFrame frame = null;
	
	/** Linkes Panel Baum und Info */
	JPanel leftPanel = null;
	
	/** Rechtes Panel GLViews */
	JPanel rightPanel = null;
	
	/** Der ObjectBaum Navigator */
	View objectNavigatorView = null;
	
	/** Info View */
	View infoView = null;
	
	/** TabPane zum Auswählen der GL View */
	View glView;
	
	/** Die Aktuelle GL View */
	JPanel glViewPanel = null;
	
	public MainFrame(Model model) {
		super(model);
		
		frame = new JFrame("Spiel >> Maptool");

		frame.setSize(1024, 768);
		frame.setLayout(new MigLayout("", "[grow, fill]", "[grow, fill]"));
		frame.setJMenuBar(new MainMenu(model));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		leftPanel = new JPanel();
		leftPanel.setLayout(new MigLayout("", "[:200:, grow, fill]", "[grow, fill]"));
		rightPanel = new JPanel();
		rightPanel.setLayout(new MigLayout("", "[grow, fill]", "[grow, fill]"));
		
		objectNavigatorView = new ObjectNavigatorView(model);		
		infoView = new InfoView(model);		
		glView = new GLViewSelectionPanel(model);				
//		glView = new GLViewTabbedSelectionPanel(model);
		
		JSplitPane naviSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, objectNavigatorView.getComponent(), infoView.getComponent());
		leftPanel.add(naviSplitPane, "grow");
		
		rightPanel.add(glView.getComponent(), "grow");
		
		frame.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel));		
		
		frame.setVisible(true);
	}
	
	@Override
	public JFrame getComponent() {
		return frame;
	}
	
	@Override
	public void update(EventAction event, Object arg) {
		if(event.equals(MainModel.MAINMENU_RELOAD)) {
			frame.setJMenuBar(new MainMenu(getModel()));
			frame.validate();
		}
	}
}
