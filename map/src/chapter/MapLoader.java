package chapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import framework.object.interactive.Switch;
import framework.object.interactive.lift.Lift;
import framework.object.interactive.lift.LiftPoint;
import framework.object.interactive.lift.LiftPointSwitch;
import framework.types.ContainerObject;
import framework.types.GraphicalObject;
import framework.types.ObjectMetaData;
import framework.types.Portal;
import framework.types.Portal.PortalType;
import framework.types.SimpleContainerObject;
import framework.types.SimpleGraphicalObject;
import framework.types.Vector2D;
import framework.types.Vector3D;
import utilities.Utilities;
import utilities.log.Log;
import utilities.log.LogFactory;


/**
 * Erzeugt ein Objekt der Klasse Map. Geladen wird die Entwicklungsmap (*.xml)
 * Zum lesen der XML Struktur wird JDOM verwendet (Package=org.jdom.*)
 * Eventuell sollten wir noch auf SAXParser umsteigen da dieser wesentlich
 * Resourcen schonender ist.
 * 
 * XML Struktur:
 * <map>		neu Map (root element)
 * <c_obj>		ComplexObject
 * 		name	Der Name des Objektes z.B. Tür oder Haus
 * <s_obj>		SimpleObject
 * <vertex>		3D Vektor x,y,z Werte getrennt durch Kommas
 * 
 * @author Thorben
 *
 */

public class MapLoader {
	
	public static Log log = LogFactory.getLog(MapLoader.class);
	
	private Set<Long> existingIDs = new HashSet<Long>();
	
	private List<ObjectMetaData> idNullObjects = new ArrayList<ObjectMetaData>();
	
	protected Chapter chapter = null;


	public Chapter loadMap(File mapFile) {
		Document doc;
		try {
			if (!mapFile.exists()) {
				return createEmptyChapter();
			}
			
			doc = new SAXBuilder().build(mapFile);
			Element root = doc.getRootElement();
			
			chapter = createEmptyChapter();
			preLoad(root);
			chapter.setMainContainerObject(load(null, root));
			postInitIds();
			portalLoad(root);
			chapter.addSwitchs(switchLoad(root));
			liftLoad(root);
			calcMap(chapter.getMainContainerObject());
		} catch (JDOMException e) {
			log.error("loadMap() Fehler beim lesen des XML-Codes", e);
		} catch (IOException e) {
			log.error("loadMap() Datei wurde nicht gefunden", e);
		}
		return chapter;
	}
	
	private void postInitIds() {
		Long id = new Long(0);
		for (ObjectMetaData object : idNullObjects) {
			while(existingIDs.contains(id)) {id++;} 
			existingIDs.add(id);
			object.setId(id);
		}		
	}

	public Chapter createEmptyChapter() {
		return new Chapter(null);
	}
	
	protected ContainerObject createContainerObject() {
		return new SimpleContainerObject(); 
	}
	
	protected GraphicalObject createGraphicalObject() {
		return new SimpleGraphicalObject(); 
	}
	
	@SuppressWarnings("unchecked")
	public void preLoad(Element root) throws DataConversionException {
		List<Element> textures = root.getChildren("texture");
		for(Element texture : textures) {
			int id = texture.getAttribute("id").getIntValue();
			String file = texture.getAttribute("file").getValue();
			chapter.prepareTexture(id, file);
		}
	}
	
