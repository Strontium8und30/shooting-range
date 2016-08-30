package client.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import net.miginfocom.swing.*;
import utilities.mvc.*;
import client.*;

import common.*;

public class ChatView extends DefaultView {

	TransPanel panel;

	TransPanel panelMessage;
	
	TransTextfield inputField;
	
	public ChatView(Model model) {
		super(model);
		
		Action sendMessage = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				executeCommand();
			}
		};
		
		panel = new TransPanel(TransPanel.TRANSPARENT);
		panel.setLayout(new MigLayout("insets 0 0 0 0", "[grow, fill][]", "[grow, fill]10[][]"));

		panelMessage = new TransPanel();
		
		inputField = new TransTextfield();
		inputField.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					executeCommand();
				}
			}
		});
		
		panel.add(new TransScrollPanel(panelMessage), "span, wrap");		
		panel.add(inputField, "spany, grow");
		panel.add(new TextButton("Senden", 40, sendMessage));
	}

	public void executeCommand() {
		JLabel text = new JLabel(inputField.getText());
		inputField.setText("");

		text.setFont(new Font("Courier New", Font.PLAIN, 14));
		text.setForeground(Color.WHITE);
		panelMessage.add(text, "wrap");
		if (text.getText().startsWith("connect to")) {
			((ClientModel)getModel()).createSocket(text.getText().substring(text.getText().lastIndexOf(" ")+1), 1234);
		} else if (text.getText().startsWith("change name to")) {
			((ClientModel)getModel()).sendData(DataType.NAME, text.getText().substring(text.getText().lastIndexOf(" ")+1));
		} else {
			((ClientModel)getModel()).sendData(DataType.MESSAGE, text.getText());
		}				
	}
	
	@Override
	public Component getComponent() {
		return panel;
	}
	
	@Override
	public void update(EventAction event, Object arg) {
		if(event.equals(ClientModel.RECEIVE_MESSAGE)) {
			JLabel text = new JLabel(arg.toString());
			text.setFont(new Font("Courier New", Font.PLAIN, 14));
			text.setForeground(Color.WHITE);
			panelMessage.add(text, "wrap");
			panelMessage.validate();
		}
	}
}
