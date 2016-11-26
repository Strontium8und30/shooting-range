package framework.weapons;

import java.io.*;
import java.util.*;

import org.jdom.*;

import utilities.*;
import utilities.log.*;
import utilities.resource.ResourceLoader;
import framework.*;
import framework.weapons.crosshair.*;

public enum Weapon {
	NO_WEAPON(0, "Faust", 'F', new NoCrossHair(), Ammo.NO_AMMO),
	PISTOL("weapons/pistole.properties"),
	MP5("weapons/mp5.properties");

	/** Logging */
	public static Log log = LogFactory.getLog(Weapon.class);
	
	/** Id der Waffe */
	private int id;
	
	/** Der Name der Waffe */
	private String name;
	
	/** Kurzwahl taste */
	private char shortcut;
	
	/** Darstellung des Fadenkreuzes */
	private CrossHair crossHair;
	
	/** Ist die Waffe eine automatische Waffe */
	private boolean automatic;
	
	/** Die Reichweite der Waffe */
	private float range;
	
	/** Power/Kraft/Zerstörung/Verletzung */
	private float power;
	
	/** Zuverlässigkeit */
	private float dependance;
	
	/** Der Streuungsfaktor */
	private float diffusion;
	
	/** Kugeln maximal im Magazin */
	private int maxBulletMagazine;
	
	/** Kugeln im Magazin */
	private int bulletMagazine = maxBulletMagazine;
	
	/** Wann wurde der letze Schuss abgegeben */
	private transient long lastShootTime;
	
	/** Schussrate Schuss/Minute */
	private int bulletsPerMinute = 1;
	
	/** Munitionstyp */
	private Ammo ammoType;
	
	/** Schusssound */
	private String soundShoot;
	
	/** Nachladesound */
	private String soundReload;
	
	/** Leere Waffesound */
	private String soundEmtyWeapon;
	
	/** Ladehämungssaound */
	private String soundStoppageOfGun;
	
	/** Texture für das Einschussloch */
	private String bulletHoleTexture;
	
	/** Die Id der Einschusslochtexture */
	private Integer bulletHoleTextureId;

	/** Wird die Waffe gerade benutzt */
	private transient boolean shooting;
	
	
	private Weapon(int id, String name, char shortcut, CrossHair crossHair, Ammo ammoType) {
		this.id = id;
		this.name = name;
		this.shortcut = shortcut;
		this.crossHair = crossHair;
		this.ammoType = ammoType;
	}
	
	private Weapon(int id, String name, char shortcut, CrossHair crossHair, boolean automatic,
			float range, float power, float dependance, float diffusion, 
			int bulletMagazine, int bulletsPerMinute, Ammo ammoType, 
			String soundShoot, String soundReload, String soundEmtyWeapon,
			String soundStoppageOfGun, String bulletHoleTexture) {
		this.id = id;
		this.name = name;
		this.shortcut = shortcut;
		this.automatic = automatic;
		this.crossHair = crossHair;
		this.range = range;
		this.power = power;
		this.dependance = dependance;
		this.diffusion = diffusion;
		this.bulletMagazine = bulletMagazine;
		this.bulletsPerMinute = bulletsPerMinute;
		this.ammoType = ammoType;
		this.soundShoot = soundShoot;
		this.soundReload = soundReload;
		this.soundEmtyWeapon = soundEmtyWeapon;
		this.soundStoppageOfGun = soundStoppageOfGun;
		this.bulletHoleTexture = bulletHoleTexture;
	}
	
