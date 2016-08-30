package maptool.object;

import framework.types.*;

public interface ComplexObjectMetaData extends ObjectMetaData {

	public String getName();

	public void setName(String name);
	
	public boolean isSelected();

	public void setSelected(boolean selected);
	
}
