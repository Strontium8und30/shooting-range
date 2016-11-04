package client;

import utilities.log.*;

public abstract class GameThread extends Thread {
	
	/** Logging */
	public static Log log = LogFactory.getLog(GameThread.class);
	
	/** Ist der Thread pausiert */
	private boolean pause = false;
	
	/** Lebt der Thread noch */
	private boolean quit = true;
	
	
	public GameThread(String name) {
		setName(name);
	}
	
	@Override
	public void run() {
		while(quit) {
			
			synchronized (this) {
				while(pause) {	
					try {
						wait();
					} catch (InterruptedException e) {}
				}
			}
			
			loop();
		}
	}
	
	public abstract void loop();
	
	public synchronized void pause() {
		pause = true;
	}
	
	public synchronized void proceed() {
		if (pause) {
			pause = false;
			notify();
		}
	}
	
	public void quit() {
		proceed();
		quit = false;
	}
}
