package client.chapter;

import org.jdom.*;

import framework.object.dynamic.*;
import framework.types.*;

public class MapLoader extends chapter.MapLoader {

	@Override
	protected ContainerObject load(ContainerObject parent, Element tag) throws DataConversionException {
		ContainerObject complexObject = super.load(parent, tag);
		if (complexObject.isDynamic()) {
			complexObject = (ContainerObject)new DynamicObjectImpl(complexObject);
			chapter.addDynamicObject((DynamicObject)complexObject);
		}
		return complexObject;
	}
}
