package client.gui;

import java.awt.*;

import javax.swing.*;

public class TransScrollPanel extends JScrollPane {
		
	public TransScrollPanel(JComponent transPanel) {
		super(transPanel);
		setBorder(null);
		setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
	}		
}
