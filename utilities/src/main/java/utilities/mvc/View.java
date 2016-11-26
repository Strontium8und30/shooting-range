package utilities.mvc;

import java.awt.Component;

public interface View {
	
	/** Gibt das Model der View zurück */
	public Model getModel();
	
	/** Gibt die zugeörige Komponente zurück */
	public Component getComponent();
	
	/** Wird aufgerufen wenn sich im Model Daten veränder haben */
	public void update(EventAction event, Object arg);

}
