package utilities.mvc;

import java.util.List;

public interface Model {

	/** Erzeugt die zugehörige View */
	public View createView();
	
	
	/** Fügt ein SubModel hinzu */
	public void addModel(Model model);
	
	/** Löscht die Beziehung zu einem SubModel */
	public void removeModel(Model model);
	
	/** Löscht alle SubModels */
	public void removeModels();
	
	/** Gibt eine Liste von SubModels zurück */
	public List<Model> getModels();
	
	
	/** Fügt eine View hinzu */
	public void addView(View view);
	
	/** Löscht eine View */
	public void removeView(View view);
	
	/** Löscht alle Views */
	public void removeViews();
	
	/** Gibt eine Liste von Views zurück */
	public List<View> getViews();	
	
	/** benachrichtigt alle Views */
	public void notifyViews(EventAction event, Object arg);
	
	/** benachrichtigt alle Views bisauf die sender View*/
	public void notifyViews(EventAction event, Object arg, View sender);
}
