package framework.object.interactive;

import java.util.*;

import framework.object.interactive.lift.*;

public enum SwitchType {
	REQEUST_LIFT("Lift rufen", LiftPointSwitch.class),
	USE_LIFT("Lift benutzen", LiftSwitch.class);
	
	
	private String name;
	
	private Class<? extends Switch> switchType;
	
	
	private SwitchType(String name, Class<? extends Switch> switchType) {
		this.name = name;
		this.switchType = switchType;
	}
	
	public String getName() {
		return name;
	}
	
	public Class<? extends Switch> getType() {
		return switchType;
	}
	
	public static SwitchType getSwitchType(Class<? extends Switch> type) {
		return Arrays.stream(values()).filter(st -> st.getType() == type).findAny().get();
	}
	
	@Override
	public String toString() {
		return name;
	}
}
