package client;


public class DataReceiver extends Thread {
	
	ClientModel clientModel;
	
	boolean running = true;
	
	public DataReceiver(ClientModel clientModel) {
		this.clientModel = clientModel;	
		start();
	}
	
	@Override
	public void run() {
		try {
			while(!clientModel.getSocket().isClosed() && running) {
				clientModel.receiveData();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stopNetworkLoop() {
		running = false;
	}
}
