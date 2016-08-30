package client;

import utilities.log.*;

public class Spiel {
	
	/** Logging */
	public static Log log = LogFactory.getLog(Spiel.class);
	
	public static void main(String[] args) {
		
		Log.setLogLevel(Log.LogLevel.INFO);
		Log.setLogDirectory("./log/log.txt");
//		ClientModel clientModel = new ClientModel();
		ClientModel clientModel = new LocalClientModel();
		clientModel.createView();
	}
}
