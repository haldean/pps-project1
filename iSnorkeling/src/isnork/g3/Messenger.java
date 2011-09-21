package isnork.g3;

import isnork.sim.Observation;
import isnork.sim.SeaLife;
import isnork.sim.iSnorkMessage;

import java.util.Set;

public interface Messenger {
	public void addReceivedMessages(Set<iSnorkMessage> incomingMessages);
	public void addOutboundMessage(Observation seen);
    public Set<SeaLife> getDiscovered();
	public String sendNext();
}