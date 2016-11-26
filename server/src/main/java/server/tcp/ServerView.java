package server.tcp;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;

import common.*;

import net.miginfocom.swing.*;
import utilities.mvc.*;

public class ServerView extends DefaultView  {

	/** Der Frame zur Steuerung des Servers */
	private JFrame frame;
	
	private DefaultTableModel tableModel;
	
	public ServerView(Model model) {
		super(model);
		
		frame =  new JFrame("Spiel Server Application");
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new MigLayout("","[grow, fill]","[grow, fill]"));

		tableModel = new DefaultTableModel(new Object[][]{}, new Object[]{"Id", "Name", "Position", "IP-Adresse", "Port"});
		
		JTable clientTable = new JTable(tableModel) {
            public boolean isCellEditable(int x, int y) {
                return false;
            }
        };

		frame.add(new JScrollPane(clientTable));
		frame.setVisible(true);
	}
	
	@Override
	public Component getComponent() {
		return frame;
	}
	
	@Override
	public void update(EventAction event, Object arg) {
		if(event.equals(ServerModel.CLIENT_ADD)) {
			Client client = (Client)arg;
			tableModel.addRow(new Object[]{client.getID(), client.getName(), client.getPosition(), client.getSocket().getInetAddress(), client.getSocket().getPort()});
		} else if(event.equals(ServerModel.CLIENT_REMOVE)) {
			for (int row = 0; row < tableModel.getRowCount(); row++) {
				if (tableModel.getValueAt(row, 0).equals(((Client)arg).getID())) {
					tableModel.removeRow(row);
					break;
				}
			}
		} else if(event.equals(ServerModel.CLIENT_CHANGE)) {
			Client client = (Client)arg;
			for (int row = 0; row < tableModel.getRowCount(); row++) {
				if (tableModel.getValueAt(row, 0).equals(((Client)arg).getID())) {
					tableModel.setValueAt(client.getName(), row, 1);
					tableModel.setValueAt(client.getPosition(), row, 2);
					break;
				}
			}
			tableModel.fireTableDataChanged();
		}
	}
}
