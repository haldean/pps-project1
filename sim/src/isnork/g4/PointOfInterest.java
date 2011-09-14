package isnork.g4;

import isnork.sim.SeaLife;

import java.awt.geom.Point2D;

public class PointOfInterest {
    protected Point2D location;
    protected SeaLife creature;
    protected int age;
    
    public PointOfInterest(Point2D location) {
        this(location, null, false);
    }
    
    public PointOfInterest(Point2D location, SeaLife creature) {
        this(location, creature, true);
    }
    
    public PointOfInterest(Point2D location, SeaLife creature, boolean degrades) {
        this.location = location;
        this.creature = creature;
        this.age = degrades ? 0 : -1;
    }
    
    public Point2D getLocation() { return location; }
    public SeaLife getCreature() { return creature; }
    public int getAge() { return (age > 0 ? age : 0); }
    
    public void age() {
        if(age >= 0) {
            age++;
        }
    }
}
