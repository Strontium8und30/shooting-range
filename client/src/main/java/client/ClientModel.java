package client;

import java.io.*;
import java.net.*;
import java.util.*;

import utilities.log.*;
import utilities.mvc.*;
import client.control.*;
import client.gui.*;
import client.player.*;

import common.*;

import framework.*;
import framework.types.*;
import framework.weapons.*;

public class ClientModel extends NetModel {

	/** Logging */
	public static Log log = LogFactory.getLog(ClientModel.class);
	
	private DataReceiver dataReceiver;
	
	private DataInputStream in;
	
	private DataOutputStream out;
	
	/** Socket */
	private Socket socket;
	
	/** Die eindeutige Id des Clients */
	private int clientId;
	
	/** Alle Netzwerk Mitspieler */
	private Map<Integer, NetClient> clients = new HashMap<Integer, NetClient>();
	
	
	public ClientModel() {
		this("127.0.0.1", 1234);
	}
	
	public ClientModel(String address, int port) {
		createSocket(address, port);
	}

	
	public void createSocket(String address, int port) {
		try {
			log.info("Versuche Verbindung zu " + address);
			socket = new Socket(address, port);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			dataReceiver = new DataReceiver(this);
		} catch (Exception e) {
			log.warning("createSocket() Zur Zeit ist keine Verbindung möglich.");
			notifyViews(RECEIVE_MESSAGE, "Es kann zur Zeit keine verbindung zum Server aufgebaut werden.");
		}
	}
	
	public void closeSockte() {
		try {
			if (socket != null) socket.close();
		} catch (IOException e) {
			log.error("Fehler beim schließen des Sockets", e);
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public void closeConnection() {
		sendData(DataType.CONNECTION_CLOSE, null);
		closeSockte();
	}
	
	public void stopNetworkLoop() {
		dataReceiver.stopNetworkLoop();
	}
		
	public void receiveData() throws IOException {
		DataType type = DataType.getTypeByID(in.readInt());
		
		if (DataType.ID.equals(type)) {
			clientId = in.readInt();
			notifyViews(RECEIVE_MESSAGE, "Neue ID erhalten: " + clientId);
		} else if (DataType.POSITION.equals(type)) {			
			Client client = clients.get(in.readInt());
			if (client == null) {
				log.warning("Client noch nicht bekannt!");
				return;
			}
			Vector3D position = new Vector3D();
			position.setX(in.readFloat());
			position.setY(in.readFloat());
			position.setZ(in.readFloat());
			client.setPosition(position);
			client.setAngleHorizontal(in.readFloat());
			client.setAngleVertical(in.readFloat());
		} else if (DataType.CLIENT_ADD.equals(type)) {
			NetClient client = new NetClient(in.readInt());
			clients.put(client.getID(), client);
			log.info("Neuer Client Hinzugefügt: " + client.getID());
		} else if (DataType.CLIENT_REMOVE.equals(type)) {
			clients.remove(in.readInt());
		} else if (DataType.PLAYER_SHOOT.equals(type)) {
			ShootController sc = ShootController.getInstance();
			Client client = clients.get(in.readInt());				
			sc.shoot(client.getPosition(), Camera.getAngleAsVertex(client.getAngleHorizontal(), client.getAngleVertical()), client.getWeapon());
		} else if (DataType.WEAPON_CHANGE.equals(type)) {
			Client client = clients.get(in.readInt());
			client.setWeapon(Weapon.getById(in.readInt()));
		} else if (DataType.MESSAGE.equals(type)) { 
			notifyViews(ClientModel.RECEIVE_MESSAGE, in.readLine());
		} else {
			log.error("Kein entsprechender Datentyp beim empfangen (Client)" + type.getTypeID());
		}
	}
	
	public void sendData(DataType dataType, Object data) {
		if (socket == null || socket.isClosed()) return;
		try {			
			if (DataType.POSITION.equals(dataType) && clients.get(clientId) != null) {
				if (clients.get(clientId).equals((PlayerImpl)data)) {
					return;
				}
			}
			out.writeInt(dataType.getTypeID());
			if (DataType.POSITION.equals(dataType)) {
				PlayerImpl player = (PlayerImpl)data;
				out.writeFloat(player.getPosition().getX());
				out.writeFloat(player.getPosition().getY());
				out.writeFloat(player.getPosition().getZ());				
				out.writeFloat(player.getCamera().getAngleHorizontal());
				out.writeFloat(player.getCamera().getAngleVertical());
			} else if (DataType.NAME.equals(dataType)) {
				out.writeBytes((String)data + '\n');
			} else if (DataType.PLAYER_SHOOT.equals(dataType)) {
				
			} else if (DataType.WEAPON_CHANGE.equals(dataType)) {
				out.writeInt(((Weapon)data).getId());
			} else if (DataType.SEND_DATA.equals(dataType)) {
				
			} else {
				log.error("Kein entsprechender Datentyp beim senden (Client)" + dataType.getTypeID());
			}
			out.flush();
		} catch (IOException e) {
			log.error("Der Server ist nicht mehr erreichbar.", e);
			closeSockte();
			notifyViews(RECEIVE_MESSAGE, "Die Verbindung zum Server wurde unterbrochen. Bitte erneut verbinden.");
		}
	}
	
	public int getClientId() {
		return clientId;
	}

	public Map<Integer, NetClient> getClients() {
		return clients;
	}

	public void setClients(Map<Integer, NetClient> clients) {
		this.clients = clients;
	}

	@Override
	public View createView() {
		return new MainMenuFrame(this);
	}
}
