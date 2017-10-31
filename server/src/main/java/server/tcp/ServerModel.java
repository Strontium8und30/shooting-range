package server.tcp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import common.Client;
import common.DataType;
import common.NetModel;
import utilities.log.Log;
import utilities.log.LogFactory;
import utilities.mvc.View;

public class ServerModel extends NetModel {

	/** Logging */
	public static Log log = LogFactory.getLog(ServerModel.class);
	
	/** Der ServerSocket */
	private ServerSocket serverSocket;
		
	/** Nächste gültige Client Id */
	private int nextValidClientId;
	
	/** Liste der zur Zeit verbundenen Clients */
	private List<Client> clients = new ArrayList<Client>();
	
	
	public ServerModel() {
		createServerSoket(1234);
	}
	
	public void createServerSoket(int port) {
		try {
			serverSocket = new ServerSocket(port);			
			new ClientConnector(this);
		} catch (IOException e) {
			log.error("Fehler beim erstellen des ServerSockets", e);
		}
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public synchronized void addClient(Client client) {
		clients.add(client);
		new ClientDataReceiver(this, client);
		sendData(client, DataType.ID, client.getID());
		
		for (Client c : clients) {
			sendData(client, DataType.CLIENT_ADD, c.getID());
		}
		
		sendToAll(DataType.CLIENT_ADD, client.getID());
		notifyViews(CLIENT_ADD, client);
		sendToAll(DataType.MESSAGE, "Server: " + client.getName() + " hat die Lobby betreten.");
	}
	
	public synchronized void removeClient(Client client) {
		try {
			client.getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		clients.remove(client);
		notifyViews(CLIENT_REMOVE, client);
		sendToAll(client, DataType.MESSAGE, "Server: " + client.getName() + " hat die Lobby verlassen.");
	}
	
	public synchronized Client getClient(int index) {
		return clients.get(index);
	}
	
	public synchronized List<Client> getClients() {
		return clients;
	}
	
	public synchronized void sendData(Client client, DataType dataType, Object data) {
		try {
			DataOutputStream out = client.getOutputStream();			
			if (DataType.CLIENT_ADD.equals(dataType) || DataType.ID.equals(dataType)) {
				out.writeInt(dataType.getTypeID());
				out.writeInt((Integer)data);
			} else if (DataType.POSITION.equals(dataType)) {
				for (Client c : clients) {
					if (c.getID() == client.getID()) continue;
					out.writeInt(dataType.getTypeID());
					out.writeInt(c.getID());
					out.writeFloat(c.getPosition().getX());
					out.writeFloat(c.getPosition().getY());
					out.writeFloat(c.getPosition().getZ());
					out.writeFloat(c.getAngleHorizontal());
					out.writeFloat(c.getAngleVertical());
				}
			} else if (DataType.PLAYER_SHOOT.equals(dataType)) {
				out.writeInt(dataType.getTypeID());
				out.writeInt((Integer)data);
			} else if (DataType.WEAPON_CHANGE.equals(dataType)) {
				out.writeInt(dataType.getTypeID());
				out.writeInt(((Client)data).getID());
				out.writeInt(((Client)data).getWeapon().getId());
			} else if (DataType.MESSAGE.equals(dataType)) {
				out.writeInt(dataType.getTypeID());
				out.writeBytes((String)data + '\n');
			} else {
				log.error("Kein entsprechender Datentyp beim senden (Server)");
			}
			out.flush();
		} catch (IOException e) {
			log.warning("Ein Client (" + client + ") ist nicht mehr erreichbar.");
			removeClient(client);
		}
	}
	
	public void sendToAll(DataType dataType, Object data) {
		sendToAll(null, dataType, data);
	}
	
	public void sendToAll(Client exludeClient, DataType dataType, Object data) {
		for (int i = 0; i < getClients().size(); i++) {
			Client client = getClient(i);
			if (client != exludeClient) {
				sendData(client, dataType, data);
			}
		}
	}
	
	public int getValidClientId() {
		return ++nextValidClientId;
	}
	
	@Override
	public View createView() {
		return new ServerView(this);
	}
}
