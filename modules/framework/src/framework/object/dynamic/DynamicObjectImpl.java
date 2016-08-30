package framework.object.dynamic;

import java.util.*;

import utilities.log.*;
import framework.types.*;

public class DynamicObjectImpl extends SimpleContainerObject implements DynamicObject {
	
	public static Log log = LogFactory.getLog(DynamicObjectImpl.class);
	
	private Vector3D lastPosition;

	private Vector3D powerResult = new Vector3D();
	
	Vector3D pr = new Vector3D();
	
	private Map<Triangle, Triangle> lastCollision = new HashMap<Triangle, Triangle>();
	
	private Map<Triangle, Triangle> checkedCollision = new HashMap<Triangle, Triangle>();
	
	
	public DynamicObjectImpl(ContainerObject containerObject) {
		super(containerObject);
	}
	
	public void addPower(Vector3D power) {
		System.out.println(powerResult.betrag());
		powerResult.add(power);		
		pr = Vector3D.multiplyBy(powerResult, -0.1f);
//		if (powerResult.betrag() < 0.025f) {
//			reset();
//		}
	}
	
	public void calculate(ContainerObject chapterContainerObject) {
		if (powerResult.betrag() > 1.0f) setVisible(false);
		lastPosition = new Vector3D(getGeometricCenter());
		powerResult.add(new Vector3D(0.0f,-0.01f,0.0f));
		transfare(powerResult);

		checkedCollision.clear();
		preCollisionsDetection();
		int colCounter = 0;
		while(collisionsDetection(chapterContainerObject)) {
			if (colCounter++ > 5) {
//				transfare(Vector3D.subtract(lastPosition, getGeometricCenter()));
				checkedCollision.clear();
				lastCollision.clear();
				break;
			}
			lastCollision.clear();
		};
		lastCollision.putAll(checkedCollision);
		if (lastPosition.equals(getGeometricCenter())) {
			reset();
		}
	}
	
	public void reset() {
		powerResult = new Vector3D(0);
	}
	
	public boolean preCollisionsDetection() {
		boolean col = false;
		for (Triangle sObj : lastCollision.keySet()) {
			for (Triangle triangle : getAllSubGraphicalObjects()) {		
				if (checkTriangleTriangleCollision(sObj, triangle)) col = true;
			}
		}
		return col;
	}
	
	public boolean collisionsDetection(ContainerObject containerObject) {
		if (containerObject == this) {
			return false;
		}
		if (containerObject.getGeometricCenter() == null) {
			log.error("Object hat kein geometrisches Zentrum:" + containerObject);
			System.exit(0);
		}
		if (!containerObject.isHard()) return false; 
		if (!((SimpleContainerObject) containerObject).isPointInRange(getGeometricCenter())) return false;
		boolean col = false;
		int cObjectCount = containerObject.getContainerObjectCount();
		for(int cObjIndex = 0; cObjIndex < cObjectCount; cObjIndex++) {
			if (collisionsDetection(containerObject.getContainerObject(cObjIndex))) col = true;
		}
		
		int gObjectCount = containerObject.getGraphicalObjectCount();
		for(int gObjIndex = 0; gObjIndex < gObjectCount; gObjIndex++) {
			Triangle sObj = containerObject.getGraphicalObject(gObjIndex);
			if (!lastCollision.containsKey(sObj)) {				
				for (Triangle triangle : getAllSubGraphicalObjects()) {					
					if (checkTriangleTriangleCollision(sObj, triangle)) col = true;		
				}
			}
		}
		return col;
	}
	
	public boolean checkTriangleTriangleCollision(Triangle triangle1, Triangle triangle2) {
		float dst2 = 0;
		float dstTmp = 0;
		
		if (triangle1.checkTriangleTriangleCollision(triangle2)) {
			checkedCollision.put(triangle1, triangle2);
			Vector3D rel = Vector3D.subtract(lastPosition, triangle1.getVertex(2));
			float dst = Vector3D.produkt(triangle1.getNormalVector(), rel);
												
			for (int i = 0; i < 3;) {
				rel = Vector3D.subtract(triangle2.getVertex(i), triangle1.getVertex(2));
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
				transfare(Vector3D.multiplyBy(triangle1.getNormalVector(), dstTmp * -1.01f));				
			
				addPower(pr);
							
				if (powerResult.betrag() < 0.0001f) {
					powerResult = new Vector3D(0);
					pr = new Vector3D(0);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isActive() {
		return !powerResult.isNull();
	}
}
