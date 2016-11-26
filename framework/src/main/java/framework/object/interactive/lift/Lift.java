package framework.object.interactive.lift;

import java.util.*;

import framework.object.interactive.*;
import framework.types.*;

public class Lift extends InteractiveObjectImpl {

	private Set<LiftSwitch> liftSwitchs = new HashSet<LiftSwitch>(); 
	
	private List<LiftPoint> liftPoints = new ArrayList<LiftPoint>();
	
	private int currentLiftPoint = 0;
	
	private int targetLiftPoint;
	
	private int liftPointDirection = 1;
	
	private Vector3D liftDirection = null;
	
	private Vector3D currentLiftPosition = null;
	
	private boolean inUse = false;
		
	
	public void addLiftSwitch(LiftSwitch liftSwitch) {
		liftSwitchs.add(liftSwitch);
		liftSwitch.setLift(this);
	}
	
	public void addLiftSwitchs(Set<Switch> liftSwitchs) {
		for (Switch liftSwitch : liftSwitchs) {
			addLiftSwitch((LiftSwitch)liftSwitch);
		}
	}
	
	public void addLiftPoint(LiftPoint liftPoint) {
		if (currentLiftPosition == null) {
			currentLiftPosition = new Vector3D(liftPoint);
		}
		liftPoint.setLift(this);
		liftPoints.add(liftPoint);
	}
	
	public void addLiftPoints(List<LiftPoint> liftPointList) {
		for (LiftPoint liftPoint : liftPointList) {
			addLiftPoint(liftPoint);
		}
	}
	
	public List<LiftPoint> getLiftPoints() {
		return liftPoints;
	}
	
	public boolean isInUse() {
		return inUse;
	}
	
	public void moveLift() {
		if (Vector3D.dinstance(currentLiftPosition, liftPoints.get(getNextLiftPoint())) > 0.05) {
			getContainerObject().transfare(liftDirection);
			currentLiftPosition.add(liftDirection);
		} else {
			currentLiftPoint = getNextLiftPoint();
			currentLiftPosition = new Vector3D(liftPoints.get(currentLiftPoint));
			if (targetLiftPoint != currentLiftPoint) {
				liftDirection = getLiftDirection();
			} else {
				inUse = false;
			}
		}			
	}
	
	public void moveLiftTo(LiftPoint liftPoint) {
		if (!isInUse()) {
			inUse = true;
			if (currentLiftPoint == liftPoints.indexOf(liftPoint)) return;
			if (currentLiftPosition == null) {
				currentLiftPosition = new Vector3D(liftPoints.get(currentLiftPoint));
			}
			targetLiftPoint = liftPoints.indexOf(liftPoint);
			liftDirection = getLiftDirection();
		}
	}
	
	@Override
	public void use(Object value) {
		if (!isInUse()) {
			inUse = true;	
			if (currentLiftPoint <= 0) {
				liftPointDirection = 1;
			}
			if (currentLiftPoint >= liftPoints.size()-1) {
				liftPointDirection = -1;
			}
			targetLiftPoint = currentLiftPoint + liftPointDirection;
			liftDirection = getLiftDirection();
		}		
	}
	
	public int getNextLiftPoint() {
		return targetLiftPoint > currentLiftPoint ? currentLiftPoint + 1 : currentLiftPoint - 1;
	}
	
	public Vector3D getLiftDirection() {
		return Vector3D.subtract(liftPoints.get(currentLiftPoint), liftPoints.get(getNextLiftPoint())).normalize().invert().multiplyBy(0.05f);
	}
}
