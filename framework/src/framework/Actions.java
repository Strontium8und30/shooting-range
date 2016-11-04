package framework;

import java.awt.event.*;

import javax.swing.*;

public class Actions {

	public static Action ACTION_QUIT = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}		
	};
	
}
