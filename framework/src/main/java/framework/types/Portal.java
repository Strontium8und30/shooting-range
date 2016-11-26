package framework.types;


public class Portal implements PortalInf {

	private ContainerObject portalA = null;
	
	private ContainerObject portalB = null;
	
	private PortalType portalType = PortalType.ATOB;

	private long lastUseTime = System.currentTimeMillis();
	
	private long closedTime = 5000;
	
	private boolean used = false;
	
	
	public Portal() {
	}
	
	public Portal(ContainerObject portalA, ContainerObject portalB) {
		this.portalA = portalA;
		this.portalB = portalB;
	}


	@Override
	public ContainerObject getPortalA() {
		return portalA;
	}

	@Override
	public void setPortalA(ContainerObject portalA) {
		this.portalA = portalA;
	}

	@Override
	public ContainerObject getPortalB() {
		return portalB;
	}

	@Override
	public void setPortalB(ContainerObject portalB) {
		this.portalB = portalB;
	}
	
	@Override
	public PortalType getType() {
		return portalType;
	}
	
	@Override
	public void setType(PortalType portalType) {
		this.portalType = portalType;
	}
	
	@Override
	public boolean isATOB() {
		return portalType.equals(PortalType.ATOB) || portalType.equals(PortalType.ATOB_BTOA);
	}
	
	@Override
	public boolean isBTOA() {
		return portalType.equals(PortalType.BTOA) || portalType.equals(PortalType.ATOB_BTOA);
	}
	
	public boolean isUsed() {
		return used;
	}
	
	public void setUsed(boolean used) {
		this.used = used; 
		if (used) {
			lastUseTime = System.currentTimeMillis();
		}
	}
	
	public boolean isOpen() {
		return lastUseTime + closedTime < System.currentTimeMillis();
	}
	
	@Override
	public String toString() {
		return portalA + " " + portalType + " " + portalB;
	}
	

	public enum PortalType {
		ATOB(){
			@Override
			public String toString() {return "->";}
		},
		BTOA(){
			@Override
			public String toString() {return "<-";}
		},
		ATOB_BTOA(){
			@Override
			public String toString() {return "<->";}
		};
	}
}
