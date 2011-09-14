package isnork.g4;

import isnork.sim.GameObject.Direction;
import isnork.sim.SeaLifePrototype;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

public class Itinerary {
    private static final int AGE_THRESHOLD = 20;
    private static final int DISTANCE_THRESHOLD = 4;
    private static final int MOVEMENT_LENGTH = 6;
    
    private ArrayList<PointOfInterest> goals;
    private int boardRadius;
    private Direction spiralHeading = Direction.S;
    
    public Itinerary(int boardRadius) {
        this.boardRadius = boardRadius;
        goals = new ArrayList<PointOfInterest>();
    }
    
    public void addGoal(PointOfInterest location) {
        goals.add(location);
    }
    
    public void tick(Point2D whereIAm) {
        PointOfInterest poi;
        for(Iterator<PointOfInterest> i = goals.iterator(); i.hasNext();) {
            poi = i.next();
            poi.age();
            if(poi.getAge() > AGE_THRESHOLD ||
                    StaticDiver.getDistanceFromCreature(poi.getLocation(), whereIAm) < DISTANCE_THRESHOLD) {
                i.remove();
            }
        }
    }
    
    public boolean hasNext() {
        return !goals.isEmpty();
    }
    
    public PointOfInterest nextGoal(Point2D whereIAm) {
        if(goals.isEmpty()) return null; //standingOrder(whereIAm);
        return goals.get(0);
    }
    
    protected PointOfInterest standingOrder(Point2D whereIAm) {
        Point2D target = new Point2D.Double(whereIAm.getX() + (spiralHeading.getDx() * MOVEMENT_LENGTH),
                                            whereIAm.getY() + (spiralHeading.getDy() * MOVEMENT_LENGTH));
        
        while(!StaticDiver.pointIsOnBoard(target.getX(), target.getY(), boardRadius)) {
            switch(spiralHeading) {
            case N:
                spiralHeading = Direction.W;
                break;
            case E:
                spiralHeading = Direction.N;
                break;
            case S:
                spiralHeading = Direction.E;
                break;
            case W:
                spiralHeading = Direction.S;
                break;
            }
            
            target = new Point2D.Double(whereIAm.getX() + (spiralHeading.getDx() * MOVEMENT_LENGTH),
                    whereIAm.getY() + (spiralHeading.getDy() * MOVEMENT_LENGTH));
        }
        
        return new PointOfInterest(target);
    }
    
    public void creatureFulfilled(SeaLifePrototype creatureType) {
        PointOfInterest poi;
        for(Iterator<PointOfInterest> i = goals.iterator(); i.hasNext();) {
            poi = i.next();
            if(poi.getCreature() == null)
            	continue;
            if(poi.getCreature().getName().equals(creatureType.getName())) {
                i.remove();
            }
        }
    }
}
