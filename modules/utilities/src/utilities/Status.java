package utilities;

public enum Status {
	SUCSESS(2, "Erfolgreich"),
	INFO(1, "Info"),
	DEFAULT(0, "Default"),
	DEBUG(-1, "Debug"),
	WARNING(-2, "Warnung"),
	ERROR(-3, "Fehler");
	
	/** Die ID des Status */
	private int id;
	
	/** Die Mitteilung des Status */
	private String message;
	
	private Status(int id, String defaultMessage) {
		this.id = id;
		this.message = defaultMessage;
	}

	public int getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
