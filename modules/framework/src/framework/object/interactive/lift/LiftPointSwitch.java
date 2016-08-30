package framework.object.interactive.lift;

import framework.object.interactive.*;


public class LiftPointSwitch extends Switch {
		
	private LiftPoint liftPoint = null;
	
	
	public LiftPointSwitch() {
		
	}
	
	public LiftPointSwitch(LiftPoint liftPoint) {
		this.liftPoint = liftPoint;
	}
	
	public void setLiftPoint(LiftPoint liftPoint) {
		this.liftPoint = liftPoint;
	}
	
	@Override
	public void execute(Object object) {
		liftPoint.moveLiftTo();
	}
}

