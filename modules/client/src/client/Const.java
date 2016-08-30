package client;

import java.io.*;

import framework.types.*;


public class Const {

	/** Maussensitivität */
	public final static float MOUSE_SENSETIV = 0.1f; 
	
	/** Playermodel */
	public final static File PLAYER_MODEL = new File("models/player/player.xml");
	
	/** Startposition des Spielers */
	public final static Vector3D PLAYER_STARTPOS = new Vector3D(0.0f, 2.0f, 0.0f);
	
	/** Frames pro Sekunde */
	public static float FRAME_RATE = 50;

}