	protected void setAttributes(Element tag, ContainerObject cObj, ContainerObject parent) throws DataConversionException {
		Attribute objectId;
		if ((objectId = tag.getAttribute("id")) != null) {
			Long id = objectId.getLongValue();
			cObj.setId(id);
			existingIDs.add(id);
		} else {
			idNullObjects.add(cObj);
			log.error("Ein Object hat keine Id!");
		}
		
		Attribute objectVisible;
		if ((objectVisible = tag.getAttribute("visible")) != null) {
			cObj.setVisible(objectVisible.getBooleanValue());
		}
		Attribute objectHard;
		if ((objectHard = tag.getAttribute("hard")) != null) {
			cObj.setHard(objectHard.getBooleanValue());
		}
		Attribute objectShootable;
		if ((objectShootable = tag.getAttribute("shootable")) != null) {
			cObj.setShootable(objectShootable.getBooleanValue());
		}
		Attribute objectDynamic;
		if ((objectDynamic = tag.getAttribute("dynamic")) != null) {
			cObj.setDynamic(objectDynamic.getBooleanValue());
		} else {
			if (parent != null) {
				cObj.setDynamic(parent.isDynamic());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void setAttributes(Element tag, GraphicalObject gObj) throws DataConversionException {
		Attribute graphicalId;
		if ((graphicalId = tag.getAttribute("id")) != null) {
			Long id = graphicalId.getLongValue();
			gObj.setId(id);
			existingIDs.add(id);
		} else {
			idNullObjects.add(gObj);
		}
		Attribute graphicalVisible;
		if ((graphicalVisible = tag.getAttribute("visible")) != null) {
			gObj.setVisible(graphicalVisible.getBooleanValue());
		}
		Attribute attr = tag.getAttribute("texture");
		if(attr != null) {
			if (Utilities.isNumber(attr.getValue())) {
				gObj.setTextureID(attr.getIntValue());
			}else {
				gObj.setTextureID(chapter.getTextureID(attr.getValue()));	
			}
		}
		int i_vec = 0;
		for (Element vertexTag : (List<Element>)tag.getChildren("vertex")) {
			String[] vertexValue = vertexTag.getText().split(";");
			gObj.setVertex(i_vec++, new Vector3D(
												Float.valueOf(vertexValue[0]),
												Float.valueOf(vertexValue[1]),
												Float.valueOf(vertexValue[2])));
		}
		i_vec = 0;
		for (Element textureVertexTag : (List<Element>)tag.getChildren("tex_coord")) {
			String[] textureVertexValue = textureVertexTag.getText().split(";");
			gObj.setTextureVector(i_vec++, new Vector2D(
												Float.valueOf(textureVertexValue[0]),
												Float.valueOf(textureVertexValue[1])));
		}
	}
	
	@SuppressWarnings("unchecked")
	protected ContainerObject load(ContainerObject parent, Element tag) throws DataConversionException {
		ContainerObject cObj = createContainerObject();
		setAttributes(tag, cObj, parent);
		List<Element> taggs = tag.getChildren("c_obj");
		for(Element tagg : taggs) {
			ContainerObject complexObject = load(cObj, tagg);				
			cObj.addContainerObject(complexObject);
		}
		List<Element> sObj = tag.getChildren("s_obj");
		for(Element tagg : sObj) {
			GraphicalObject simpleObject = createGraphicalObject();
			setAttributes(tagg, simpleObject);
			cObj.addGraphicalObject(simpleObject);
		}
		cObj.setSize(cObj.calcSize(cObj.getGeometricCenter()));
		return cObj;
	}
		
	private void calcMap(ContainerObject complexObject) {
		for(ContainerObject cObj : complexObject.getContainerObjectList()) {
			calcMap(cObj);
		}
		for (GraphicalObject sObj : complexObject.getGraphicalObjectList()) {
			sObj.calcComplete();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void portalLoad(Element root) {
		for(Element portal : (List<Element>)root.getChildren("portal")) {
			Portal portalObj = new Portal();
			Long portalIdA = new Long(((Element)portal.getChildren("port").get(0)).getValue());
			portalObj.setPortalA((ContainerObject)chapter.getMainContainerObject().getContainerObjectById(portalIdA));
			Long portalIdB = new Long(((Element)portal.getChildren("port").get(1)).getValue());
			portalObj.setPortalB((ContainerObject)chapter.getMainContainerObject().getContainerObjectById(portalIdB));
			portalObj.setType(PortalType.valueOf(portal.getAttributeValue("type")));
			chapter.addPortal(portalObj);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Set<Switch> switchLoad(Element root) {
		Set<Switch> switchs = new HashSet<Switch>();
		for(Element switch_ : (List<Element>)root.getChildren("switch")) {
			Switch switcher = (Switch)getObject(switch_.getAttributeValue("type"));
			switcher.setSwitchOnSound(switch_.getAttributeValue("switchOnSound"));
			switcher.setSwitchOffSound(switch_.getAttributeValue("switchOffSound"));
			Long switchId = new Long(((Element)switch_.getChildren("object").get(0)).getValue());
			switcher.setContainerObject((ContainerObject)chapter.getMainContainerObject().getContainerObjectById(switchId));
			switchs.add(switcher);	
		}
		return switchs;
	}
	
	@SuppressWarnings("unchecked")
	public void liftLoad(Element root) {
		for(Element liftElement : (List<Element>)root.getChildren("lift")) {
			Lift lift = new Lift();
			Long liftObjId = new Long(((Element)liftElement.getChildren("object").get(0)).getValue());
			lift.setContainerObject((ContainerObject)chapter.getMainContainerObject().getContainerObjectById(liftObjId));
			lift.addLiftPoints(liftPointLoad(liftElement));
			Set<Switch> liftSwitchs = switchLoad(liftElement);
			lift.addLiftSwitchs(liftSwitchs);
			chapter.addSwitchs(liftSwitchs);
			chapter.addLift(lift);
			
			for (LiftPoint liftPoint : lift.getLiftPoints()) {
				for (Switch liftPointSwitch : liftPoint.getliftPointSwitchs()) {
					((LiftPointSwitch)liftPointSwitch).setLiftPoint(liftPoint);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<LiftPoint> liftPointLoad(Element element) {
		List<LiftPoint> liftPointList = new ArrayList<LiftPoint>();
		for(Element liftPointElement : (List<Element>)element.getChildren("liftpoint")) {
			String[] vectorStr = liftPointElement.getChild("position").getValue().split(";");
			LiftPoint liftPoint = new LiftPoint(new Vector3D(Float.valueOf(vectorStr[0]),
										 				 Float.valueOf(vectorStr[1]),
										 				 Float.valueOf(vectorStr[2])));
			for (Switch switch_ : switchLoad(liftPointElement)) {
				((LiftPointSwitch) switch_).setLiftPoint(liftPoint);
				liftPoint.addSwitch(switch_);
				chapter.addSwitch(switch_);
			}
			liftPointList.add(liftPoint);
		}
		return liftPointList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Vector3D> vectorLoad(Element element, String tagName) {
		List<Vector3D> vectorlList = new ArrayList<Vector3D>();
		for(Element vector : (List<Element>)element.getChildren(tagName)) {
			String[] vectorStr = vector.getValue().split(";");
			vectorlList.add(new Vector3D(Float.valueOf(vectorStr[0]),
										 Float.valueOf(vectorStr[1]),
										 Float.valueOf(vectorStr[2])));
		}
		return vectorlList;
	}
	
//	@SuppressWarnings("unchecked")
//	public static List<Vector3D> eqipmentBoxLoad(Element root, String tagName) {
//		for(Element liftElement : (List<Element>)root.getChildren("equipment")) {
//			Lift lift = new Lift();
//			Long liftObjId = new Long(((Element)liftElement.getChildren("object").get(0)).getValue());
//			lift.setContainerObject((ContainerObject)chapter.getMainContainerObject().getContainerObjectById(liftObjId));
//			lift.addLiftPoints(liftPointLoad(liftElement));
//			Set<Switch> liftSwitchs = switchLoad(liftElement);
//			lift.addLiftSwitchs(liftSwitchs);
//			chapter.addSwitchs(liftSwitchs);
//			chapter.addLift(lift);
//			
//			for (LiftPoint liftPoint : lift.getLiftPoints()) {
//				for (Switch liftPointSwitch : liftPoint.getliftPointSwitchs()) {
//					((LiftPointSwitch)liftPointSwitch).setLiftPoint(liftPoint);
//				}
//			}
//		}
//	}
	
	public static Object getObject(String className) {
		try	{
			Class<?> c = Class.forName(className);
			return c.newInstance();
		} catch(ClassNotFoundException e) {
			System.out.println("Klasse nicht gefunden: " + className);
		} catch(IllegalAccessException ie) {
			ie.printStackTrace(System.err);
		} catch(InstantiationException insterr) {
			insterr.printStackTrace(System.err);
		}
		return null;
	}
}