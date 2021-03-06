package client.gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import net.miginfocom.swing.*;

public class TransPanel extends JPanel {

	/** Das Panel ist ganz Transparent */
	public static final int TRANSPARENT = 0;
	
	/** Das Panel �berdeckt alle anderen Komponenten zu 40%*/ 
	public static final int LOW_TRANSPARENT = 1;
	
	
	public TransPanel() {
		this(LOW_TRANSPARENT);
	}
	
	public TransPanel(int type) {
		setLayout(new MigLayout());
		if (type == TRANSPARENT) {
			setOpaque(false);
			setBorder(null);
		} else if (type == LOW_TRANSPARENT) {
			setOpaque(false);
			setBorder(new LineBorder(Color.WHITE));
		}
	}
}
