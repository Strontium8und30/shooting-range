package framework.types;

public interface ObjectMetaData {
	
	public Long getId();
	
	public void setId(Long id);
	
	public boolean isVisible();
	
	public void setVisible(boolean visible);
	
	public boolean isDynamic();

	public void setDynamic(boolean dynamic);

	public boolean isHard();
	
	public void setHard(boolean val);

	public boolean isShootable();
	
	public void setShootable(boolean val);
	
	public boolean isDestroyable();

	public void setDestroyable(boolean val);
	
	public int getMass();

	public void setMass(int mass);

	public int getPower();
	
	public void setPower(int power);

	public void setMetaData(ObjectMetaData metaData);
}
