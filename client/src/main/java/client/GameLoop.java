package client;

import java.util.*;

import utilities.*;
import client.control.*;
import client.gui.*;
import client.player.*;
import framework.object.dynamic.*;
import framework.object.interactive.lift.*;
import framework.types.*;

public class GameLoop extends GameThread {
	
	/** Das Spieler Objekt */
	private PlayerImpl player = null;
	
	/** Das Grafikrender Panel */
	private GLPanel glPanel = null;
	
	/** Der Tastatur Kontroller */
	private KeyController keyController = null;
	
	/** Der Haupt Kontroller */
	private GameController gameController =  null;
	
	/** Wieviel Zeit darf pro Frame benötigt werden */
	private float maxTimePerFrame = 1000.0f / Const.FRAME_RATE;
	
	private long timePerFrameSet = 0; 
	
	private long framesPerSecondCounter = 0;
	
	private long currentFramesPerSecond;
	
	private Map<Triangle, Triangle> lastCollision = new HashMap<Triangle, Triangle>();
	private Map<Triangle, Triangle> checkedCollision = new HashMap<Triangle, Triangle>();
	
	
	public GameLoop(GameController gameController) {
		super("gameLoop");
		this.gameController = gameController;
		this.player = gameController.getPlayer();
		this.glPanel = gameController.getGLPanelInstance();
		this.keyController = gameController.getKeyController();
		gameController.registerThread(getName(), this);
	}
	
	@Override
	public void loop() {
		long startTimer = System.currentTimeMillis();
		keyboardInput();
		networkSync();
		checkPortals();
		checkLifts();
		calcDynamics();
		
		checkedCollision.clear();
		preCollisionsDetection();
		int colCounter = 0;
		while(collisionsDetection(gameController.getMap().getMainContainerObject())) {
			if (colCounter++ > 5) {
				System.out.println("Spieler zurück gesetzt" + player.getPosition() + " | " + player.getLastPosition());
				player.setPosition(player.getLastPosition());
				checkedCollision.clear();
				lastCollision.clear();
				if (collisionsDetection(gameController.getMap().getMainContainerObject())) {
					log.fatal("Letzte Position nicht kollisionsfrei.");
				} 
				break;
			}
			lastCollision.clear();
		};
		lastCollision.putAll(checkedCollision);
		
		graficsRepaint();
		syncWithFrameRate(startTimer);
	}
	
	public void keyboardInput() {
		keyController.executeMovements();
	}
	
	public void networkSync() {
//		if (z < 3) {
//			z++;
//			return;
//		}
//		z=0;
		gameController.networkSync();
	}
	
	public boolean preCollisionsDetection() {
		boolean col = false;
		for (Triangle sObj : lastCollision.keySet()) {
			for (Triangle triangle : player.getPlayerModel().getAllSubGraphicalObjects()) {		
				if (checkTriangleTriangleCollision(sObj, triangle)) col = true;
			}
		}
		return col;
	}
	
	public boolean collisionsDetection(ContainerObject containerObject) {
		if (containerObject.getGeometricCenter() == null) {
			log.error("Object hat kein geometrisches Zentrum:" + containerObject);
			System.exit(0);
		}
		if (!containerObject.isHard()) return false; 
		if (!((SimpleContainerObject) containerObject).isPointInRange(player.getPosition())) return false;
		boolean col = false;
		int cObjectCount = containerObject.getContainerObjectCount();
		for(int cObjIndex = 0; cObjIndex < cObjectCount; cObjIndex++) {
			if (collisionsDetection(containerObject.getContainerObject(cObjIndex))) col = true;
		}
		
		int gObjectCount = containerObject.getGraphicalObjectCount();
		for(int gObjIndex = 0; gObjIndex < gObjectCount; gObjIndex++) {
			Triangle sObj = containerObject.getGraphicalObject(gObjIndex);
			if (!lastCollision.containsKey(sObj)) {
				for (Triangle triangle : player.getPlayerModel().getAllSubGraphicalObjects()) {
					if (checkTriangleTriangleCollision(sObj, triangle)) col = true;		
				}
			}
		}
		return col;
	}
	
