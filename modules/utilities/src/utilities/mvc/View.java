package utilities.mvc;

import java.awt.Component;

public interface View {
	
	/** Gibt das Model der View zur�ck */
	public Model getModel();
	
	/** Gibt die zuge�rige Komponente zur�ck */
	public Component getComponent();
	
	/** Wird aufgerufen wenn sich im Model Daten ver�nder haben */
	public void update(EventAction event, Object arg);

}
