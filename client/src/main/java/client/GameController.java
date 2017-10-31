package client;

import java.io.*;

import chapter.*;
import client.chapter.MapLoader;
import client.control.*;
import client.gui.*;
import client.player.*;
import common.*;
import framework.legacy.*;
import framework.object.interactive.*;
import framework.types.*;
import utilities.log.*;

public class GameController {

	/** Logging */
	public static Log log = LogFactory.getLog(GameController.class);
	
	/** Model für Netzwerk */
	private ClientModel clientModel = null;
	
	/** Der Spielframe */
	private WindowView gameView = null;
	
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
	
	/** läuft das Spiel */
	private boolean isRunning = true;
	

	public GameController(ClientModel clientModel, WindowView gameView, File mapFile) {
		this.clientModel = clientModel;
		this.gameView = gameView;		
		this.chapter = new MapLoader().loadMap(mapFile);
		
		this.player = new PlayerImpl(this, loadPlayerModel(Const.PLAYER_MODEL), Const.PLAYER_STARTPOS);

		this.keyController = new KeyController(this);
		this.glPanel = new GLPanel(this);
		this.gameLoop = new GameLoop(this);
		ShootController.initalize(this);
		this.gameLoop.start();
	}
	
	public ClientModel getClientModel() {
		return clientModel;
	}
	
	public WindowView getGameView() {
		return gameView;
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
		clientModel.sendData(DataType.SEND_DATA, null);		
	}
				
	public boolean isRunning() {
		return isRunning;
	}
	
	public void quit() {
		gameView.dispose();
		System.exit(0);
	}
}
