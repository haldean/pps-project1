package isnork.g3;

import isnork.sim.SeaLife;
import isnork.sim.Observation;
import isnork.sim.iSnorkMessage;

import java.awt.geom.Point2D;
import java.util.Map;
import java.util.Set;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class WaterProofMessenger implements Messenger {
    private Transcoder xcoder;
    private Pokedex dex;
    private PriorityQueue<OutboundMessage> outboundMessages;
    private OutboundMessage current;
    private Map<Integer,iSnorkBuffer> receivedMessages;
    private Set<SeaLife> discovered;

    private class iSnorkBuffer {
        private Point2D location = null;
        private StringBuilder message = new StringBuilder();

        public boolean hasLocation() {
            return location != null;
        }

        public void setLocation(Point2D location) {
            this.location = location;
        }

        public void add(String c) {
            message.append(c);
        }

        public boolean isComplete() {
            if(message.length() == xcoder.getMessageLength()) {
                return true;
            }
            return false;
        }

        public String getMessage() {
            String retMsg = message.toString();
            message = new StringBuilder();
            return retMsg;
        }

        public String toString() {
            return message.toString();
        }
    }

    public WaterProofMessenger(Pokedex dex, int numDivers, int sideLength) {
        this.dex = dex;
        xcoder = new WaterProofTranscoder(dex.getSpeciesRanking(), sideLength);
        outboundMessages = new PriorityQueue<OutboundMessage>();
        receivedMessages = Maps.newHashMap();
        for(int i=0; i<numDivers; i++) {
            receivedMessages.put(-i, new iSnorkBuffer());
        }
        //System.out.println("received:\n"+receivedMessages.toString());
        discovered = Sets.newHashSet();
    }

    @Override
    public void addReceivedMessages(Set<iSnorkMessage> incomingMessages) {
        for(iSnorkMessage in : incomingMessages) {
            iSnorkBuffer msgBuffer = receivedMessages.get(in.getSender());
            if(msgBuffer == null) {
                continue;
            }
            if(!msgBuffer.hasLocation()) {
                msgBuffer.setLocation(in.getLocation());
            }
            msgBuffer.add(in.getMsg());
            
            if(msgBuffer.isComplete()) {
                discovered.add(xcoder.decode(msgBuffer.getMessage()));
            }
        }
    }

    @Override
    public void addOutboundMessage(Observation seen) {
        String name = seen.getName();
        List<String> encoded = xcoder.encode(name, seen.getId(), seen.getLocation());
        if(encoded == null) {
           return;
        }
        //System.out.println("to send: "+messagesToSend.toString());
        OutboundMessage newOutbound = new OutboundMessage(encoded, dex.getHappiness(name));
        if(!outboundMessages.contains(newOutbound)) {
            outboundMessages.add(newOutbound);
        }
    }

    @Override
    public Set<SeaLife> getDiscovered() {
        Set<SeaLife> discoveredCreatures = Sets.newHashSet(discovered);
        discovered = Sets.newHashSet();
        //System.out.println("Discovered: "+discoveredCreatures.toString());
        return discoveredCreatures;
    }

    @Override
    public String sendNext() {
        // return next message-char in queue
        if(outboundMessages.size() != 0 && current == null) {
            current = outboundMessages.peek();
        }
        if(current == null) {
            return null;
        }
        //System.out.println("outbound: "+outboundMessages);
        //System.out.println("current: "+current);
        String msg = current.characters.poll();
        if(current.characters.size() == 0) {
            outboundMessages.remove(current);
            current = outboundMessages.peek();
        }
        //System.out.println("next: "+msg);
        return msg;
    }

    private class OutboundMessage implements Comparable {
        public double happiness;
        public Queue<String> characters;

        public OutboundMessage(List<String> chars, int happiness) {
            this.happiness = (double) happiness;
            this.characters = new LinkedList<String>();
            this.characters.addAll(chars);
        }

        @Override
        public int compareTo(Object other) {
            return Double.compare(this.happiness, ((OutboundMessage) other).happiness);
        }

        @Override
        public boolean equals(Object other) {
            if(this.characters.size() == WaterProofTranscoder.messageLength
                && ((OutboundMessage) other).characters.size() == this.characters.size()) {
                return this.toString().substring(0,9).equals(other.toString().substring(0,9));
            }
            return this.toString().equals(other.toString());
        }

        @Override
        public int hashCode() {
            return this.toString().hashCode();
        }

        public String toString() {
            return characters.toString()+":"+happiness;
        }
    }
}
