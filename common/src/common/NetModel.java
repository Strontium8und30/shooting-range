package common;

import utilities.log.*;
import utilities.mvc.*;

public abstract class NetModel extends DefaultModel {

	/** Logging */
	public static Log log = LogFactory.getLog(NetModel.class);
	
	/** Ein neuer Client wurde aufgenommen */
	public final static EventAction CLIENT_ADD = new EventAction(); 
	
	/** Ein Client hat die Verbindung beendet */
	public final static EventAction CLIENT_REMOVE = new EventAction();
	
	/** Eine Client Eigenschaft hat sich geändert */
	public final static EventAction CLIENT_CHANGE = new EventAction();
	
	/** Eine Nachricht wurde empfangen */
	public final static EventAction RECEIVE_MESSAGE = new EventAction();
	
}
