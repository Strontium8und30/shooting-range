package server;

import server.tcp.*;

public class Server {

	public static void main(String[] args) {
		ServerModel serverModel = new ServerModel(); 
		serverModel.createView();
		
		try { Thread.sleep(5000); } catch (Exception e) {}
	}
}
