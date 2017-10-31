package client.gui;

import java.awt.*;

import javax.swing.*;

public class TransScrollPanel extends JScrollPane {
		
	public TransScrollPanel(JComponent transPanel) {
		super(transPanel);
		setBorder(null);
		setOpaque(false);
		getViewport().setOpaque(false);
	}		
}
