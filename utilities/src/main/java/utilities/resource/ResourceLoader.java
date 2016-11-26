package utilities.resource;

import java.io.File;
import java.net.URISyntaxException;

import utilities.log.Log;
import utilities.log.LogFactory;


/**
 * Diese Klasse ließt Daten wie z.B. Größe, Beschriftung usw. aus einer 
 * einer Datei (resource.properties) die im jeweiligen Package abgelegt ist.
 * 
 * @author Thorben
 *
 */
public class ResourceLoader {
	
	public static Log log = LogFactory.getLog(ResourceLoader.class);
	
	public static File getFile(String relPath) {
		File file = null; 
		try {
			file = new File(Thread.currentThread().getContextClassLoader().getResource(relPath).toURI());
		} catch (Exception e) {
			log.error("File not found: " + relPath);
			e.printStackTrace();
		}
		return file;
	}
}
