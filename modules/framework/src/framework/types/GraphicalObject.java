package framework.types;


/**
 * Ein GraphicalObject enthält lediglich drei Vektoren, die zusammen ein 
 * Dreieck ergeben.
 * 
 * @author Thorben
 *
 */
public abstract class GraphicalObject extends TexturedTriangle implements ObjectMetaData {
	
	/** Die eindeutige Id des Objekts */
	private Long id = ObjectMetaDataDefaults.ID;
	
	/** Reference to the Parent */
	private ContainerObject owner = null;	
	
	/** Is the MapObject visible or not */
	private boolean visible = ObjectMetaDataDefaults.VISIBLE;
	
	/** Kann sich dieses Objekt bewegen */
	private boolean dynamic = ObjectMetaDataDefaults.DYNAMIC;
	
	/** Can you walk through the MapObject or not */
	private boolean hard = ObjectMetaDataDefaults.HARD;
	
	/** Is it shoot through the MapObject or not */
	private boolean shootable = ObjectMetaDataDefaults.SHOOTABLE;
	
	/** Is it possible to destroy the MapObject or not */
	private boolean destroyable = ObjectMetaDataDefaults.DESTROYABLE;
	
	/** Whitch mass has the MapObject */
	private int mass = ObjectMetaDataDefaults.MASS;
	
	/** Witch Power has the MapObject */
	private int power = ObjectMetaDataDefaults.POWER;
	
	
	public GraphicalObject() {}
	
	public GraphicalObject(GraphicalObject graphicalObject) {
		super(graphicalObject);
		setMetaData(graphicalObject);
		this.owner = graphicalObject.getOwner();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public ContainerObject getOwner() {
		return owner;
	}
	
	public void setOwner(ContainerObject owner) {
		this.owner = owner;
	}
	
	@Override
	public boolean isVisible() {
		return visible;
	}
	
	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	@Override
	public boolean isDynamic() {
		return dynamic;
	}

	@Override
	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

	@Override
	public boolean isHard() {
		return hard;
	}
	
	@Override
	public void setHard(boolean hard) {
		this.hard = hard;
	}

	@Override
	public boolean isShootable() {
		return shootable;
	}
	
	@Override
	public void setShootable(boolean shootable) {
		this.shootable = shootable;
	}

	@Override
	public boolean isDestroyable() {
		return destroyable;
	}
	
	@Override
	public void setDestroyable(boolean destroyable) {
		this.destroyable = destroyable;
	}

	@Override
	public int getMass() {
		return mass;
	}

	@Override
	public void setMass(int mass) {
		this.mass = mass;
	}
	
	@Override
	public int getPower() {
		return power;
	}
	
	@Override
	public void setPower(int power) {
		this.power = power;
	}
	
	public void remove() {
		owner.removeChildGraphicalObject(this);
	}
	
	@Override
	public void setMetaData(ObjectMetaData metaData) {
		id = metaData.getId();
		dynamic = metaData.isDynamic();
		hard = metaData.isHard();
		shootable = metaData.isShootable();		
		destroyable = metaData.isDestroyable();
		mass = metaData.getMass();
		power = metaData.getPower();
	}
	
	@Override
	public String toString() {
		return "GO id = " + id;
	}
}
