package utilities.swing;

import static java.awt.GridBagConstraints.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

public class PanelList {
	
	private List<JPanel> panels = new ArrayList<JPanel>();
	
	private JPanel mainPanel = new JPanel();
	
	private JPanel selectedPanel = null;
	
	private int selectedIndex = 0; 
	
	public PanelList() {
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent event) {
				if(event.getKeyCode() == 38) {
					if (selectedIndex > 0) selectedIndex--;
					setSelectedPanel(selectedIndex);
				}
				if(event.getKeyCode() == 40) {
					if (selectedIndex < mainPanel.getComponentCount()) selectedIndex++;
					setSelectedPanel(selectedIndex);
				}
			}			
		});
	}
	
	public void addPanel(JPanel panel) {
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				selectPanel((JPanel)event.getSource());
			}			
		});
		panels.add(panel);
		mainPanel.add(panel,
				new GridBagConstraints(1, mainPanel.getComponentCount(), 1, 1, 1.0, 0.0, 
				NORTH, HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));	
	}
	
	public void setSelectedPanel(int index) {
		selectPanel((JPanel)mainPanel.getComponent(index));
	}
	
	private void selectPanel(JPanel panel) {
		if (panels.contains(panel)) {
			if (selectedPanel != null) {
				selectedPanel.setBackground(null);
			} else {
				selectedPanel = new JPanel();
			}
			selectedPanel = panel;
			selectedPanel.setBackground(Color.LIGHT_GRAY);
			selectedIndex = panels.indexOf(panel);
		}
	}
		
	public JPanel getSelectedPanel() {
		return selectedPanel;
	}
	
	public JScrollPane getPanelList() {
		JScrollPane scrollPane = new JScrollPane(mainPanel);
		scrollPane.setAlignmentY(JScrollPane.TOP_ALIGNMENT);
		return scrollPane;
	}
}
