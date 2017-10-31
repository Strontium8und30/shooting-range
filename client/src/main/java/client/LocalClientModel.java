package client;

import java.io.*;
import java.net.*;
import java.util.*;

import client.player.*;
import common.*;

public class LocalClientModel extends ClientModel {
	
	public void createSocket(String address, int port) {
		
	}
	
	public void closeSockte() {
		
	}

	public Socket getSocket() {
		throw new UnsupportedOperationException();
	}

	public void setSocket(Socket socket) {
		
	}
	
	public void closeConnection() {
		
	}
	
	public void stopNetworkLoop() {
		
	}
	
	public synchronized void receiveData() throws IOException {
	
	}
	
	public synchronized void sendData(DataType dataType, Object data) {
		
	}
	
	public int getClientId() {
		return 0;
	}

	public Map<Integer, NetClient> getClients() {
		return new HashMap<Integer, NetClient>();
	}

	public void setClients(Map<Integer, NetClient> clients) {
		throw new UnsupportedOperationException();
	}	
}
