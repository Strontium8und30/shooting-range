package utilities.log;

/**
 * 
 * Eine statische Klasse die ein Log Objekt erzeugt welches einer bestimmten 
 * Klasse zuordbar ist.
 * 
 * @Zweck: Reproduzieren von Fehlern; Fehler sind Klassen zuordbar; Ausgabe von Fehlermeldungen  
 * @author Thorben
 * 
 */
public class LogFactory {
	
	public static Log getLog(Class<?> clazz) {
		Log log = new Log(clazz.getName());
		return log;
	}

}
