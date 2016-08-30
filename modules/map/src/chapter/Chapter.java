package chapter;

import java.util.*;

import utilities.gl.*;
import utilities.log.*;

import com.sun.opengl.util.texture.*;

import framework.object.dynamic.*;
import framework.object.interactive.*;
import framework.object.interactive.lift.*;
import framework.types.*;


public class Chapter {
	
	public static Log log = LogFactory.getLog(Chapter.class);
	
	/** Der Name der Map */
	protected String name = null;
	
	/** Der Wurzelknoten der Objekt Daten */
	protected ContainerObject containerObject = null;
	
	/** Portale */
	protected List<PortalInf> portals = new ArrayList<PortalInf>();
		
	/** InteractiveObjects */
	protected List<InteractiveObject> interactiveObjects = new ArrayList<InteractiveObject>();
	
	/** InteractiveObjects */
	protected Set<Switch> switchs = new HashSet<Switch>();
	
	/** InteractiveObjects */
	protected List<Lift> lifts = new ArrayList<Lift>();
	
	/** InteractiveObjects */
	protected Set<DynamicObject> dynamics = new HashSet<DynamicObject>();
	
	/** Hält und verwaltet alle Texturen */
	protected TextureHelper textureHelper = null;
	
	
	public Chapter(ContainerObject cObj) {
		containerObject = cObj;
		textureHelper = new TextureHelper();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ContainerObject getMainContainerObject() {
		return containerObject;
	}
	
	public void setMainContainerObject(ContainerObject complexObject) {
		this.containerObject = complexObject;
	}
	
	public void addPortal(PortalInf portal) {
		portals.add(portal);
	}
	
	public void removePortal(PortalInf portal) {
		portals.remove(portal);
	}
	
	public PortalInf getPortal(int index) {
		return portals.get(index);
	}
	
	public List<PortalInf> getPortals() {
		return portals;
	}
	
	public void addInteractiveObject(InteractiveObject interactiveObject) {
		interactiveObjects.add(interactiveObject);
	}
	
	public void addInteractiveObjects(List<InteractiveObject> interactiveObjectList) {
		interactiveObjects.addAll(interactiveObjectList);
	}
	
	public void removeInteractiveObject(InteractiveObject interactiveObject) {
		interactiveObjects.remove(interactiveObject);
	}
	
	public InteractiveObject getInteractiveObject(int index) {
		return interactiveObjects.get(index);
	}
	
	public List<InteractiveObject> getInteractiveObjects() {
		return interactiveObjects;
	}
	
	public void addSwitch(Switch switch_) {
		switchs.add(switch_);
	}
	
	public void addSwitchs(Set<Switch> switchList) {
		switchs.addAll(switchList);
	}
	
	public void removeSwitch(Switch switch_) {
		switchs.remove(switch_);
	}
	
	public Set<Switch> getSwitchs() {
		return switchs;
	}
	
	public void addLift(Lift lift) {
		lifts.add(lift);
	}
	
	public List<Lift> getLifts() {
		return lifts;
	}
	
	public Set<DynamicObject> getDynamics() {
		return dynamics;
	}

	public void addDynamicObject(DynamicObject dynamic) {
		this.dynamics.add(dynamic);
	}

	public Integer prepareTexture(String file) {
		return textureHelper.prepareTexture(file);
	}
	
	public void prepareTexture(int id, String file) {
		textureHelper.prepareTexture(id, file);
	}
	
	public Texture getTexture(int id) {
		return textureHelper.getTexture(id);
	}
	
	public int getTextureID(String path) {
		return textureHelper.getTextureID(path);
	}
	
	public TextureHelper getTextureHelper() {
		return textureHelper;
	}
	
	public void fetchTextures() {
		textureHelper.fetchTextures();
	}
	
	public boolean hitsObject(Line line) {
		return hitsObject(getMainContainerObject(), null, line);
	}
	
	public boolean hitsObject(Line line, ContainerObject exclude) {
		return hitsObject(getMainContainerObject(), exclude, line);
	}
	
	private boolean hitsObject(ContainerObject containerObject, ContainerObject exclude, Line line) {
		if (containerObject.isShootable() || exclude == containerObject) return false;
		int cObjectCount = containerObject.getContainerObjectCount();
		for(int cObjIndex = 0; cObjIndex < cObjectCount; cObjIndex++) {
			if (hitsObject(containerObject.getContainerObject(cObjIndex), exclude, line)) {
				return true;
			}
		}
		
		int gObjectCount = containerObject.getGraphicalObjectCount();
		for(int gObjIndex = 0; gObjIndex < gObjectCount; gObjIndex++) {
			GraphicalObject gObj = containerObject.getGraphicalObject(gObjIndex);
			if (!gObj.isShootable()) {
				if (gObj.checkLineTriangleCollision(line) != null) {
					return true;
				}
			} 
		}
		return false;
	}
}
