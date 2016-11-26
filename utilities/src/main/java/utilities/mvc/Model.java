package utilities.mvc;

import java.util.List;

public interface Model {

	/** Erzeugt die zugeh�rige View */
	public View createView();
	
	
	/** F�gt ein SubModel hinzu */
	public void addModel(Model model);
	
	/** L�scht die Beziehung zu einem SubModel */
	public void removeModel(Model model);
	
	/** L�scht alle SubModels */
	public void removeModels();
	
	/** Gibt eine Liste von SubModels zur�ck */
	public List<Model> getModels();
	
	
	/** F�gt eine View hinzu */
	public void addView(View view);
	
	/** L�scht eine View */
	public void removeView(View view);
	
	/** L�scht alle Views */
	public void removeViews();
	
	/** Gibt eine Liste von Views zur�ck */
	public List<View> getViews();	
	
	/** benachrichtigt alle Views */
	public void notifyViews(EventAction event, Object arg);
	
	/** benachrichtigt alle Views bisauf die sender View*/
	public void notifyViews(EventAction event, Object arg, View sender);
}
