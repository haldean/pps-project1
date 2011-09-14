package isnork.g4;

import java.awt.geom.Point2D;

import isnork.sim.iSnorkMessage;

public class iSnorkTransmission {
    protected String msg;
    protected Point2D location;
    protected int sender;
    
    public iSnorkTransmission(iSnorkMessage initialMessage) {
        this.msg = initialMessage.getMsg();
        this.location = initialMessage.getLocation();
        this.sender = initialMessage.getSender();
    }
    
    public iSnorkTransmission( int sender, Point2D location ) {
        this.msg = " ";
        this.location = location;
        this.sender = sender;
    }
    
    public String getMsg() {
        return msg;
    }
    
    public Point2D getLocation() {
        return location;
    }
    
    public int getSender() {
        return sender;
    }
    
    public void appendMessage(iSnorkMessage newMessage) {
        if(this.sender == newMessage.getSender()) {
            this.msg += (newMessage == null ? " " : newMessage.getMsg());
        }
    }
}
