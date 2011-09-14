package isnork.g2.utilities;

import java.awt.geom.Point2D;

/**Class to represent nodes in destination queue*/
public class DestinationPoints implements Comparable{
	
	public Point2D location = new Point2D.Double(0, 0);
	public double happinesstogain = 0;
	public Point2D whereIAm = new Point2D.Double(0, 0);
	
	public DestinationPoints(Point2D l, Point2D i, double h){
		location = l;
		happinesstogain = h;
		whereIAm = i;
	}
	
	public int compareTo(Object o) {
				
		if(this.happinesstogain > ((DestinationPoints) o).happinesstogain)
			return -1;
		
		if(happinesstogain < ((DestinationPoints) o).happinesstogain)
			return 1;
		
		if(whereIAm.distance(location) > whereIAm.distance(((DestinationPoints) o).location))
			return -1;
		
		if(whereIAm.distance(location) < whereIAm.distance(((DestinationPoints) o).location))
			return 1;
		
		return 0;
	}
	
	public String toString(){
		return location + ", " + happinesstogain;
	}

}
