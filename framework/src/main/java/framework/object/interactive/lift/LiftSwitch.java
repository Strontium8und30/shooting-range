package framework.object.interactive.lift;

import framework.object.interactive.*;
import framework.types.*;

public class LiftSwitch extends Switch {

	private Lift lift = null;
	
	public LiftSwitch() {
	}
	
	public LiftSwitch(ContainerObject cObj) {
		super(cObj);
	}
	
	@Override
	public void execute(Object object) {
		if (lift != null) {
			lift.use(object);
		}
	}
	
	public void setLift(Lift lift) {
		this.lift = lift;
	}
}
