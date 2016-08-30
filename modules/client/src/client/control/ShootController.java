package client.control;

import java.util.*;

import client.*;

import framework.*;
import framework.object.dynamic.*;
import framework.types.*;
import framework.types.Line.*;
import framework.weapons.*;

public class ShootController {

	private static ShootController instance = null;
	
	private GameController gameController = null;
	
	private Weapon weapon;
	
	private Vector3D position;
	
	private Vector3D angle;
	
	private Line shootLine = null;
	
	private ContainerObject checkContainerObject = null;
	
	private Map<GraphicalObject, Vector3D> intersectionPoints = new HashMap<GraphicalObject, Vector3D>();
	
	
	private ShootController(GameController gameController) {
		this.gameController = gameController;
	}
	
	public static void initalize(GameController gameController) {
		instance = new ShootController(gameController);
	}
	
	public static ShootController getInstance() {
		if (instance == null) {
			throw new IllegalStateException("ShootController not initalized!");
		} else {
			return instance;
		}
	}
	
	public void shoot(Vector3D position, Vector3D angle, Weapon weapon) {
		shoot(position, angle, weapon, gameController.getMap().getMainContainerObject());
	}
	
	public void shoot(Vector3D position, Vector3D angle, Weapon weapon, ContainerObject containerObject) {
		this.checkContainerObject = containerObject;
		this.shootLine = new Line(new Vector3D(position), new Vector3D(position).add(angle), LineType.NEGATIVE_OPEN_LINE);
		this.intersectionPoints.clear();

		shoot(checkContainerObject);
		
		float minDist = Float.MAX_VALUE;
		float dist;
		GraphicalObject shootObject = null;
		for (GraphicalObject gObj : intersectionPoints.keySet()) {
			if ((dist = intersectionPoints.get(gObj).dinstance(position)) < minDist) {
				minDist = dist;
				shootObject = gObj;
			}
		}
		if (shootObject != null) {
			createBulletHoleObject(position, weapon, shootObject);
			if (shootObject.getOwner().isDynamic()) {
				((DynamicObject)shootObject.getOwner()).addPower(angle.multiplyBy(-weapon.getPower()/500));
			}
		}	
	}
	
	public void shoot(Player player, ContainerObject containerObject) {
		shoot(player.getPosition(), player.getCamera().getAngleAsVertex(), player.getWeapon(), containerObject);
	}
	
	private void shoot(ContainerObject containerObject) {
		if (containerObject.isShootable()) return;
		int cObjectCount = containerObject.getContainerObjectCount();
		for(int cObjIndex = 0; cObjIndex < cObjectCount; cObjIndex++) {
			shoot(containerObject.getContainerObject(cObjIndex));
		}
		
		int gObjectCount = containerObject.getGraphicalObjectCount();
		for(int gObjIndex = 0; gObjIndex < gObjectCount; gObjIndex++) {
			GraphicalObject gObj = containerObject.getGraphicalObject(gObjIndex);
			if (!gObj.isShootable()) {
				Vector3D intersectionPoint = gObj.checkLineTriangleCollision(shootLine);
				if (intersectionPoint != null) {
					intersectionPoints.put(gObj, intersectionPoint);
				}
			} 
		}
	}
	
	private void createBulletHoleObject(Vector3D position, Weapon weapon, GraphicalObject shootObject) {
		Integer textureId = weapon.getBulletHoleTextureId();
		Vector3D iPnt = intersectionPoints.get(shootObject);
		Vector3D alignment;
		
		if (shootObject.isInFrontOf(position)) {
			alignment = Vector3D.multiplyBy(shootObject.getNormalVector(), 0.001f + (float)Math.random() / 1000).add(iPnt);
		} else {
			alignment = Vector3D.multiplyBy(shootObject.getNormalVector(),-0.001f - (float)Math.random() / 1000).add(iPnt);
		}
		
		if (textureId == null) {
			String textureFile = weapon.getBulletHoleTexture();
			textureId = gameController.getMap().getTextureID(textureFile);
			weapon.setBulletHoleTextureId(textureId);
		}
		
		Vector3D vertex1 = Vector3D.subtract(iPnt, shootObject.getVertex(0)).normalize();
		Vector3D vertex2 = Vector3D.crossproduct(vertex1, shootObject.getNormalVector());
		Vector3D vertex3 = Vector3D.crossproduct(vertex2, shootObject.getNormalVector());
		Vector3D vertex4 = Vector3D.crossproduct(vertex3, shootObject.getNormalVector());
		
		
		SimpleGraphicalObject graphicalObject = new SimpleGraphicalObject();
		graphicalObject.setVertex(0, vertex1.multiplyBy(0.1f).add(alignment));
		graphicalObject.setTextureVector(0, new Vector2D(0.0f, 0.0f));
		graphicalObject.setVertex(1, vertex2.multiplyBy(0.1f).add(alignment));
		graphicalObject.setTextureVector(1, new Vector2D(1.0f, 0.0f));
		graphicalObject.setVertex(2, vertex3.multiplyBy(0.1f).add(alignment));
		graphicalObject.setTextureVector(2, new Vector2D(1.0f, 1.0f));
		graphicalObject.setTextureID(textureId);
		graphicalObject.calcComplete();
		shootObject.getOwner().addGraphicalObject(new SimpleGraphicalObject(graphicalObject));
//		bulletHoles.add(0, graphicalObject);
		graphicalObject.setVertex(0, vertex3);
		graphicalObject.setTextureVector(0, new Vector2D(1.0f, 1.0f));
		graphicalObject.setVertex(1, vertex4.multiplyBy(0.1f).add(alignment));
		graphicalObject.setTextureVector(1, new Vector2D(0.0f, 1.0f));
		graphicalObject.setVertex(2, vertex1);
		graphicalObject.setTextureVector(2, new Vector2D(0.0f, 0.0f));
		graphicalObject.setTextureID(textureId);
		graphicalObject.calcComplete();
		shootObject.getOwner().addGraphicalObject(new SimpleGraphicalObject(graphicalObject));
//		bulletHoles.add(0, graphicalObject2);

		
//		if (bulletHoles.size() > 10) {
//			((SimpleGraphicalObject)bulletHoles.get(bulletHoles.size()-1)).remove();
//			bulletHoles.remove(bulletHoles.size()-1);
//			((SimpleGraphicalObject)bulletHoles.get(bulletHoles.size()-1)).remove();
//			bulletHoles.remove(bulletHoles.size()-1);
//		}
	}
}