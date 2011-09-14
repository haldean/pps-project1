package isnork.g3.strategy;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class LineStrategyPlayer
{
	private int playerId;
	private int pathId;
	private int x;
	private ArrayList<Point2D> node;
	
	public LineStrategyPlayer(int playerId, int pathId)
	{
		node=new ArrayList<Point2D>();
		this.playerId=playerId;
		this.pathId=pathId;
	}
	
	public void setX(int x)
	{
		this.x=x;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getPlayerId()
	{
		return playerId;
	}
	
	public int getPathId()
	{
		return pathId;
	}
	
	public ArrayList<Point2D> getNode()
	{
		return node;
	}
	
	public Point2D getCurrentPoint()
	{
		return node.get(0);
	}
	
	public void switchToNextPoint()
	{
		Point2D p=node.get(0);
		node.remove(0);
		node.add(p);
	}
	
	public void addNode(Point2D p)
	{
		node.add(p);
	}
}
