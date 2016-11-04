package utilities.swing;

import java.io.*;

public class FileFilter extends javax.swing.filechooser.FileFilter {

	/** Welche Dateiendungen sollen akzeptiert werden */
	String accept;
	
	/** Die Beschreibung der Datei */
	String description;
	
	public FileFilter(String accept, String description) {
		this.accept = accept;
		this.description = description;
	}
	
	@Override
	public boolean accept(File file) {
		return file.isDirectory() || file.getName().toLowerCase().endsWith(accept);
	}

	@Override
	public String getDescription() {
		return description;
	}
}
