package common;

import java.io.*;
import java.net.*;

import framework.*;
import framework.types.*;
import framework.weapons.*;

public class Client {
	
	/** ClientSocket */
	private Socket socket;
	
	/** OutputStream */
	private DataOutputStream outputStream;
	
	/** InputStream */
	private DataInputStream inputStream;
	
	/** Die eindeutige ID des Clients */
	private int id;
	
	/** Der Name des Clients */
	private String name = "No Name";
	
	/** Spieler position */
	private Vector3D position = new Vector3D(0);
	
	/** Blickwinkel der Kamera Horizontal*/
	private float angleHorizontal = 0;

	/** Blickwinkel der Kamera Vertical*/
	private float angleVertical = 0;
	
	/** Waffe des Netzwerkspielers */
	private Weapon weapon = Weapon.NO_WEAPON;
	
	
	public Client(int id) {
		this.id = id;
	}
	
	public Client(Socket socket, int id) {		
		try {
			this.socket = socket;
			this.outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			this.inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			this.id = id;
		} catch (IOException e) {
			System.out.println("Fehler " + e);
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public DataOutputStream getOutputStream() {
		return outputStream;
	}

	public DataInputStream getInputStream() {
		return inputStream;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Vector3D getPosition() {
		return position;
	}

	public void setPosition(Vector3D position) {
		this.position = position;
	}

	public float getAngleHorizontal() {
		return angleHorizontal;
	}
	
	public void setAngleHorizontal(float angle) {
		this.angleHorizontal = angle;
	}
	
	public float getAngleVertical() {
		return angleVertical;
	}
	
	public void setAngleVertical(float angle) {
		this.angleVertical = angle;
	}
	
	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Player) {
			return ((Player) obj).getPosition().equals(position) && 
				   ((Player) obj).getCamera().getAngleHorizontal() == angleHorizontal &&
				   ((Player) obj).getCamera().getAngleVertical() == angleVertical;
		}
		return super.equals(obj); 
	}
	
	@Override
	public String toString() {
		return id + "  " + name + "  Addr: " + socket.getInetAddress() + "  Port: " + socket.getPort();
	}
}
