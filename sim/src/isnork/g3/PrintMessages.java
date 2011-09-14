package isnork.g3;

import isnork.sim.GameEngine;

public class PrintMessages {
	
	private boolean messagesEnabled;
	private int printId;
	private int id;
	public PrintMessages(boolean messagesEnabled, int printId, int id)
	{
		this.messagesEnabled = messagesEnabled;
		this.printId = printId;
		this.id = id;
	}
	
	public void print(String msg)
	{
		if(messagesEnabled && (id == printId || printId == 100))
			GameEngine.println(msg);
	}
}
