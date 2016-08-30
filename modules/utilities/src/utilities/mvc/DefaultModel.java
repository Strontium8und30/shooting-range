package utilities.mvc;

import java.util.*;


public abstract class DefaultModel implements Model {

	List<Model> models = new ArrayList<Model>();
	
	List<View> views = new ArrayList<View>();
	
	@Override
	public void addModel(Model model) {
		models.add(model);
	}

	@Override
	public void addView(View view) {
		views.add(view);
	}

	@Override
	public List<Model> getModels() {
		return models;
	}

	@Override
	public List<View> getViews() {
		return views;
	}

	public void notifyViews(EventAction event) {
		notifyViews(event, null, null);
	}
	
	@Override
	public void notifyViews(EventAction event, Object arg) {
		notifyViews(event, arg, null);
	}
	
	/** benachrichtigt alle Views bisauf die sender View*/
	public void notifyViews(EventAction event, Object arg, View sender) {
		for(Model model : models) {
			model.notifyViews(event, arg, sender);
		}
		for(View view : views) {
			if (!view.equals(sender)) {
				view.update(event, arg);
			}
		}
	}

	@Override
	public void removeModel(Model model) {
		models.remove(model);
	}

	@Override
	public void removeModels() {
		models.clear();
	}

	@Override
	public void removeView(View view) {
		views.remove(view);
	}

	@Override
	public void removeViews() {
		views.clear();
	}

}
