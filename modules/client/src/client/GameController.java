package client;

import java.awt.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import utilities.control.*;
import utilities.log.*;
import chapter.*;
import client.chapter.MapLoader;
import client.control.*;
import client.gui.*;
import client.player.*;

import common.*;

import framework.legacy.*;
import framework.object.interactive.*;
import framework.types.*;

public class GameController {

	/** Logging */
	public static Log log = LogFactory.getLog(GameController.class);
	
	/** Model für Netzwerk */
	private ClientModel clientModel = null;
	
	/** Der Spielframe */
	private JFrame gameFrame = null;
	
	/** Der Spieler */
	private PlayerImpl player = null;
	
	/** Das grafische Ausgabe Panel */
	private GLPanel glPanel = null;
	
	/** Tastaturkontroller */
	private KeyController keyController = null; 
	
	/** Die Map */
	private Chapter chapter = null;	
	
	/** Spielschleife */
	private GameLoop gameLoop = null; 
	
	/** Alle registrierten GameThreads */
	private Map<String, GameThread> gameThreads = new HashMap<String, GameThread>();

	/** läuft das Spiel */
	private boolean isRunning = true;
	

	public GameController(ClientModel clientModel, JFrame frame, File mapFile) {
		this.clientModel = clientModel;
		this.gameFrame = frame;		
		this.chapter = new MapLoader().loadMap(mapFile);
		
		this.player = new PlayerImpl(this, loadPlayerModel(Const.PLAYER_MODEL), Const.PLAYER_STARTPOS);

		this.keyController = new KeyController(this);
		this.glPanel = new GLPanel(this);
		this.gameLoop = new GameLoop(this);
		ShootController.initalize(this);
	}
	
	public ClientModel getClientModel() {
		return clientModel;
	}

	public void setClientModel(ClientModel clientModel) {
		this.clientModel = clientModel;
	}

	public SimpleContainerObject loadPlayerModel(File file) {
		ImportFacade importFacade = ImportFacade.getInstance();
		return importFacade.importToModel(getMap().getTextureHelper(), file);
	}
	
	public PlayerImpl getPlayer() {
		return player;
	}
	
	public GLPanel getGLPanelInstance() {
		return glPanel;
	}
	
	public KeyController getKeyController() {
		return keyController;
	}
	
	public Chapter getMap() {
		return chapter;
	}	
	
	public GameLoop getGameLoop() {
		return gameLoop;
	}
	
	
	public void playerShoot() {
		ShootController shootController = ShootController.getInstance();
		shootController.shoot(player, getMap().getMainContainerObject());
		clientModel.sendData(DataType.PLAYER_SHOOT, null);
	}
	
	public void useSwitchs() {
		for (Switch switch_ : getMap().getSwitchs()) {
			if (((SimpleContainerObject)switch_.getContainerObject()).isPointInRange(player.getPosition(), 0.5f) && 
				!getMap().hitsObject(new Line(((SimpleContainerObject)switch_.getContainerObject()).getGeometricCenter(), player.getPosition()), switch_.getContainerObject())) {
				switch_.use(getMap().getPortals());
			}
		}
	}
	
	public long getFPS() {
		return gameLoop.getFPS();
	}
	
	public void networkSync() {
		try {
			clientModel.sendData(DataType.SEND_DATA, null);
			if (clientModel.isDataAvailable()) {
				clientModel.receiveData();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void registerThread(String name, GameThread thread) {
		if (gameThreads.containsKey(name)) {
			if (gameThreads.containsValue(thread)) {
				log.debug("Thread ist bereits registriert.");
			}
			log.debug("Name ist nicht eindeutig.");
		}
		gameThreads.put(name, thread);
	}
	
	public void deregisterThread(String name) {
		gameThreads.remove(name);
	}
	
	public void startAllThreads() {
		for (GameThread thread : gameThreads.values()) {
			if (!thread.isAlive()) {
				thread.start();
			}
		}
	}
	
	public void stopAllThreads() {
		for (GameThread thread : gameThreads.values()) {
			thread.quit();
		}
	}
	
	public void pauseAllThreads() {
		for (GameThread thread : gameThreads.values()) {
			thread.pause();
		}
	}
	
	public void proceedAllThreads() {
		for (GameThread thread : gameThreads.values()) {
			thread.proceed();
		}
	}
	
	public void pauseThread(String name) {
		gameThreads.get(name).pause();
	}
	
	public void proceedThread(String name) {
		gameThreads.get(name).proceed();
	}
	
	public void pause() {
		while(!isRunning) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
		log.warning("Pause ende");
	}
	
	public void mini() {
		log.info("Lost Focus --> Threads pausieren");
		gameFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		pauseAllThreads();
		gameFrame.setVisible(false);
		gameFrame.dispose();
		gameFrame.setUndecorated(false);
		gameFrame.setResizable(true);
		gameFrame.setVisible(true);
	}
	
	public void maxi() {
		log.info("Get Focus --> Threads wieder starten");
//		gameFrame.setVisible(false);
//		gameFrame.dispose();
//		gameFrame.setUndecorated(true);
//		gameFrame.setResizable(false);
//		gameFrame.setVisible(true);
		proceedAllThreads();
		gameFrame.setCursor(Mouse.getInvisibleCursor());
	}
	
	public void quit() {
		stopAllThreads();
		gameFrame.setVisible(false);
		gameFrame.dispose();
		System.exit(0);
	}
}
