package framework.legacy;

import java.io.File;
import java.io.IOException;

import org.jdom.JDOMException;

import framework.types.SimpleContainerObject;
import utilities.gl.TextureHelper;
import utilities.log.Log;
import utilities.log.LogFactory;

public class ImportFacade {
	
	/** Logging */
	public static Log log = LogFactory.getLog(ImportFacade.class);
	
	/** ImportFasade instanz */
	private static ImportFacade instance;
	
	/** ImportDAO instanz */
	private ImportDAO importDAO;
	
	
	private ImportFacade() {
		importDAO = new ImportDAO();
	}

	
	public static ImportFacade getInstance() {
		return instance == null ? instance = new ImportFacade() : instance;
	}
	
	public SimpleContainerObject importToModel(TextureHelper textureHelper, File importFile) {
		try {
			return importDAO.importToModel(textureHelper, importFile);
		} catch (JDOMException e) {
			log.error("Fehler beim Parsen des XML codes", e);
		} catch (IOException e) {
			log.error("Fehler beim Dateizugriff", e);
		}
		return null;
	}
}
