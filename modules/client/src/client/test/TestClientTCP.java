package client.test;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import client.*;

import net.miginfocom.swing.*;
import utilities.log.*;
import utilities.mvc.*;

import common.*;

class TestClientTCP extends DefaultView {

	/** Logging */
	public static Log log = LogFactory.getLog(TestClientTCP.class);
	
	JFrame frame;
	
	JTextField clientName;
	
	JTextField clientAddress;
	
	JTextField textField;
	
	JPanel defaultList;
	
	public TestClientTCP(Model model) {
		super(model);
		
		JFrame frame = new JFrame("Client");
		   frame.setSize(600,200);
		   frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				((ClientModel)getModel()).closeConnection();
				System.exit(0);
			}
		   });
		   frame.setLayout(new MigLayout("","[grow,fill][grow,fill]","[][][][][grow,fill]"));
		   frame.add(new JLabel("IP-Adresse:"));
		   frame.add(clientAddress = new JTextField("10.78.21.36"), "wrap");
		   frame.add(new JLabel("Name:"));
		   frame.add(clientName = new JTextField(), "wrap");
		   JButton verbinden = new JButton("Verbinden");
		   verbinden.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					((ClientModel)getModel()).createSocket(clientAddress.getText(), 1234);
					((ClientModel)getModel()).sendData(DataType.NAME, clientName.getText());
				}
		   });
		   frame.add(verbinden, "span, wrap");
		   textField = new JTextField();
		   frame.add(textField);
		   JButton butten = new JButton("Senden");
		   frame.add(butten, "wrap");
		   defaultList = new JPanel(new MigLayout());
		   defaultList.setBackground(Color.WHITE);
		   defaultList.setBorder(new LineBorder(Color.BLACK));
		   frame.add(new JScrollPane(defaultList), "span, grow");
		   
		   butten.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			      try {
			    	  ((ClientModel)getModel()).sendData(DataType.MESSAGE, textField.getText());
			       } catch(Exception e) {
			     	  log.error("Client fehler", e);
			       }
			}
		   });
		   frame.setVisible(true);
	}
	
	@Override
	public Component getComponent() {
		return frame;
	}
	
	@Override
	public void update(EventAction event, Object arg) {
		if(event.equals(ClientModel.RECEIVE_MESSAGE)) {
			JLabel text = new JLabel((String)arg);
			defaultList.add(text, "wrap");
			defaultList.getParent().validate();
		}
	}
	
	public static void main(String args[]) {
		ClientModel clientModel = new ClientModel() {
			@Override
			public View createView() {
				return new TestClientTCP(this);
			}
		};
		clientModel.createView();
	}
}


