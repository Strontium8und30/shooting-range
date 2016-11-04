package maptool.gui.mainFrame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.jdom.JDOMException;

import framework.Camera;
import framework.object.interactive.Switch;
import framework.object.interactive.lift.Lift;
import framework.types.GraphicalObject;
import framework.types.ObjectMetaData;
import framework.types.PortalInf;
import framework.types.Vector3D;
import maptool.Const;
import maptool.chapter.Chapter;
import maptool.chapter.MapLoader;
import maptool.control.Controller;
import maptool.export.ExportDAO;
import maptool.gui.mainFrame.ObjectTree.ObjectTreeNode;
import maptool.legacy.ImportDAO;
import maptool.object.ComplexContainerObject;
import maptool.object.ModelBuildObject;
import utilities.log.Log;
import utilities.log.LogFactory;
import utilities.mvc.DefaultModel;
import utilities.mvc.EventAction;
import utilities.mvc.View;



public class MainModel extends DefaultModel {

	/** Logging */
	public final static Log log = LogFactory.getLog(MainModel.class);
	
	/** Das Haupmenü muss aktualisiert werden */
	public final static EventAction MAINMENU_RELOAD = new EventAction(); 
	
	/** Eine GLView muss aktualisiert werden */
	public final static EventAction GLVIEW_REPAINT = new EventAction(); 
	
	/** Eine GLView muss aktualisiert werden */
	public final static EventAction GLVIEW_CHANGED = new EventAction(); 
	
	/** Ein ComplexObject wurde dem Tree hinzugefügt */
	public final static EventAction ADD_COMPLEXOBJECT_TO_TREE = new EventAction(); 
	
	/** Die Selektierung im ObjectNavgator hat sich geändert */
	public final static EventAction OBJECTTREE_SELECTION_CHANGED = new EventAction(); 
	
	/** Ein Objekt wurde gelöscht */
	public final static EventAction OBJECTTREE_DELETE_OBJECT = new EventAction(); 
	
	/** Der Objektnavigator wird neu geladen */
	public final static EventAction OBJECTTREE_RELOAD = new EventAction(); 
	
	/** Der Portalnavigator bekommt ein neues Portal */
	public final static EventAction PORTALTREE_ADD_PORTAL = new EventAction(); 
	
	/** InfoView anzeigen oder nicht anzeigen */
	public final static EventAction INFOVIEW_SHOW = new EventAction(); 
	
	/** Hauptview */
	private View mainView = null;
	
	/** Alle standard Models */
	public Map<String, ComplexContainerObject> modelObjects;
	
	/** DataAccessObject Export*/
	public static ExportDAO daoExport = new ExportDAO();
	
	/** DataAccessObject Export*/
	public static ImportDAO daoImport = new ImportDAO();
	
	/** BackupObject */
	public ComplexContainerObject backupObject = null;
	
	/** Selektiertes Objekt im ObjektNavigator */
	public ModelBuildObject modelObject = null;
	
	/** Selektiertes Objekt im ObjektNavigator */
	public List<ObjectTreeNode> selectedObjects = null;
	
	/** Sollen selektierte Objekte bewegt werden */
	public boolean moveSelectedObjects = false;
	
	/** Perspektive */
	public int perspective = Const.TEXTURE_FACE;
	
	/** Gittergenauigkeit */
	public Vector3D gridPrecision = new Vector3D(Const.GRID_PRECISION);
	
	/** Die Karte */
	private Chapter map = null;
	
	
	public MainModel() {
		loadMap();
		loadStadardObjects();
	}
	
	public void newMap() {
		map = new Chapter(new ComplexContainerObject("Haupt Kontainer"));
		setModelObject(null);
		loadStadardObjects();
		notifyViews(MainModel.OBJECTTREE_RELOAD);
		glRepaint();
	}
	
	public void loadMap() {
		try {
			File mapFile = new File("./maps/NoNameMap.xml");
			map = new MapLoader().loadMap(mapFile);
		} catch (Exception e) {
			log.error("Fehler beim laden der Map.", e);
		}
	}
	