	public boolean checkTriangleTriangleCollision(Triangle triangle1, Triangle triangle2) {
		Triangle tmpTriangle = SimpleGraphicalObject.transfare(triangle2, player.getPosition());
		float dst2 = 0;
		float dstTmp = 0;
		
		if (triangle1.checkTriangleTriangleCollision(tmpTriangle)) {
			checkedCollision.put(triangle1, triangle2);
			Vector3D rel = Vector3D.subtract(player.getLastPosition(), triangle1.getVertex(2));
			float dst = Vector3D.produkt(triangle1.getNormalVector(), rel);
												
			for (int i = 0; i < 3;) {
				rel = Vector3D.subtract(tmpTriangle.getVertex(i), triangle1.getVertex(2));
				dst2 = Vector3D.produkt(triangle1.getNormalVector(), rel);
										
				if (dst < 0) {
					if (dst2 > dstTmp) {
						dstTmp = dst2;
					}
				} else if (dst > 0) {
					if (dst2 < dstTmp) {
						dstTmp = dst2;
					}
				} else {
					log.error("unbekannter Kollisionsstatus");
				}
				player.setPosition(Vector3D.add(player.getPosition(), Vector3D.multiplyBy(triangle1.getNormalVector(), dstTmp * -1.1f)));
				
				if (player.isJumping() && Math.abs(triangle1.getNormalVector().getY()) > 0.1f) {
					player.setJumping(false);
					player.getCamera().jumpFactor = new Vector3D(player.getCamera().jumpHeight);
					Utilities.playSound("fx/aufprall.wav");
				}
				
				return true;
			}
		}
		return false;
	}
	
	public void checkPortals() {
		for (PortalInf portal : gameController.getMap().getPortals()) {
			if (portal.isATOB()) {
				if (((SimpleContainerObject) portal.getPortalA()).isPointInRange(player.getPosition())) {
					if (!portal.isUsed() && portal.isOpen()) {
						player.setPosition(portal.getPortalB().getGeometricCenter());
						Utilities.playSound("fx/boing.wav");
					}
					portal.setUsed(true);
					continue;
				}
			}
			if (portal.isBTOA()) {
				if (((SimpleContainerObject) portal.getPortalB()).isPointInRange(player.getPosition())) {
					if (!portal.isUsed() && portal.isOpen()) {
						player.setPosition(portal.getPortalA().getGeometricCenter());
						Utilities.playSound("fx/boing.wav");
					}
					portal.setUsed(true);
					continue;
				}
			}
			portal.setUsed(false);
		}
	}
	
	public void checkLifts() {
		for (Lift lift : gameController.getMap().getLifts()) {
			if (lift.isInUse()) {
				lift.moveLift();
			}
		}
	}
	
	public void calcDynamics() {
		for (DynamicObject dynamicObject : gameController.getMap().getDynamics()) {
			if (dynamicObject.isActive()) {
				dynamicObject.calculate(gameController.getMap().getMainContainerObject());
			}
		}
	}
	
	public void graficsRepaint() {
		glPanel.repaint();
	}
	
	private void syncWithFrameRate(long startFrameTimer) {
		long endFrameTimer = System.currentTimeMillis();
		long timer = (long)maxTimePerFrame - (endFrameTimer - startFrameTimer);

		if (System.currentTimeMillis() - timePerFrameSet >= 1000) {
			currentFramesPerSecond = framesPerSecondCounter;
			timePerFrameSet = System.currentTimeMillis();
			framesPerSecondCounter = 0;
		}

		if (timer > 0) {
			try {
				Thread.sleep(timer);
			} catch (InterruptedException e) {}
		}
		framesPerSecondCounter++;
	}
	
	public long getFPS() {
		return currentFramesPerSecond;
	}
}
