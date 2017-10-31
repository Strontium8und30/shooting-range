package client.gui;

import java.awt.*;
import java.io.*;

import javax.swing.*;

import client.*;
import utilities.control.*;
import utilities.log.*;
import utilities.resource.*;

public class GameFrame extends WindowView {
		
	/** Logging */
	public Log log = LogFactory.getLog(GameFrame.class);
	
	/** Die zentrale Steuerung/Verwaltung des Spiels */
	public GameController gameController = null;
	
	
	public GameFrame(ClientModel clientModel, Point location) {
		this(clientModel, ResourceLoader.getFile("./maps/NoNameMap.xml"), location);
	}
	
	public GameFrame(ClientModel clientModel, File mapFile) {
		this(clientModel, mapFile, new Point(0,0));
	}
	
	public GameFrame(ClientModel clientModel, File mapFile, Point location) {
		super(clientModel, location);
				
		setFullscreen();
		
		frame.setCursor(Mouse.getInvisibleCursor());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		gameController = new GameController(clientModel, this, mapFile);
		
		frame.getContentPane().add(gameController.getGLPanelInstance().getGLCanvas());
	}

	@Override
	public Component getComponent() {
		return frame;
	}
}