	public void loadStadardObjects() {
		modelObjects = new HashMap<String, ComplexContainerObject>();
		Properties properties = new Properties();
		try {
			properties.load(new FileReader(new File("models/models.properties")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		String[] modelList = properties.getProperty("model.list").split(",");
		for (String modelObj : modelList) {
			String modelFile = properties.getProperty("model." + modelObj + ".file");
			try {
				ComplexContainerObject cco = daoImport.importToModel(map.getTextureHelper(), new File(modelFile));
				modelObjects.put(cco.getName(), cco);
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isLift(ObjectMetaData cObj) {
		return getLift(cObj).isPresent();
	}
	
	public Optional<Lift> getLift(ObjectMetaData cObj) {
		return getMap().getLifts().stream().filter(lift -> lift.getContainerObject().getId() == cObj.getId()).findAny();
	}
	
	public boolean isSwitch(ObjectMetaData cObj) {
		return getSwitch(cObj).isPresent();
	}
	
	public Optional<Switch> getSwitch(ObjectMetaData cObj) {
		return getMap().getSwitchs().stream().filter(switch_ -> switch_.getContainerObject().getId() == cObj.getId()).findAny();
	}
	
	public View createView() {		
		return mainView = new MainFrame(this);
	}
	
	public void glRepaint() {
		notifyViews(GLVIEW_REPAINT);
	}

	public void glViewChanged() {
		notifyViews(GLVIEW_CHANGED);
	}
	
	public View getMainView() {
		return mainView;
	}
	
	public void moveToObject(ComplexContainerObject complexObject) {
		Vector3D center = new Vector3D(complexObject.getGeometricCenter());
		center.autoGridAlignment();
		Controller.getCamera().setPosition(center);
		Controller.getCamera().setAngleHorizontal(0);
		Controller.getCamera().setAngleVertical(0);
		glRepaint();
	}
	
	public void setModelObject(ComplexContainerObject containerObject) {
		if (containerObject != null) {
			modelObject = new ModelBuildObject(containerObject);
		} else {
			modelObject = null;
			gridPrecision = new Vector3D(Const.GRID_PRECISION);
		}
		glRepaint();
	}
	
	public ModelBuildObject getModelObject() {
		return this.modelObject;
	}
	
	public void placeModelObject() {
		Camera cam = Controller.getCamera();
		if (getModelObject() != null) {
			if (isMoveSelectedObjects()) { 
				ComplexContainerObject lastObj = getBackupObject();
				lastObj.setVisible(true);
				lastObj.transfareTo(cam.getPosition());
				setBackupObject(null);
				setModelObject(null);
				setMoveSelectedObjects(false);
			} else {
				ComplexContainerObject cObj = new ComplexContainerObject(getModelObject());
				cObj.transfare(cam.getPosition());
				notifyViews(MainModel.ADD_COMPLEXOBJECT_TO_TREE, cObj);
			}
			glRepaint();
		}
	}

	public ComplexContainerObject getBackupObject() {
		return backupObject;
	}

	public void setBackupObject(ComplexContainerObject backupObject) {
		this.backupObject = backupObject;
	}

	public List<ObjectTreeNode> getSelectedObjects() {
		return selectedObjects;
	}

	public void setSelectedObjects(List<ObjectTreeNode> selectedObjects) {
		notifyViews(MainModel.OBJECTTREE_SELECTION_CHANGED, selectedObjects);
		this.selectedObjects = selectedObjects;
	}

	public void setMap(Chapter map) {
		this.map = map;
	}
	
	public Chapter getMap() {
		return map;
	}
	
	public void addPortal(PortalInf portal) {
		map.addPortal(portal);
		notifyViews(PORTALTREE_ADD_PORTAL, portal);
	}
	
	public Vector3D getGridPrecision() {
		return gridPrecision;
	}


	public void setGridPrecision(Vector3D gridPrecision) {
		this.gridPrecision = gridPrecision;
	}


	public boolean isMoveSelectedObjects() {
		return moveSelectedObjects;
	}

	public void setMoveSelectedObjects(boolean moveSelectedObjects) {
		this.moveSelectedObjects = moveSelectedObjects;
	}

	public int getPerspective() {
		return perspective;
	}

	public void setPerspective(int perspective) {
		this.perspective = perspective;
		glRepaint();
	}
	
	public void exportToModel(ComplexContainerObject complexObject, File file) {
		try {
			daoExport.exportToModel(map, complexObject, file);
		} catch (IOException ioe) {
			log.error("Konnte nicht exportiert werden", ioe);
		}
	}
	
	public boolean exportMap(File mapFile) {
		try {
			new ExportDAO().exportToMap(getMap(), mapFile);
		} catch (IOException ioe) {
			log.error("Fehler beim exportieren der Map", ioe);
			return false;
		}
		return true;
	}
	
	public void removeObjectFromTree(List<ObjectTreeNode> treeNodes) {
		for(ObjectTreeNode treeNode : treeNodes) {
			Object object = treeNode.getUserObject();
			if (object instanceof ComplexContainerObject) {
				((ComplexContainerObject) object).removeFromParent();
			} else if (object instanceof GraphicalObject) {
				((GraphicalObject) object).remove();
			}
			notifyViews(OBJECTTREE_DELETE_OBJECT, treeNode);
		}
	}
}
