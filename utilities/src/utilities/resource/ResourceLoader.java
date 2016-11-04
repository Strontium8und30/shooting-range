package utilities.resource;

import java.io.*;
import java.net.*;

import javax.swing.*;

import utilities.log.*;


/**
 * Diese Klasse ließt Daten wie z.B. Größe, Beschriftung usw. aus einer 
 * einer Datei (resource.properties) die im jeweiligen Package abgelegt ist.
 * 
 * @author Thorben
 *
 */
public class ResourceLoader {
	
	public static Log log = LogFactory.getLog(ResourceLoader.class);
	
	URL file = null;
		
	/**
	 * Setzt den Pfad der resource Datei je nach dem in welcher 
	 * Klasse das Objekt erzeugt wird
	 */
	public ResourceLoader(Class<?> clazz) {	
		file = clazz.getResource("resource.properties");
			//file = new File(url.getFile().replace("file:\\", ""));
			//System.out.println(file);
	}

	/**
	 * This method extract the Path of a package  
	 */
//	private String getFilePath(String clazz) {
//		clazz = clazz.substring(clazz.indexOf(' ') + 1, clazz.lastIndexOf('.'));
//		return "/" + clazz.replace('.', '/');
//	}
	
	/**
	 * Diese Methode gibt einen Integer zurück der 
	 */
	public int getInteger(String name) {
		
		String token = null;
				
		try {
			BufferedReader hFile = new BufferedReader(new InputStreamReader(file.openStream()));
	
			do {
				if((token = hFile.readLine()) == null) {
					log.warning("getInteger() Bezeichner wurde nicht gefunden");
					hFile.close();
					return -1;
				}
			} while(token.indexOf(name) == -1 );
				
			hFile.close();
		} catch(IOException e) { 
			return 0;
		}
		return Integer.parseInt(token.substring(token.indexOf("=")+1));
	}
	
	
	public String getString(String name) {
		
		String token = null;
		
		try {
			BufferedReader hFile = new BufferedReader(new InputStreamReader(file.openStream()));
	
			do {
				if((token = hFile.readLine()) == null) {
					log.warning("getString() Bezeichner wurde nicht gefunden");
					hFile.close();
					return "";
				}
			} while(token.indexOf(name) == -1);
				
			hFile.close();
		} catch(IOException e) { 
			e.printStackTrace();
		}
		return token.substring(token.indexOf("=")+1);
	}
	
	
	public JMenuBar getMenu(String name) {
		JMenuBar menuBar = new JMenuBar();
		String[] menues = getString(name).split(";");
		
		for(String menu : menues) {
			
			JMenu tmpMenu = new JMenu(menu);
			String[] menuItems = getString(name + "." + menu).split(";");
			
			for(String menuItem : menuItems) {
				tmpMenu.add(new JMenuItem(menuItem));
			}
			
			menuBar.add(tmpMenu);
		}
		return menuBar;		
	}
}
