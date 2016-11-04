package client.player;

import common.*;
import framework.types.*;

public class NetClient extends Client {

	/** Der Grafischespieler */
	private SimpleContainerObject playerModel;
	

	public NetClient(int id) {
		super(id);
	}
	
	
	public SimpleContainerObject getPlayerModel() {
		return playerModel;
	}

	public void setPlayerModel(SimpleContainerObject playerModel) {
		this.playerModel = playerModel;
	}
}
