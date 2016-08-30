package server.tcp;

import java.io.*;
import java.net.*;
import java.util.*;

import utilities.log.*;
import utilities.mvc.*;

import common.*;

import framework.types.*;
import framework.weapons.*;

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
			new DataReceiver(this);
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
		sendData(client, DataType.ID, client.getID());
//		for (Client c : clients) {
////			if (c.equals(client)) continue;
//			sendData(client, DataType.CLIENT_ADD, c.getID());
//		}
		sendToAll(DataType.CLIENT_ADD, client.getID());
		notifyViews(CLIENT_ADD, client);
	}
	
	public synchronized void removeClient(Client client) throws IOException {
		client.getSocket().close();
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
	
	public synchronized void receiveData(Client client) {
		try {
			DataInputStream in = new DataInputStream(client.getInputStream());
			DataType type = DataType.getTypeByID(in.readInt());			
			if (DataType.NAME.equals(type)) {
				client.setName(in.readLine());
				sendData(client, DataType.MESSAGE, "Server: Hallo " + client.getName());
				sendToAll(client, DataType.MESSAGE, "Server: " + client.getName() + " hat die Lobby betreten.");
			} else if (DataType.POSITION.equals(type)) {
				Vector3D position = new Vector3D();
				position.setX(in.readFloat());
				position.setY(in.readFloat());
				position.setZ(in.readFloat());
				client.setPosition(position);				
				client.setAngleHorizontal(in.readFloat());
				client.setAngleVertical(in.readFloat());
			} else if (DataType.PLAYER_SHOOT.equals(type)) {
				sendToAll(client, DataType.PLAYER_SHOOT, client.getID());
			} else if (DataType.WEAPON_CHANGE.equals(type)) {
				client.setWeapon(Weapon.getById(in.readInt()));
				sendToAll(DataType.WEAPON_CHANGE, client);
			} else if (DataType.CONNECTION_CLOSE.equals(type)) {
				removeClient(client);
			} else if (DataType.SEND_DATA.equals(type)) {
				sendData(client, DataType.POSITION, null);
			} else {
				log.error("Kein entsprechender Datentyp beim empfangen (Server)");
			}
			notifyViews(CLIENT_CHANGE, client);
		} catch (IOException e) {
			log.error("Fehler beim lesen der Daten", e);
		}
	}
	
	public synchronized void sendData(Client client, DataType dataType, Object data) {
		try {
			DataOutputStream out = new DataOutputStream(client.getOutputStream());
			log.info("sendData() " + out.size());
			
			if (DataType.CLIENT_ADD.equals(dataType) || DataType.ID.equals(dataType)) {
				out.writeInt(dataType.getTypeID());
				out.writeInt((Integer)data);
			} else if (DataType.POSITION.equals(dataType)) {
				for (Client c : clients) {
//					if (c.getID() == client.getID()) continue;
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
		} catch (IOException e) {
			log.warning("Ein Client (" + client + ") ist nicht mehr erreichbar.");
			try {
				removeClient(client);
			} catch (IOException e1) {
				log.error("Fehler beim schließen des Sockets.", e1);
			}
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
