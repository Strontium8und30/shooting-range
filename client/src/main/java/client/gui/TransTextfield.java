package client.gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

public class TransTextfield extends JTextArea {

	public TransTextfield() {
		setBorder(new LineBorder(Color.WHITE));
		setFont(new Font("Courier New", Font.PLAIN, 14));
		setBackground(new Color(0.0f, 0.0f, 0.0f, 0.4f));
		setForeground(new Color(1.0f, 1.0f, 1.0f));
	}
	
}
