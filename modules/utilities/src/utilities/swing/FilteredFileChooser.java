package utilities.swing;

import java.util.*;

import javax.swing.*;

public class FilteredFileChooser extends JFileChooser{
		
	public static final int OPEN = 0;
	public static final int SAFE = 1;
	public static final int LOAD = 2;
	public static final int IMPORT = 3;
	public static final int EXPORT = 4;
	
	public FilteredFileChooser() {
		
	}
	
	public FilteredFileChooser(int type) {
		setType(type);
	}
		
	public FilteredFileChooser(List<FileFilter> filter) {
		for(FileFilter fileFilter : filter) {
			addChoosableFileFilter(fileFilter);
		}
	}
	
	public FilteredFileChooser(int type, List<FileFilter> filter) {
		setType(type);
	}
		
	public void setType(int type) {
		switch(type) {
		case OPEN:
			this.setDialogTitle("Öffnen");
			this.setApproveButtonText("Öffnen");
			break;
		case SAFE:
			this.setDialogTitle("Speichern");
			this.setApproveButtonText("Speichern");
			break;
		case LOAD:
			this.setDialogTitle("Laden");
			this.setApproveButtonText("Laden");
			break;
		case IMPORT:
			this.setDialogTitle("Importiren");
			this.setApproveButtonText("Importiren");
			break;
		case EXPORT:
			this.setDialogTitle("Exportiren");
			this.setApproveButtonText("Exportiren");
			break;
		}	
	}
}
