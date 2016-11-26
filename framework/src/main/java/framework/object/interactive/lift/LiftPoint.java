package framework.object.interactive.lift;

import java.util.*;

import framework.object.interactive.*;
import framework.types.*;

public class LiftPoint extends Vector3D {
	
	private Set<Switch> liftPointSwitchs = new HashSet<Switch>();
	
	private Lift lift = null;
	
	
	public LiftPoint(Vector3D position) {
		this(position, null);
	}
	
	public LiftPoint(Vector3D position, Lift lift) {
		this(position, null, new HashSet<Switch>());
	}
	
	public LiftPoint(Vector3D position, Lift lift, Set<Switch> liftPointSwitchs) {
		super(position);
		this.lift = lift;
		this.liftPointSwitchs = liftPointSwitchs;
	}
	
	
	public void addSwitch(Switch switch_) {
		liftPointSwitchs.add(switch_);
	}
	
	public void addSwitchs(Set<Switch> switchList) {
		liftPointSwitchs.addAll(switchList);
	}
	
	public void removeSwitch(Switch switch_) {
		liftPointSwitchs.remove(switch_);
	}
	
	public Set<Switch> getliftPointSwitchs() {
		return liftPointSwitchs;
	}
	
	public void setLift(Lift lift) {
		this.lift = lift;
	}
	
	public void moveLiftTo() {
		lift.moveLiftTo(this);
	}
}