	private Weapon(String propertieFile) {
		Properties prop = new Properties();
		try {
			prop.load(new FileReader(ResourceLoader.getFile(propertieFile)));
			
			this.id = new Integer(prop.getProperty("weapon.id"));
			this.name = prop.getProperty("weapon.name");
			this.shortcut = prop.getProperty("weapon.shortcut").charAt(0);
			
			try	{
				Class<?> c = Class.forName(prop.getProperty("weapon.crossHair"));
				this.crossHair = (CrossHair)c.newInstance();
			} catch(ClassNotFoundException e) {
				System.out.println("Klasse nicht gefunden");
			} catch(IllegalAccessException ie) {
				ie.printStackTrace(System.err);
			} catch(InstantiationException insterr) {
				insterr.printStackTrace(System.err);
			}

			this.automatic = new Boolean(prop.getProperty("weapon.automatic"));
			this.range = new Float(prop.getProperty("weapon.range"));
			this.power = new Float(prop.getProperty("weapon.power"));
			this.dependance = new Float(prop.getProperty("weapon.dependance"));
			this.diffusion = new Float(prop.getProperty("weapon.diffusion"));
			this.maxBulletMagazine = new Integer(prop.getProperty("weapon.maxBulletMagazine"));
			this.bulletMagazine = this.maxBulletMagazine;
			this.bulletsPerMinute = new Integer(prop.getProperty("weapon.bulletsPerMinute"));;
			this.ammoType = Ammo.valueOf(prop.getProperty("weapon.ammoType"));
			this.soundShoot = prop.getProperty("weapon.soundShoot");
			this.soundReload = prop.getProperty("weapon.soundReload");
			this.soundEmtyWeapon = prop.getProperty("weapon.soundEmtyWeapon");
			this.soundStoppageOfGun = prop.getProperty("weapon.soundStoppageOfGun");
			this.bulletHoleTexture = prop.getProperty("weapon.bulletHoleTexture");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public char getShortcut() {
		return shortcut;
	}

	public CrossHair getCrossHair() {
		return crossHair;
	}

	public boolean isAutomatic() {
		return automatic;
	}
	
	public float getRange() {
		return range;
	}

	public float getPower() {
		return power;
	}

	public float getDependance() {
		return dependance;
	}

	public float getDiffusion() {
		return diffusion;
	}

	public int getBulletMagazine() {
		return bulletMagazine;
	}

	public int getBulletsPerMinute() {
		return bulletsPerMinute;
	}
	
	public int getMaxBulletMagazine() {
		return maxBulletMagazine;
	}

	public Ammo getAmmoType() {
		return ammoType;
	}

	public String getSoundShoot() {
		return soundShoot;
	}

	public String getSoundReload() {
		return soundReload;
	}

	public String getSoundEmtyWeapon() {
		return soundEmtyWeapon;
	}

	public String getSoundStoppageOfGun() {
		return soundStoppageOfGun;
	}
	
	public String getBulletHoleTexture() {
		return bulletHoleTexture;
	}
	
	public Integer getBulletHoleTextureId() {
		return bulletHoleTextureId;
	}
	
	public void setBulletHoleTextureId(Integer bulletHoleTextureId) {
		this.bulletHoleTextureId = bulletHoleTextureId;
	}
	
	private int checkId(int id) {
		for (Weapon weapon : values()) {
			if (weapon.getId() == id) {
				throw new IllegalDataException("Doppelte Waffen Id.");
			}
		}
		return id;
	}
	
	public static Weapon getById(int id) {
		for (Weapon weapon : values()) {
			if (weapon.getId() == id) {
				return weapon;
			}
		}
		throw new NoSuchElementException("Es wurde keine Waffe mit der Id " + id + "gefunden." );
	}

	public void setShooting(final Player player, boolean isShooting) {
		if (!isShooting() && isShooting) {
			if (isAutomatic() && getBulletMagazine() > 0) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						while(isShooting()) {
							shoot(player);
							try {
								Thread.sleep(60000/getBulletsPerMinute());
							} catch (InterruptedException e) {}
						}
					}
				}).start();
			} else {
				shoot(player);
			}
		}
		this.shooting = isShooting;
	}
	
	public boolean isShooting() {
		return shooting;
	}
	
	private void shoot(Player player) {
		if ((System.currentTimeMillis() - lastShootTime) > 60000 / bulletsPerMinute-100) {
			lastShootTime = System.currentTimeMillis();
			if (bulletMagazine > 0) {
				if (Math.random() * 100 > dependance) {
					Utilities.playSound(soundStoppageOfGun);
				} else {
					Utilities.playSound(soundShoot);
					bulletMagazine--;
					player.shoot();
					crossHair.addCrossHairSize(0.7f);
				}
			} else {
				Utilities.playSound(soundEmtyWeapon);
				shooting = false;
			}
		}
	}
	
	/**
	 * Diese Methode läd die Waffe nach.
	 * 
	 * @param availableBullets Wie viele Kugeln sind von diesem Munitionstyp im Rucksack vorhanden
	 * @return Die Anzahl der Kugeln die Nachgeladen wurden
	 */
	public int reload(int availableBullets) {
		if (availableBullets == 0) return 0;
		int reqiuredBullets = maxBulletMagazine - bulletMagazine;
		if (reqiuredBullets == 0) return 0;
		if (reqiuredBullets > availableBullets) {
			reqiuredBullets = availableBullets;
		}
		bulletMagazine += reqiuredBullets;
		Utilities.playSound(soundReload);
		return reqiuredBullets;
	}
}
