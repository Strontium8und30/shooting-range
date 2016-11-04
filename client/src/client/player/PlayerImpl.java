package client.player;

import common.*;

import utilities.*;
import client.*;
import framework.*;
import framework.types.*;
import framework.weapons.*;

public class PlayerImpl implements Player {
	
	private GameController gameController;
	
	/** Der Grafischespieler */
	private SimpleContainerObject playerModel;
	
	/** Die Kamera (Das Auge) des Spielers */
	private Camera camera;
	
	/** Der Rucksack des Spielers */
	private BackpackImpl backpack;
	
	/** Die Waffe die der Spieler gerade trägt */
	private Weapon weapon = Weapon.NO_WEAPON;
	
	/** Die Energie des Spielers */
	private int energy = 100;
	
	/** Die Rüstung des Spielers */
	private int amor = 100;
	
	/** Bewegt sich der Spieler */
	private boolean isMoving = false;
	
	/** Springt der Spieler */
	private boolean isJumping = false;
	
	/** Klettert der Spieler */
	private boolean isClimbing = false;
	
	/** Dieser "Winkel" wird zur berechnung der animierten Position benötigt */
	private transient float animationAngle;
	
	/** Welcher Sound muss für die Animation gerade abgespielt werden */
	private transient int animationSound;
	
	
	public PlayerImpl(GameController gameController, SimpleContainerObject playerModel) {
		this(gameController, playerModel, new Vector3D());
	}
	
	public PlayerImpl(GameController gameController, SimpleContainerObject playerModel, Vector3D startPosition) {
		this(gameController, startPosition);
		this.playerModel = playerModel;
		this.playerModel.transfareToZero();
		
	}
	
	private PlayerImpl(GameController gameController, Vector3D startPosition) {
		this.gameController = gameController;
		this.camera = new Camera(startPosition);
		this.backpack = new BackpackImpl();
	}
	
	public Vector3D getPosition() {
		return camera.getPosition();
	}
	
	public void setPosition(Vector3D position) {
		camera.setPosition(position);
		gameController.getClientModel().sendData(DataType.POSITION, this);
	}
	
	public Vector3D getLastPosition() {
		return camera.getLastPosition();
	}
	
	public void setLastPosition(Vector3D position) {
		camera.setLastPosition(position);
	}
	
	public Vector3D getAnimatedPosition() {
		if (isClimbing || isJumping || !isMoving) {
			return getPosition();
		}
		Vector3D animatedPosition = getPosition();
		if (animationAngle <= 0.0f) {
			animationAngle = 6.25f; 
			
			if (animationSound == 1) {
				getWeapon().getCrossHair().addCrossHairSize(0.8f);
				Utilities.playSound("fx/run1.wav");
				animationSound = 0;
			} else {
				getWeapon().getCrossHair().addCrossHairSize(0.8f);
				Utilities.playSound("fx/run2.wav");
				animationSound = 1;
			}
		} else { 
			animationAngle -= 6.25f / 12;
		}
		animatedPosition.setY(animatedPosition.getY() + (float)Math.sin(animationAngle) / 40.0f);
		return animatedPosition;
	}
	
	public Camera getCamera() {
		return camera;
	}

	@Override
	public BackpackImpl getBackpack() {
		return backpack;
	}
	
	public Weapon getWeapon() {
		return weapon;
	}
	
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
		Utilities.playSound("fx/change.wav");
	}
	
	public int getEnergy() {
		return energy;
	}
	
	public void setEnergy(int energy) {
		this.energy = energy;
	}
	
	public int getAmor() {
		return amor;
	}
	
	public void setAmor(int amor) {
		this.amor = amor;
	}
	
	public boolean isMoving() {
		return isMoving;
	}

	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	public boolean isJumping() {
		return isJumping;
	}


	public void setJumping(boolean isJumping) {
		this.isJumping = isJumping;
	}
	
	public boolean isClimbing() {
		return isClimbing;
	}

	public void setClimbing(boolean isClimbing) {
		this.isClimbing = isClimbing;
	}
	
	public void setShooting(boolean isShooting) {
		weapon.setShooting(this, isShooting);
	}
	
	public boolean isShooting() {
		return weapon.isShooting();
	}
	
	@Override
	public void shoot() {
		gameController.playerShoot();
	}
	
	public void reload() {
		Ammo ammoType = weapon.getAmmoType();
		backpack.removeAmmo(ammoType, weapon.reload(backpack.getAmmoCount(ammoType)));
	}

	public SimpleContainerObject getPlayerModel() {
		return playerModel;
	}
}