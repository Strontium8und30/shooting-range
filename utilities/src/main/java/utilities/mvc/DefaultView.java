package utilities.mvc;

public abstract class DefaultView implements View {

	Model model;
	
	public DefaultView(Model model) {
		this.model = model;
		model.addView(this);
	}
	
	@Override
	public Model getModel() {
		return model;
	}
	
	public void update(EventAction event, Object arg) {
		
	}
}
