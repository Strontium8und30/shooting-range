package server.tcp;

import java.io.*;

import common.*;

import utilities.log.*;

public class ClientConnector extends Thread {
	
	/** Logging */
	public static Log log = LogFactory.getLog(ClientConnector.class);
	
	/** ServerModel */
	private ServerModel serverModel;
		
	
	public ClientConnector(ServerModel serverModel) {
		this.serverModel = serverModel;
		log.debug("Warte auf neue Clients...");
		start();
	}

	@Override
	public void run() {
		while(true) {
			try {
				Client clientSocket = new Client(serverModel.getServerSocket().accept(), serverModel.getValidClientId());
				serverModel.addClient(clientSocket);
				log.info("Ein neuer Client wurde aufgenommen");
			} catch (IOException e) {
				log.error("Fehler bei der Aufnahme eines neuen Clients", e);
			}
		}
	}
}
