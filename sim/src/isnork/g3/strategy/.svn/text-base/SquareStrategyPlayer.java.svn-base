package isnork.g3.strategy;

import java.awt.geom.Point2D;

import java.util.ArrayList;

public class SquareStrategyPlayer
{
	private int playerId;
	private int pathId;
	private int pos;
	private Point2D initNode;
	private ArrayList<Point2D> node;
	//private static final Logger log=Logger.getLogger(SquareStrategyPlayer.class);
	
	public SquareStrategyPlayer(int playerId, int pathId)
	{
		node=new ArrayList<Point2D>();
		this.playerId=playerId;
		this.pathId=pathId;
	}
	
	public void setPos(int pos)
	{
		this.pos=pos;
	}
	
	public int getX()
	{
		return pos;
	}
	
	public void setInitNode(Point2D initNode)
	{
		this.initNode=initNode;
	}
	
	public Point2D getInitNode()
	{
		return initNode;
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
	
	public void setNode(ArrayList<Point2D> node)
	{
		this.node=node;
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
	
	public void buildNodeBasedOnInitAndStandard()
	{
		double x=initNode.getX();
		double y=initNode.getY();
		int count=-1;
		if (x==pos && (y<=pos && y>0))
			count=0;
		else if (x==pos && (y<=0 && y>-pos))
			count=1;
		else if (y==-pos && (x<=pos && x>0))
			count=2;
		else if (y==-pos && (x<=0 && x>-pos))
			count=3;
		else if (x==-pos && (y>=-pos && y<0))
			count=4;
		else if (x==-pos && (y>=0 && y<pos))
			count=5;
		else if (y==pos && (x>=-pos && x<0))
			count=6;
		else if (y==pos && (x>=0 && x<pos))
			count=7;
		
		for (int i=0; i<count; i++)
			switchToNextPoint();
		node.add(0, initNode);
	}
	
	public void buildNodeBasedOnInitAndCorner()
	{
		double x=initNode.getX();
		double y=initNode.getY();
		int count=-1;
		if (x==pos && (y<=pos && y>0))
			count=0;
		else if (x==pos && (y<=0 && y>-pos))
			count=1;
		else if (y==-pos && (x<=pos && x>0))
			count=4;
		else if (y==-pos && (x<=0 && x>-pos))
			count=5;
		else if (x==-pos && (y>=-pos && y<0))
			count=8;
		else if (x==-pos && (y>=0 && y<pos))
			count=9;
		else if (y==pos && (x>=-pos && x<0))
			count=12;
		else if (y==pos && (x>=0 && x<pos))
			count=13;
		for (int i=0; i<count; i++)
			switchToNextPoint();
		node.add(0, initNode);
	}
	
	public String nodeString()
	{
		String s="";
		for (int i=0; i<node.size(); i++)
		{
			s+=node.get(i)+" ";
		}
		return s;
	}
	
	public void addNode(Point2D p)
	{
		node.add(p);
	}
}
