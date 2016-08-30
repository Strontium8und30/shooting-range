package server.tcp;

import common.*;



public class DataReceiver extends Thread {
	
	private boolean newDataFlag = false;
	
	private ServerModel serverModel;
	
	public DataReceiver(ServerModel serverModel) {
		this.serverModel = serverModel;	
		start();
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				for (int i = 0; i < serverModel.getClients().size(); i++) {
					Client client = serverModel.getClient(i);
					if (client.getInputStream().available() != 0) {
						serverModel.receiveData(client);
						newDataFlag = true;
					}
				}
				Thread.sleep(10);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
