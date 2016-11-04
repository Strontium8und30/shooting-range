package client.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import utilities.*;

public class TextButton extends JButton {
	
	Fader fader;
	
	public TextButton(String text) {
		this(text, null);
	}	
	
	public TextButton(String text, Action action) {
		this(text, 90.0f, action);
	}
	
	public TextButton(String text, float size, Action action) {
		super(text);
		setFocusPainted(false);
		setBorderPainted(false);
		setForeground(Color.WHITE);
		setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
		setFont(Utilities.getFont("Blackletter.ttf").deriveFont(size));
		
		if(action != null) addActionListener(action);
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}			
		});
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				fader = new Fader();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				fader.exit();
			}
		});
	}
	
	public class Fader extends Thread {

		boolean runnig = true;
		float fadeMin = 0.60f;
		float fadeMax = 1.0f;
		float fade = fadeMax;
		float add = -0.05f;
		
		public Fader() {
			start();
		}
		
		public void run() {
			while(runnig || fade != 1.0f) {
				try {
					Thread.sleep(60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (!runnig) add = add > 0 ? add : -add;
				if (fade >= fadeMax) add = add < 0 ? add : -add;
				if (fade <= fadeMin) add = add > 0 ? add : -add;
				setForeground(new Color(fade, fade, fade));
				fade += add;
			}
			setForeground(Color.WHITE);
		}
		
		public void exit() {
			runnig = false;
		}
	}
}
