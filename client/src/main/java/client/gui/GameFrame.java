package client.gui;

import java.io.*;

import javax.swing.*;

import utilities.*;
import utilities.control.*;
import utilities.log.*;
import utilities.resource.*;
import client.*;

public class GameFrame extends JFrame {
		
	/** Logging */
	public Log log = LogFactory.getLog(GameFrame.class);
	
	/** Die zentrale Steuerung/Verwaltung des Spiels */
	public GameController gameController = null;
	
	
	public GameFrame(ClientModel clientModel) {
		this(clientModel, ResourceLoader.getFile("./maps/NoNameMap.xml"));
	}
	
	public GameFrame(ClientModel clientModel, File mapFile) {		
		setTitle("Spiel");
		setUndecorated(true);
		setResizable(false);
		setSize(getToolkit().getScreenSize());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		gameController = new GameController(clientModel, this, mapFile);

		setCursor(Mouse.getInvisibleCursor());	
		Mouse.setPosition(Utilities.getScreenCenter());
		
		getContentPane().add(gameController.getGLPanelInstance().getGLCanvas());
		
		gameController.startAllThreads();		
	}
}
