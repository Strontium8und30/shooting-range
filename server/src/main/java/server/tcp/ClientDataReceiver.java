package server.tcp;

import java.io.DataInputStream;
import java.io.IOException;

import common.Client;
import common.DataType;
import framework.types.Vector3D;
import framework.weapons.Weapon;
import utilities.log.Log;
import utilities.log.LogFactory;

public class ClientDataReceiver extends Thread {

	/** Logging */
	public static Log log = LogFactory.getLog(ClientDataReceiver.class);
	
	private ServerModel serverModel;
	
	private Client client;
	
	public ClientDataReceiver(ServerModel serverModel, Client client) {
		this.serverModel = serverModel;	
		this.client = client;
		start();
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				receiveData(client);
			}
		} catch (Exception e) {
			log.error("Fehler beim lesen der Daten", e);
			serverModel.removeClient(client);
		}
	}
	
	private void receiveData(Client client) throws IOException  {
		DataInputStream in = client.getInputStream();
		DataType type = DataType.getTypeByID(in.readInt());			
		if (DataType.NAME.equals(type)) {
			client.setName(in.readLine());
			serverModel.sendData(client, DataType.MESSAGE, "Server: Hallo " + client.getName());
			serverModel.sendToAll(client, DataType.MESSAGE, "Server: " + client.getName() + " hat die Lobby betreten.");
		} else if (DataType.POSITION.equals(type)) {
			Vector3D position = new Vector3D();
			position.setX(in.readFloat());
			position.setY(in.readFloat());
			position.setZ(in.readFloat());
			client.setPosition(position);				
			client.setAngleHorizontal(in.readFloat());
			client.setAngleVertical(in.readFloat());
		} else if (DataType.PLAYER_SHOOT.equals(type)) {
			serverModel.sendToAll(client, DataType.PLAYER_SHOOT, client.getID());
		} else if (DataType.WEAPON_CHANGE.equals(type)) {
			client.setWeapon(Weapon.getById(in.readInt()));
			serverModel.sendToAll(DataType.WEAPON_CHANGE, client);
		} else if (DataType.CONNECTION_CLOSE.equals(type)) {
			serverModel.removeClient(client);
		} else if (DataType.SEND_DATA.equals(type)) {
			serverModel.sendData(client, DataType.POSITION, null);
		} else {
			log.error("Kein entsprechender Datentyp beim empfangen (Server)");
		}
		serverModel.notifyViews(ServerModel.CLIENT_CHANGE, client);
	}	
}
