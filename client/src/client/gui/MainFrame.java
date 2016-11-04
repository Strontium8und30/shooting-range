package client.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

import net.miginfocom.swing.*;
import utilities.log.*;
import utilities.mvc.*;
import client.*;

import common.*;

import framework.*;

public class MainFrame extends DefaultView {
		
	/** Logging */
	public static Log log = LogFactory.getLog(MainFrame.class);
	
	JFrame mainFrame;
	
	ImagePanel bgPanel = null;
	
	float fade = 0.0f;
	
	public MainFrame(Model model) {
		super(model);
		
		mainFrame = new JFrame("Spiel");
		Toolkit toolkit = mainFrame.getToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		
		mainFrame.setUndecorated(true);
		mainFrame.setResizable(false);
		mainFrame.setSize(screenSize);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		
		bgPanel = new ImagePanel("gfx/main_menue_bg.jpg");
		mainFrame.setContentPane(bgPanel);
		
		mainFrame.setLayout(new MigLayout("insets 50 50 50 50", "[]75[grow, fill]", "[grow, fill]50[180:200:220,fill]"));
		
		TransPanel panel = new TransPanel();
		panel.setLayout(new MigLayout("", "[]", "[]30[]30[]75[]"));
		
		Action act = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				((ClientModel)getModel()).stopNetworkLoop();
				GameFrame gameFrame = new GameFrame((ClientModel)getModel());
				gameFrame.setVisible(true);
				mainFrame.setVisible(false);
			}			
		};
		
		panel.add(new TextButton("Einzelspieler", act), "cell 0 0");
		panel.add(new TextButton("Mehrspieler", act), "cell 0 1");
		panel.add(new TextButton("Einstellungen"), "cell 0 2");
		panel.add(new TextButton("Beenden", Actions.ACTION_QUIT), "cell 0 3");
		
		mainFrame.add(panel, "cell 0 0 1 3");	
		JTabbedPane tb = new JTabbedPane();
		tb.setBackground(new Color(0.0f,0.0f,0.0f,0.4f));
		tb.setForeground(new Color(1.0f,1.0f,1.0f));
		tb.setFont(new Font("Courier New", Font.PLAIN, 14));
		
		
		DefaultTableModel tableModel = new DefaultTableModel(new Object[][]{{"sdfsad","sdafasd","adsfa","sadf"}}, new Object[]{"Id", "Name", "IP-Adresse", "Port"});
		
		for (int x = 0; x < 100; x++)
		tableModel.addRow(new Object[]{"sdfa","dsagas","asdf","sdfads"});
		JTable clientTable = new JTable(tableModel) {
            public boolean isCellEditable(int x, int y) {
                return false;
            }
        };
        clientTable.setGridColor(new Color(0.0f,0.0f,0.0f,0.0f));
        clientTable.setBackground(new Color(0.0f,0.0f,0.0f,0.0f));
        clientTable.setForeground(new Color(1.0f,1.0f,1.0f));
        clientTable.setFont(new Font("Courier New", Font.PLAIN, 14));
        clientTable.setBorder(null);
        clientTable.getTableHeader().setReorderingAllowed(false) ;
        JTableHeader header = clientTable.getTableHeader();
        header.setBackground(new Color(0.0f,0.0f,0.0f,0.4f));
        header.setForeground(new Color(1.0f,1.0f,1.0f));
        header.setFont(new Font("Courier New", Font.BOLD, 14));
        header.setBackground(new Color(0.0f,0.0f,0.0f,0.0f));
		tb.addTab("Spieler Online", new TransScrollPanel(clientTable));
		tb.addTab("aktuelle Spiele", new JLabel("Alle Spiele"));
		mainFrame.add(tb, "cell 1 0 1 1");

		View chatView = new ChatView(getModel());
		mainFrame.add(chatView.getComponent(), "cell 1 1 1 1");		
		mainFrame.setVisible(true);
		
		((ClientModel)getModel()).createSocket("127.0.0.1", 1234);
		((ClientModel)getModel()).sendData(DataType.NAME, "Thorben");
	}

	@Override
	public Component getComponent() {
		return mainFrame;
	}
}
