package maptool;

import javax.swing.JPopupMenu;

import maptool.gui.mainFrame.MainModel;
import utilities.log.Log;
import utilities.log.LogFactory;
import utilities.mvc.*;

public class Maptool {

	public static Log log = LogFactory.getLog(Maptool.class);
	
	public static void main(String[] args) {
		
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		
		Log.setLogLevel(Log.LogLevel.INFO);
		Log.setLogDirectory("./log/log.txt");
				
		Model mainModel = new MainModel();
		mainModel.createView();		
	}
}
