package server.udp;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerUDP {

	static final String ANMELDUNG = "ANMELDUNG";
	static final String ENDE = "ENDE";
	static int port = 1234;
	static int length = 256; // L�nge eines Pakets

	public static void main(String args[]) {
		DatagramPacket paket = new DatagramPacket(new byte[length], length);
		Vector<InetSocketAddress> clients = new Vector<InetSocketAddress>(); // Liste der Clients
		try {
			DatagramSocket socket = new DatagramSocket(port);
			for (;;) {
				// Warten auf n�chstes Paket
				socket.receive(paket);
				InetSocketAddress add = (InetSocketAddress) paket
						.getSocketAddress();
				// Text aus Paket extrahieren
				String text = new String(paket.getData(), 0, paket.getLength());
				System.out.println(add + ">" + text);
				// Paket auswerten
				if (text.equals(ANMELDUNG)) {
					clients.add(add);
					System.out.println("Anzahl Clients: " + clients.size());
				} else if (text.equals(ENDE)) {
					clients.remove(add);
					System.out.println("Anzahl Clients: " + clients.size());
				} else {
					// Versenden von Kopien an alle anderen Clients
					for (int i = 0; i < clients.size(); i++) {
						InetSocketAddress dest = clients
								.get(i);
						if (!dest.equals(add)) {
							paket.setSocketAddress(dest);
							socket.send(paket);
							System.out.println("Kopie an " + dest);
						}
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Ausnahmefehler: " + e);
		}
	}
}
